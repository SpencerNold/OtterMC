@echo off

set "THIS=distributors\windows"
set "TARGET_DIR=%THIS%\staging"
if not exist "%TARGET_DIR%" mkdir "%TARGET_DIR%"

REM -------- Version JARs --------
set "OMC_DIR=%TARGET_DIR%\ottermc"
if not exist "%OMC_DIR%" mkdir "%OMC_DIR%"
if not exist "%OMC_DIR%\versions" mkdir "%OMC_DIR%\versions"
copy "versions\vanilla-1.8.9\build\libs\vanilla-1.8.9-remapped-joined-safe.jar" "%OMC_DIR%\versions\vanilla-1.8.9.jar" /Y
copy "versions\vanilla-1.21.10\build\libs\vanilla-1.21.10-remapped-joined-safe.jar" "%OMC_DIR%\versions\vanilla-1.21.10.jar" /Y

REM -------- Client JAR --------
copy "client\build\libs\client-joined.jar" "%OMC_DIR%\client.jar" /Y

REM -------- JSON file0 --------
set "JAR_DIR=%TARGET_DIR%\versions\ottermc-client"
if not exist "%JAR_DIR%" mkdir "%JAR_DIR%"
copy "client\ottermc-client.json" "%JAR_DIR%\ottermc-client.json" /Y

REM -------- JSON file1 --------
copy "client\manifest.json" "%OMC_DIR%\manifest.json" /Y

REM -------- Wrapper JAR --------
set "WRAPPER_DIR=%TARGET_DIR%\libraries\io\github\ottermc\wrapper\1.0.0"
if not exist "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"
copy "wrapper\build\libs\wrapper.jar" "%WRAPPER_DIR%\wrapper-1.0.0.jar" /Y

REM -------- Window JAR --------
copy "window\build\libs\window.jar" "%OMC_DIR%\window.jar"

REM -------- Profiler JAR --------
copy "distributors\profiler\build\libs\profiler-all.jar" "%TARGET_DIR%\profiler.jar"

cd %TARGET_DIR%
cd ..
"C:\Program Files (x86)\Inno Setup 6\iscc.exe" installer.iss
copy "Output\OtterMC.exe" "..\..\OtterMC.exe"
cd ..\..