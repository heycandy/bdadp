#!/usr/bin/env bash

echo "Hello, ARK!"
echo "PID: $$"
echo "PPID: $PPID"
echo "UID: $UID"

BASEDIR=$(cd `dirname $0`; cd ..; pwd)
echo $BASEDIR

# -----------------------------------------------------------------------------
# Control Script for the ARK Server
#
# Environment Variable Prerequisites
#
#   JAVA_HOME       Must point at your Java Development Kit installation.
#                   Required to run the with the "debug" argument.
#
#   JRE_HOME        Must point at your Java Runtime installation.
#                   Defaults to JAVA_HOME if empty. If JRE_HOME and JAVA_HOME
#                   are both set, JRE_HOME is used.
#
#   JAVA_OPTS       (Optional) Java runtime options used when any command
#                   is executed.
#
#   ARK_HOME        Default "$BASEDIR"
#
#   ARK_WAR         Default "$BASEDIR"
#
#   ARK_ENV         Default "local"
#
#   ARK_PORT        Default "8080"
#
#   ARK_HOST        Default "localhost"
#
#   ARK_CONTEXT     Default "/ark"
#
#   ARK_COMPONENTS_DIR Default "$ARK_WAR/components"
# -----------------------------------------------------------------------------

export ARK_HOME="$BASEDIR"
export ARK_WAR="$BASEDIR"
export ARK_ENV=local
export ARK_PORT=8080
export ARK_HOST=localhost
export ARK_CONTEXT="/ark"
export ARK_COMPONENTS_DIR="$ARK_WAR/components"

JAVA_BIN=java
if test -n $JAVA_HOME; then
    JAVA_BIN=$JAVA_HOME/bin/java
fi
if test -n $JRE_HOME; then
    JAVA_BIN=$JRE_HOME/bin/java
fi

JAVA_OPTS="-Xms512m -Xmx512m -XX:PermSize=256m -XX:MaxPermSize=256m -Duser.components.dir=$ARK_COMPONENTS_DIR"
JAVA_AGRS="$ARK_PORT $ARK_HOST $ARK_CONTEXT"

MAIN_CLASS=com.chinasofti.ark.bdadp.standalone.Launcher

echo "Starting ARK..."
nohup "$JAVA_BIN" -cp ".:$ARK_HOME:$ARK_WAR/WEB-INF:$ARK_WAR/WEB-INF/lib/*" $JAVA_OPTS $MAIN_CLASS $JAVA_AGRS > $BASEDIR/logs/ark.out 2>&1 &

PIDFILE="$BASEDIR/current.pid"

echo $! > $PIDFILE
echo "Start success"

