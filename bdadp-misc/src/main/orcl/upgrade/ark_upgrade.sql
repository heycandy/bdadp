-- -------------------------------------------
-- 修改ARK_USER表名为Users，统一Mysql数据库用户表名称
-- Created by White on 2017/05/05.
-- -------------------------------------------
alter table ARK_USER rename Users;

UPDATE Users SET userName='YWRtaW4=' WHERE userId='9cee02c6a9f848eeba3119b8a828c66d';
