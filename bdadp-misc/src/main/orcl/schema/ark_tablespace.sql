--创建表空间路径
--mkdir -p /opt/Oracle/oracle/oradata/orcl/orclpdb/ark

--创建数据表空间
create tablespace arkdata
logging
datafile '/opt/Oracle/oracle/oradata/orcl/orclpdb/ark/data.dbf'
size 50m
autoextend on
next 50m maxsize 20480m
extent management local;

--创建临时空间
create temporary tablespace arktemp 
tempfile '/opt/Oracle/oracle/oradata/orcl/orclpdb/ark/temp.dbf'
size 50m 
autoextend on 
next 50m maxsize 20480m 
extent management local;

--创建用户并指定表空间
create user arkdba identified by arkdba default tablespace arkdata temporary tablespace arktemp;

--授权用户
grant create user,drop user,alter user,create any view,connect,resource,dba,create session,create any sequence to arkdba;
