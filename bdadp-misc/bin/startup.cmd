@echo off

echo Hello, ARK!

cd %~dp0
cd ..

set "BASEDIR=%cd%"
echo %BASEDIR%

rem -----------------------------------------------------------------------------
rem Control Script for the ARK Server
rem
rem Environment Variable Prerequisites
rem
rem   JAVA_HOME       Must point at your Java Development Kit installation.
rem                   Required to run the with the "debug" argument.
rem
rem   JRE_HOME        Must point at your Java Runtime installation.
rem                   Defaults to JAVA_HOME if empty. If JRE_HOME and JAVA_HOME
rem                   are both set, JRE_HOME is used.
rem
rem   JAVA_OPTS       (Optional) Java runtime options used when any command
rem                   is executed.
rem
rem   ARK_HOME        Default "$BASEDIR"
rem
rem   ARK_WAR         Default "$BASEDIR"
rem
rem   ARK_ENV         Default "local"
rem
rem   ARK_PORT        Default "8080"
rem
rem   ARK_HOST        Default "localhost"
rem
rem   ARK_CONTEXT     Default "/ark"
rem
rem   ARK_COMPONENTS_DIR Default "$ARK_WAR/components"
rem -----------------------------------------------------------------------------

set "ARK_HOME=%BASEDIR%"
set "ARK_WAR=%BASEDIR%"
set "ARK_ENV=local"
set "ARK_PORT=8080"
set "ARK_HOST=localhost"
set "ARK_CONTEXT=/ark"
set "ARK_COMPONENTS_DIR=%ARK_WAR%\components"

set "JAVA_BIN=java"
if not "%JAVA_HOME%" == "" set "JAVA_BIN=%JAVA_HOME%\bin\java"
if not "%JRE_HOME%" == "" set "JAVA_BIN=%JRE_HOME%\bin\java"

set "JAVA_OPTS=-Xms512m -Xmx512m -XX:PermSize=256m -XX:MaxPermSize=256m -Duser.components.dir=%ARK_COMPONENTS_DIR%"
set "JAVA_AGRS=%ARK_PORT% %ARK_HOST% %ARK_CONTEXT%"

set "MAIN_CLASS=com.chinasofti.ark.bdadp.standalone.Launcher"

echo Starting ARK...
"%JAVA_BIN%" -cp ".;%ARK_HOME%;%ARK_WAR%/WEB-INF;%ARK_WAR%/WEB-INF/lib/*" %JAVA_OPTS% %MAIN_CLASS% %JAVA_AGRS%
