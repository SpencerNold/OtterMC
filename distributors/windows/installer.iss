[Setup]
AppName=OtterMC
AppVersion=1.0.0
DefaultDirName={userappdata}\.minecraft
OutputBaseFilename=OtterMC
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=icon.ico

[Files]
Source: "staging/**"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Code]
procedure CurStepChanged(CurStep: TSetupStep);
var
  JRE8Path, JRE21Path: string;
  ProfilerJar, MCDir: string;
  ResultCode: Integer;
begin
  if CurStep = ssPostInstall then
  begin
    JRE8Path := ExpandConstant('{app}\jre\8\bin\javaw.exe');
    if not FileExists(JRE8Path) then
    begin
      MsgBox('Bundled JRE8 missing!', mbError, MB_OK);
      Abort;
    end;
    JRE21Path := ExpandConstant('{app}\jre\21\bin\javaw.exe');
    if not FileExists(JRE21Path) then
    begin
      MsgBox('Bundled JRE21 missing!', mbError, MB_OK);
      Abort;
    end;
    ProfilerJar := ExpandConstant('{app}\profiler.jar');
    MCDir := ExpandConstant('{app}');
    if not Exec(JRE8Path,
        '-cp "' + ProfilerJar + '" io.github.ottermc.Profiler "' + MCDir + '" "bin\javaw.exe"',
        '',
        SW_SHOW,
        ewWaitUntilTerminated,
        ResultCode
    ) then
    begin
      MsgBox('Failed to run profiler!', mbError, MB_OK);
    end;
    DeleteFile(ProfilerJar);
    if ResultCode <> 0 then
    begin
      MsgBox('Profiler exited with code ' + IntToStr(ResultCode), mbError, MB_OK);
      Abort;
    end;
  end;
end;