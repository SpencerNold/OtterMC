[Setup]
AppName=OtterMC
AppVersion=1.0.0
DefaultDirName={userappdata}\.minecraft
OutputBaseFilename=OtterMC
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest

[Files]
Source: "staging/**"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Code]
procedure CurStepChanged(CurStep: TSetupStep);
var
  LatestClientJarSrc, LatestClientJarDst: string;
  V189ClientJarSrc, V189ClientJarDst: string;
begin
  if CurStep = ssPostInstall then
  begin
    LatestClientJarSrc := ExpandConstant('{userappdata}\.minecraft\versions\1.21.8\1.21.8.jar');
    LatestClientJarDst := ExpandConstant('{userappdata}\.minecraft\versions\ottermc-latest\ottermc-latest.jar');
    if not FileExists(LatestClientJarSrc) then
    begin
      MsgBox('Please run Minecraft version 1.21.8 before running this!', mbError, MB_OK);
      Abort;
    end;
    if FileExists(LatestClientJarSrc) then
    begin
      ForceDirectories(ExpandConstant('{userappdata}\.minecraft\versions\ottermc-latest'));
      CopyFile(LatestClientJarSrc, ExpandConstant(LatestClientJarDst), False);
    end;
    V189ClientJarSrc := ExpandConstant('{userappdata}\.minecraft\versions\1.8.9\1.8.9.jar');
    V189ClientJarDst := ExpandConstant('{userappdata}\.minecraft\versions\ottermc-v1.8.9\ottermc-v1.8.9.jar');
    if not FileExists(V189ClientJarSrc) then
    begin
      MsgBox('Please run Minecraft version 1.8.9 before running this!', mbError, MB_OK);
      Abort;
    end;
    if FileExists(V189ClientJarSrc) then
    begin
      ForceDirectories(ExpandConstant('{userappdata}\.minecraft\versions\ottermc-v1.8.9'));
      CopyFile(V189ClientJarSrc, ExpandConstant(V189ClientJarDst), False);
    end;
  end;
end;