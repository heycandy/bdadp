#!/usr/bin/env bash

echo "Hello ARK!"
echo "PID: $$"
echo "PPID: $PPID"
echo "UID: $UID"

BASEDIR=$(cd `dirname $0`; cd ..; pwd)

echo $BASEDIR

PIDFILE="$BASEDIR/current.pid"

if [ ! -f "$PIDFILE" ]; then
    echo "ARK not running...exit"
    exit 0
fi

PID=`cat $PIDFILE`

echo "Stopping ARK..."

kill -9 $PID
rm $PIDFILE

echo "Stop success"


