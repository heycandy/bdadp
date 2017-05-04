

CREATE TABLE T_Component   (
  component_id   NVARCHAR2(255) NOT NULL ,
  component_name   NVARCHAR2(255) NULL ,
  component_desc   NVARCHAR2(255) NULL ,
  component_status   NUMBER NULL ,
  component_pid   NVARCHAR2(255) NULL ,
  component_base   NUMBER NULL ,
  component_type   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of Component
-- ----------------------------

-- ----------------------------
-- Table structure for ComponentConfig
-- ----------------------------

CREATE TABLE T_ComponentConfig   (
  param_id   NVARCHAR2(255) NOT NULL ,
  param_name   NVARCHAR2(255) NULL ,
  param_desc   NVARCHAR2(255) NULL ,
  param_type   NVARCHAR2(255) NULL ,
  default_value   NVARCHAR2(255) NULL ,
  default_options   NVARCHAR2(255) NULL ,
  nullable   NUMBER NULL ,
  order_id   NUMBER NULL ,
  component_id   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of ComponentConfig
-- ----------------------------

-- ----------------------------
-- Table structure for Permission
-- ----------------------------

CREATE TABLE T_Permission   (
  permissionId   NVARCHAR2(255) NOT NULL ,
  permissionName   NVARCHAR2(255) NULL ,
  permissionDesc   NVARCHAR2(255) NULL ,
  createTime   DATE NULL ,
  createUser   NVARCHAR2(255) NULL ,
  modifiedTime   DATE NULL ,
  modifiedUser   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of Permission
-- ----------------------------
INSERT INTO T_Permission   VALUES ('eb5a66dc25974daba14d03e16d877a82', 'admin', 'administrator', TO_DATE('2016-10-08 12:10:23', 'YYYY-MM-DD HH24:MI:SS'), '9cee02c6a9f848eeba3119b8a828c66d', null, null);

-- ----------------------------
-- Table structure for PermissionJoinScope
-- ----------------------------

CREATE TABLE T_PermissionJoinScope   (
  permissionId   NVARCHAR2(255) NOT NULL ,
  scopeId   NVARCHAR2(255) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of PermissionJoinScope
-- ----------------------------
INSERT INTO T_PermissionJoinScope   VALUES ('eb5a66dc25974daba14d03e16d877a82', 'c535d63fb2ec4f97b54aa37325a66d90');

-- ----------------------------
-- Table structure for Role
-- ----------------------------

CREATE TABLE T_Role   (
  roleId   NVARCHAR2(255) NOT NULL ,
  roleName   NVARCHAR2(255) NULL ,
  roleDesc   NVARCHAR2(255) NULL ,
  createTime   DATE NULL ,
  createUser   NVARCHAR2(255) NULL ,
  modifiedTime   DATE NULL ,
  modifiedUser   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of Role
-- ----------------------------
INSERT INTO T_Role   VALUES ('1a9f8265c51e4be395b18a4f0cb6c782', 'dev', 'develop', TO_DATE('2016-10-08 12:10:24', 'YYYY-MM-DD HH24:MI:SS'), '9cee02c6a9f848eeba3119b8a828c66d', null, null);
INSERT INTO T_Role   VALUES ('4a8ee65731324e5fbb71e1be0c0c60cc', 'oper', 'operation', TO_DATE('2016-10-08 12:10:24', 'YYYY-MM-DD HH24:MI:SS'), '9cee02c6a9f848eeba3119b8a828c66d', null, null);
INSERT INTO T_Role   VALUES ('5297eac423d845308c656cfc1808c320', 'admin', 'administrator', TO_DATE('2016-10-08 12:10:24', 'YYYY-MM-DD HH24:MI:SS'), '9cee02c6a9f848eeba3119b8a828c66d', null, null);

-- ----------------------------
-- Table structure for RoleJoinPermission
-- ----------------------------

CREATE TABLE T_RoleJoinPermission   (
  roleId   NVARCHAR2(255) NOT NULL ,
  permissionId   NVARCHAR2(255) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of RoleJoinPermission
-- ----------------------------
INSERT INTO T_RoleJoinPermission   VALUES ('5297eac423d845308c656cfc1808c320', 'eb5a66dc25974daba14d03e16d877a82');

-- ----------------------------
-- Table structure for Scenario
-- ----------------------------

CREATE TABLE T_Scenario   (
  scenario_id   NVARCHAR2(255) NOT NULL ,
  scenario_name   NVARCHAR2(255) NULL ,
  scenario_desc   NVARCHAR2(255) NULL ,
  scenario_extra   CLOB NULL ,
  scenario_col   CLOB NULL ,
  scenario_status   NUMBER NULL ,
  create_time   DATE NULL ,
  create_user   NVARCHAR2(255) NULL ,
  modified_time   DATE NULL ,
  modified_user   NVARCHAR2(255) NULL ,
  inspect_time   DATE NULL ,
  inspect_user   NVARCHAR2(255) NULL ,
  online_time   DATE NULL ,
  online_user   NVARCHAR2(255) NULL ,
  offline_time   DATE NULL ,
  offline_user   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of Scenario
-- ----------------------------

-- ----------------------------
-- Table structure for ScenarioCategory
-- ----------------------------

CREATE TABLE T_ScenarioCategory   (
  cate_id   NVARCHAR2(255) NOT NULL ,
  cate_name   NVARCHAR2(255) NULL ,
  cate_desc   NVARCHAR2(255) NULL ,
  cate_status   NUMBER NULL ,
  create_time   DATE NULL ,
  create_user   NVARCHAR2(255) NULL ,
  modified_time   DATE NULL ,
  modified_user   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of ScenarioCategory
-- ----------------------------

-- ----------------------------
-- Table structure for ScenarioCategoryDetail
-- ----------------------------

CREATE TABLE T_ScenarioCategoryDetail   (
  detail_id   NVARCHAR2(255) NOT NULL ,
  create_time   DATE NULL ,
  create_user   NVARCHAR2(255) NULL ,
  cate_id   NVARCHAR2(255) NULL ,
  scenario_id   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of ScenarioCategoryDetail
-- ----------------------------

-- ----------------------------
-- Table structure for ScenarioExecuteHistory
-- ----------------------------

CREATE TABLE T_ScenarioExecuteHistory   (
  execution_id   NVARCHAR2(255) NOT NULL ,
  execute_status   NUMBER NULL ,
  execute_progress   NUMBER NULL ,
  start_time   DATE NULL ,
  end_time   DATE NULL ,
  scenario_id   NVARCHAR2(255) NULL ,
  task_id   NVARCHAR2(255) NOT NULL ,
  create_time   DATE NULL ,
  task_name   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of ScenarioExecuteHistory
-- ----------------------------

-- ----------------------------
-- Table structure for ScenarioGraphdag
-- ----------------------------

CREATE TABLE T_ScenarioGraphdag   (
  graph_id   NVARCHAR2(255) NOT NULL ,
  graph_raw   CLOB NULL ,
  vertex_num   NUMBER NULL ,
  edge_num   NUMBER NULL ,
  create_time   DATE NULL ,
  create_user   NVARCHAR2(255) NULL ,
  modified_time   DATE NULL ,
  modified_user   NVARCHAR2(255) NULL ,
  scenario_id   NVARCHAR2(255) NULL ,
  version_id   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of ScenarioGraphdag
-- ----------------------------

-- ----------------------------
-- Table structure for ScenarioGraphedge
-- ----------------------------

CREATE TABLE T_ScenarioGraphedge   (
  edge_id   NVARCHAR2(255) NOT NULL ,
  from_key   NUMBER NULL ,
  to_key   NUMBER NULL ,
  create_time   DATE NULL ,
  create_user   NVARCHAR2(255) NULL ,
  graph_id   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of ScenarioGraphedge
-- ----------------------------

-- ----------------------------
-- Table structure for ScenarioGraphvertex
-- ----------------------------

CREATE TABLE T_ScenarioGraphvertex   (
  vertex_id   NVARCHAR2(255) NOT NULL ,
  key_id   NUMBER NULL ,
  is_top   NUMBER NULL ,
  is_terminal   NUMBER NULL ,
  create_time   DATE NULL ,
  create_user   NVARCHAR2(255) NULL ,
  task_id   NVARCHAR2(255) NULL ,
  graph_id   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of ScenarioGraphvertex
-- ----------------------------

-- ----------------------------
-- Table structure for ScenarioUservariable
-- ----------------------------

CREATE TABLE T_ScenarioUservariable   (
  variable_id   NVARCHAR2(255) NOT NULL ,
  variable_name   NVARCHAR2(255) NULL ,
  variable_desc   NVARCHAR2(255) NULL ,
  variable_expr   NVARCHAR2(255) NULL ,
  create_time   DATE NULL ,
  create_user   NVARCHAR2(255) NULL ,
  modified_time   DATE NULL ,
  modified_user   NVARCHAR2(255) NULL ,
  scenario_id   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of ScenarioUservariable
-- ----------------------------

-- ----------------------------
-- Table structure for ScenarioVariable
-- ----------------------------

CREATE TABLE T_ScenarioVariable   (
  variable_id   NVARCHAR2(255) NOT NULL ,
  variable_name   NVARCHAR2(255) NULL ,
  variable_desc   NVARCHAR2(255) NULL ,
  variable_expr   NVARCHAR2(255) NULL ,
  create_time   DATE NULL ,
  create_user   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of ScenarioVariable
-- ----------------------------

-- ----------------------------
-- Table structure for ScenarioVersion
-- ----------------------------

CREATE TABLE T_ScenarioVersion   (
  version_id   NVARCHAR2(255) NOT NULL ,
  version_label   NVARCHAR2(255) NULL ,
  create_time   DATE NULL ,
  create_user   NVARCHAR2(255) NULL ,
  scenario_id   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of ScenarioVersion
-- ----------------------------

-- ----------------------------
-- Table structure for Schedule
-- ----------------------------

CREATE TABLE T_Schedule   (
  scenario_id   NVARCHAR2(255) NOT NULL ,
  start_time   DATE NULL ,
  end_time   DATE NULL ,
  repeat_count   NUMBER NULL ,
  repeat_interval   NUMBER NULL ,
  cron_expression   NVARCHAR2(255) NULL ,
  trigger_type   NVARCHAR2(255) NULL ,
  job_group   NVARCHAR2(255) NULL ,
  user_id   NVARCHAR2(255) NULL ,
  ip_address   NVARCHAR2(255) NULL ,
  execution_frequency_unit   NVARCHAR2(255) NULL ,
  execution_day   NVARCHAR2(255) NULL ,
  execution_time   NVARCHAR2(255) NULL ,
  create_time   DATE NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of Schedule
-- ----------------------------

-- ----------------------------
-- Table structure for ScheduleHistory
-- ----------------------------

CREATE TABLE T_ScheduleHistory   (
  scenario_id   NVARCHAR2(255) NOT NULL ,
  start_time   DATE NULL ,
  schedule_status   NVARCHAR2(255) NULL ,
  schedulehistory_id   NVARCHAR2(255) NULL ,
  end_time   DATE NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of ScheduleHistory
-- ----------------------------

-- ----------------------------
-- Table structure for Scope
-- ----------------------------

CREATE TABLE T_Scope   (
  scopeId   NVARCHAR2(255) NOT NULL ,
  scopeName   NVARCHAR2(255) NULL ,
  scopeDesc   NVARCHAR2(255) NULL ,
  scopeType   NVARCHAR2(255) NULL ,
  scopeValue   NVARCHAR2(255) NULL ,
  createTime   DATE NULL ,
  createUser   NVARCHAR2(255) NULL ,
  modifiedTime   DATE NULL ,
  modifiedUser   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of Scope
-- ----------------------------
INSERT INTO T_Scope   VALUES ('c535d63fb2ec4f97b54aa37325a66d90', 'admin', 'administrator', 'url', '/**', TO_DATE('2016-10-08 12:10:24', 'YYYY-MM-DD HH24:MI:SS'), '9cee02c6a9f848eeba3119b8a828c66d', null, null);

-- ----------------------------
-- Table structure for Task
-- ----------------------------

CREATE TABLE T_Task   (
  task_id   NVARCHAR2(255) NOT NULL ,
  task_name   NVARCHAR2(255) NULL ,
  task_desc   NVARCHAR2(255) NULL ,
  task_type   NVARCHAR2(255) NULL ,
  task_status   NUMBER NULL ,
  create_time   DATE NULL ,
  create_user   NVARCHAR2(255) NULL ,
  relation_id   NVARCHAR2(255) NULL ,
  version_id   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of Task
-- ----------------------------

-- ----------------------------
-- Table structure for TaskConfig
-- ----------------------------

CREATE TABLE T_TaskConfig   (
  config_id   NVARCHAR2(255) NOT NULL ,
  param_value   CLOB NULL ,
  create_time   DATE NULL ,
  create_user   NVARCHAR2(255) NULL ,
  task_id   NVARCHAR2(255) NULL ,
  param_id   NVARCHAR2(255) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of TaskConfig
-- ----------------------------

-- ----------------------------
-- Table structure for User
-- ----------------------------

CREATE TABLE T_User   (
  userId   VARCHAR2(32 BYTE) NOT NULL ,
  userName   VARCHAR2(45 BYTE) NULL ,
  userPwd   VARCHAR2(45 BYTE) NULL ,
  userDesc   VARCHAR2(255 BYTE) NULL ,
  userStatus   NUMBER NULL ,
  createTime   DATE NULL ,
  modifiedTime   DATE NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of User
-- ----------------------------
INSERT INTO T_User   VALUES ('9cee02c6a9f848eeba3119b8a828c66d', 'admin', 'Q2hhbmdlbWVfMTIz', 'administrator', '0', TO_DATE('2016-09-20 15:15:03', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2016-10-28 16:37:30', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO T_User   VALUES ('dd91ea6a2a2e4d7ca6973feda9484669', 'wsy', 'NTY4NDY4', '123', '0', TO_DATE('2016-10-28 14:30:29', 'YYYY-MM-DD HH24:MI:SS'), null);

-- ----------------------------
-- Table structure for UserGroup
-- ----------------------------

CREATE TABLE T_UserGroup   (
  groupId   VARCHAR2(32 BYTE) NOT NULL ,
  name   VARCHAR2(45 BYTE) NULL ,
  description   VARCHAR2(255 BYTE) NULL ,
  createTime   DATE NULL ,
  createUser   VARCHAR2(32 BYTE) NULL ,
  modifiedTime   DATE NULL ,
  modifiedUser   VARCHAR2(32 BYTE) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of UserGroup
-- ----------------------------

-- ----------------------------
-- Table structure for UserGroupJoinUser
-- ----------------------------

CREATE TABLE T_UserGroupJoinUser   (
  groupId   NVARCHAR2(255) NOT NULL ,
  userId   NVARCHAR2(255) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of UserGroupJoinUser
-- ----------------------------

-- ----------------------------
-- Table structure for UserJoinRole
-- ----------------------------

CREATE TABLE T_UserJoinRole   (
  userId   NVARCHAR2(255) NOT NULL ,
  roleId   NVARCHAR2(255) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of UserJoinRole
-- ----------------------------
INSERT INTO T_UserJoinRole   VALUES ('2209a47118f04a3bad37b6192c92d4f7', '5297eac423d845308c656cfc1808c320');
INSERT INTO T_UserJoinRole   VALUES ('60d29edaa1174031b837c72ad146e93c', '5297eac423d845308c656cfc1808c320');
INSERT INTO T_UserJoinRole   VALUES ('87fc148b44764466a238ac6f9c5d6a10', '5297eac423d845308c656cfc1808c320');
INSERT INTO T_UserJoinRole   VALUES ('972024f17c2c44a59da30ae7d29fd96c', '5297eac423d845308c656cfc1808c320');
INSERT INTO T_UserJoinRole   VALUES ('9cee02c6a9f848eeba3119b8a828c66d', '5297eac423d845308c656cfc1808c320');
INSERT INTO T_UserJoinRole   VALUES ('a30d9dc1f28540219d676ba2919e8b2e', '1a9f8265c51e4be395b18a4f0cb6c782');
INSERT INTO T_UserJoinRole   VALUES ('a35538df5f2a4b94a8a90918982cdb6b', '4a8ee65731324e5fbb71e1be0c0c60cc');
INSERT INTO T_UserJoinRole   VALUES ('fb666fb759df4efe85019370d487c9fe', '1a9f8265c51e4be395b18a4f0cb6c782');

-- ----------------------------
-- Indexes structure for table Component
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Component
-- ----------------------------
ALTER TABLE T_Component   ADD PRIMARY KEY (  component_id  );

-- ----------------------------
-- Indexes structure for table ComponentConfig
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ComponentConfig
-- ----------------------------
ALTER TABLE T_ComponentConfig   ADD PRIMARY KEY (  param_id  );

-- ----------------------------
-- Indexes structure for table Permission
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Permission
-- ----------------------------
ALTER TABLE T_Permission   ADD PRIMARY KEY (  permissionId  );

-- ----------------------------
-- Indexes structure for table PermissionJoinScope
-- ----------------------------

-- ----------------------------
-- Checks structure for table PermissionJoinScope
-- ----------------------------
ALTER TABLE T_PermissionJoinScope   ADD CHECK (  permissionId   IS NOT NULL);
ALTER TABLE T_PermissionJoinScope   ADD CHECK (  scopeId   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table PermissionJoinScope
-- ----------------------------
ALTER TABLE T_PermissionJoinScope   ADD PRIMARY KEY (  permissionId  ,   scopeId  );

-- ----------------------------
-- Indexes structure for table Role
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Role
-- ----------------------------
ALTER TABLE T_Role   ADD PRIMARY KEY (  roleId  );

-- ----------------------------
-- Indexes structure for table RoleJoinPermission
-- ----------------------------

-- ----------------------------
-- Checks structure for table RoleJoinPermission
-- ----------------------------
ALTER TABLE T_RoleJoinPermission   ADD CHECK (  permissionId   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table RoleJoinPermission
-- ----------------------------
ALTER TABLE T_RoleJoinPermission   ADD PRIMARY KEY (  roleId  ,   permissionId  );

-- ----------------------------
-- Indexes structure for table Scenario
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Scenario
-- ----------------------------
ALTER TABLE T_Scenario   ADD PRIMARY KEY (  scenario_id  );

-- ----------------------------
-- Indexes structure for table ScenarioCategory
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioCategory
-- ----------------------------
ALTER TABLE T_ScenarioCategory   ADD PRIMARY KEY (  cate_id  );

-- ----------------------------
-- Indexes structure for table ScenarioCategoryDetail
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioCategoryDetail
-- ----------------------------
ALTER TABLE T_ScenarioCategoryDetail   ADD PRIMARY KEY (  detail_id  );

-- ----------------------------
-- Indexes structure for table ScenarioExecuteHistory
-- ----------------------------

-- ----------------------------
-- Checks structure for table ScenarioExecuteHistory
-- ----------------------------
ALTER TABLE T_ScenarioExecuteHistory   ADD CHECK (  task_id   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table ScenarioExecuteHistory
-- ----------------------------
ALTER TABLE T_ScenarioExecuteHistory   ADD PRIMARY KEY (  execution_id  ,   task_id  );

-- ----------------------------
-- Indexes structure for table ScenarioGraphdag
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioGraphdag
-- ----------------------------
ALTER TABLE T_ScenarioGraphdag   ADD PRIMARY KEY (  graph_id  );

-- ----------------------------
-- Indexes structure for table ScenarioGraphedge
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioGraphedge
-- ----------------------------
ALTER TABLE T_ScenarioGraphedge   ADD PRIMARY KEY (  edge_id  );

-- ----------------------------
-- Indexes structure for table ScenarioGraphvertex
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioGraphvertex
-- ----------------------------
ALTER TABLE T_ScenarioGraphvertex   ADD PRIMARY KEY (  vertex_id  );

-- ----------------------------
-- Indexes structure for table ScenarioUservariable
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioUservariable
-- ----------------------------
ALTER TABLE T_ScenarioUservariable   ADD PRIMARY KEY (  variable_id  );

-- ----------------------------
-- Indexes structure for table ScenarioVariable
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioVariable
-- ----------------------------
ALTER TABLE T_ScenarioVariable   ADD PRIMARY KEY (  variable_id  );

-- ----------------------------
-- Indexes structure for table ScenarioVersion
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioVersion
-- ----------------------------
ALTER TABLE T_ScenarioVersion   ADD PRIMARY KEY (  version_id  );

-- ----------------------------
-- Indexes structure for table Schedule
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Schedule
-- ----------------------------
ALTER TABLE T_Schedule   ADD PRIMARY KEY (  scenario_id  );

-- ----------------------------
-- Indexes structure for table ScheduleHistory
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScheduleHistory
-- ----------------------------
ALTER TABLE T_ScheduleHistory   ADD PRIMARY KEY (  scenario_id  );

-- ----------------------------
-- Indexes structure for table Scope
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Scope
-- ----------------------------
ALTER TABLE T_Scope   ADD PRIMARY KEY (  scopeId  );

-- ----------------------------
-- Indexes structure for table Task
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Task
-- ----------------------------
ALTER TABLE T_Task   ADD PRIMARY KEY (  task_id  );

-- ----------------------------
-- Indexes structure for table TaskConfig
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table TaskConfig
-- ----------------------------
ALTER TABLE T_TaskConfig   ADD PRIMARY KEY (  config_id  );

-- ----------------------------
-- Indexes structure for table User
-- ----------------------------

-- ----------------------------
-- Checks structure for table User
-- ----------------------------
ALTER TABLE T_User   ADD CHECK (  userId   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table User
-- ----------------------------
ALTER TABLE T_User   ADD PRIMARY KEY (  userId  );

-- ----------------------------
-- Indexes structure for table UserGroup
-- ----------------------------

-- ----------------------------
-- Checks structure for table UserGroup
-- ----------------------------
ALTER TABLE T_UserGroup   ADD CHECK (  groupId   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table UserGroup
-- ----------------------------
ALTER TABLE T_UserGroup   ADD PRIMARY KEY (  groupId  );

-- ----------------------------
-- Indexes structure for table UserGroupJoinUser
-- ----------------------------

-- ----------------------------
-- Checks structure for table UserGroupJoinUser
-- ----------------------------
ALTER TABLE T_UserGroupJoinUser   ADD CHECK (  userId   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table UserGroupJoinUser
-- ----------------------------
ALTER TABLE T_UserGroupJoinUser   ADD PRIMARY KEY (  groupId  ,   userId  );

-- ----------------------------
-- Indexes structure for table UserJoinRole
-- ----------------------------

-- ----------------------------
-- Checks structure for table UserJoinRole
-- ----------------------------
ALTER TABLE T_UserJoinRole   ADD CHECK (  roleId   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table UserJoinRole
-- ----------------------------
ALTER TABLE T_UserJoinRole   ADD PRIMARY KEY (  userId  ,   roleId  );
