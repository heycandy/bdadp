#!/bin/bash
KEY_USERNAME=database.username
KEY_PASSWORD=database.password
KEY_DBNAME=database.dbname
BASE_DIR=`dirname $0`/..
FILE_PATH=$BASE_DIR/orm.properties

get_prop()
{
  STRING=`grep $1 $FILE_PATH | tr -d ["\r"]`
 
  OFFSTRING=${STRING#*=}
 
  echo $OFFSTRING
}

USER=`get_prop $KEY_USERNAME`

PASSWD=`get_prop $KEY_PASSWORD`

DB=`get_prop $KEY_DBNAME`

echo -e "\033[1m\033[36mConnectInfo: user=$USER password=$PASSWD db=$DB \033[0m"
cd $BASE_DIR/scripts/sql

for ele in `ls ./|grep .sql`
do
   echo -e "\033[1m\033[36mExecuting file >>>  [$ele] \033[0m"
   msg=`mysql -u$USER -p$PASSWD $DB < $ele 2>&1` 
   if [ -z "$msg" ]
    then
     echo -e "\033[1m\033[32mExecuting successful >>> [$ele]\033[0m"
   else
     echo -e "\033[1m\033[31mERROR: $msg \033[0m"   
   fi
done
