-- -------------------------------------------
-- 修改User表名为Users，统一Oracle数据库用户表名称
-- Created by White on 2017/05/05.
-- -------------------------------------------
ALTER TABLE User rename Users;

-- -------------------------------------------
-- 增加一个排序字段，解决持久化后乱序的问题
-- Created by White on 2017/05/15.
-- -------------------------------------------
ALTER TABLE ScenarioGraphEdge ADD `order_id` int(11) DEFAULT 0;
