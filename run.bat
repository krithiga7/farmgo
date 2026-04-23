@echo off
echo Starting FarmGo Java Backend...

REM Check if Tomcat is installed
if not exist "%CATALINA_HOME%" (
    echo Apache Tomcat is not installed or CATALINA_HOME is not set
    echo Please install Tomcat and set the CATALINA_HOME environment variable
    pause
    exit /b 1
)

REM Deploy the application
echo Deploying application to Tomcat...
copy target\farmgo-backend-1.0-SNAPSHOT.war "%CATALINA_HOME%\webapps\farmgo.war"

if %errorlevel% equ 0 (
    echo Deployment successful!
    echo Starting Tomcat server...
    call "%CATALINA_HOME%\bin\startup.bat"
    echo Server started! Access the application at http://localhost:8080/farmgo
) else (
    echo Deployment failed!
    pause
    exit /b 1
)

pause