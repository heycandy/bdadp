-- -------------------------------------------
-- 修改User表名为Users，统一Oracle数据库用户表名称
-- Created by White on 2017/05/05.
-- -------------------------------------------
alter table User rename Users;

UPDATE Users SET userName='YWRtaW4=' WHERE userId='9cee02c6a9f848eeba3119b8a828c66d';
