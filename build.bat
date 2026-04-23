@echo off
echo Building FarmGo Java Backend...

REM Check if Maven is installed
mvn -v >nul 2>&1
if %errorlevel% neq 0 (
    echo Maven is not installed or not in PATH
    echo Please install Maven and try again
    pause
    exit /b 1
)

REM Clean and build the project
mvn clean install

if %errorlevel% equ 0 (
    echo Build successful!
    echo The WAR file is located in the target directory
) else (
    echo Build failed!
    pause
    exit /b 1
)

pause