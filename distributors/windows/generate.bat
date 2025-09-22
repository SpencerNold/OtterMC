@echo off

set "THIS=distributors\windows"
set "TARGET_DIR=%THIS%\staging"
if not exist "%TARGET_DIR%" mkdir "%TARGET_DIR%"

REM -------- Client JARs --------
set "OMC_DIR=%TARGET_DIR%\ottermc"
if not exist "%OMC_DIR%" mkdir "%OMC_DIR%"
copy "client-latest\build\libs\client-latest-remapped-joined.jar" "%OMC_DIR%\client-latest.jar" /Y
copy "client-v1.8.9\build\libs\client-v1.8.9-remapped-joined.jar" "%OMC_DIR%\client-v1.8.9.jar" /Y

REM -------- JSON files --------
set "LATEST_DIR=%TARGET_DIR%\versions\ottermc-latest"
if not exist "%LATEST_DIR%" mkdir "%LATEST_DIR%"
copy "client-latest\latest.json" "%LATEST_DIR%\ottermc-latest.json" /Y
set "V189_DIR=%TARGET_DIR%\versions\ottermc-v1.8.9"
if not exist "%V189_DIR%" mkdir "%V189_DIR%"
copy "client-v1.8.9\v1.8.9.json" "%V189_DIR%\ottermc-v1.8.9.json" /Y

REM -------- Wrapper JAR --------
set "WRAPPER_DIR=%TARGET_DIR%\libraries\io\github\ottermc\wrapper\1.0.0"
if not exist "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"
copy "wrapper\build\libs\wrapper-all.jar" "%WRAPPER_DIR%\wrapper-1.0.0.jar" /Y

REM -------- Profiler JAR --------
copy "distributors\profiler\build\libs\profiler-all.jar" "%TARGET_DIR%\profiler.jar"

cd %TARGET_DIR%
cd ..
"C:\Program Files (x86)\Inno Setup 6\iscc.exe" installer.iss
copy "Output\OtterMC.exe" "..\..\OtterMC.exe"
cd ..\..