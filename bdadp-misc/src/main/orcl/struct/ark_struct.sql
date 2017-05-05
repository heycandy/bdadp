

CREATE TABLE Component   (
  componenid   NVARCHAR2(255) NOT NULL ,
  componenname   NVARCHAR2(255) NULL ,
  componendesc   NVARCHAR2(255) NULL ,
  componenstatus   NUMBER NULL ,
  componenpid   NVARCHAR2(255) NULL ,
  componenbase   NUMBER NULL ,
  componentype   NVARCHAR2(255) NULL 
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

CREATE TABLE ComponentConfig   (
  param_id   NVARCHAR2(255) NOT NULL ,
  param_name   NVARCHAR2(255) NULL ,
  param_desc   NVARCHAR2(255) NULL ,
  param_type   NVARCHAR2(255) NULL ,
  defaulvalue   NVARCHAR2(255) NULL ,
  defauloptions   NVARCHAR2(255) NULL ,
  nullable   NUMBER NULL ,
  order_id   NUMBER NULL ,
  componenid   NVARCHAR2(255) NULL 
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

CREATE TABLE Permission   (
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
-- Table structure for PermissionJoinScope
-- ----------------------------

CREATE TABLE PermissionJoinScope   (
  permissionId   NVARCHAR2(255) NOT NULL ,
  scopeId   NVARCHAR2(255) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Table structure for Role
-- ----------------------------

CREATE TABLE Role   (
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
-- Table structure for RoleJoinPermission
-- ----------------------------

CREATE TABLE RoleJoinPermission   (
  roleId   NVARCHAR2(255) NOT NULL ,
  permissionId   NVARCHAR2(255) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Table structure for Scenario
-- ----------------------------

CREATE TABLE Scenario   (
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
  inspectime   DATE NULL ,
  inspecuser   NVARCHAR2(255) NULL ,
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

CREATE TABLE ScenarioCategory   (
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

CREATE TABLE ScenarioCategoryDetail   (
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

CREATE TABLE ScenarioExecuteHistory   (
  execution_id   NVARCHAR2(255) NOT NULL ,
  execute_status   NUMBER NULL ,
  execute_progress   NUMBER NULL ,
  startime   DATE NULL ,
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

CREATE TABLE ScenarioGraphdag   (
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

CREATE TABLE ScenarioGraphedge   (
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

CREATE TABLE ScenarioGraphvertex   (
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

CREATE TABLE ScenarioUservariable   (
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

CREATE TABLE ScenarioVariable   (
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

CREATE TABLE ScenarioVersion   (
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

CREATE TABLE Schedule   (
  scenario_id   NVARCHAR2(255) NOT NULL ,
  startime   DATE NULL ,
  end_time   DATE NULL ,
  repeacount   NUMBER NULL ,
  repeainterval   NUMBER NULL ,
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

CREATE TABLE ScheduleHistory   (
  scenario_id   NVARCHAR2(255) NOT NULL ,
  startime   DATE NULL ,
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

CREATE TABLE Scope   (
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
-- Table structure for Task
-- ----------------------------

CREATE TABLE Task   (
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

CREATE TABLE TaskConfig   (
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
-- Table structure for Users
-- ----------------------------

CREATE TABLE Users   (
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
-- Table structure for UserGroup
-- ----------------------------

CREATE TABLE UserGroup   (
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

CREATE TABLE UserGroupJoinUser   (
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

CREATE TABLE UserJoinRole   (
  userId   NVARCHAR2(255) NOT NULL ,
  roleId   NVARCHAR2(255) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Indexes structure for table Component
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Component
-- ----------------------------
ALTER TABLE Component   ADD PRIMARY KEY (  componenid  );

-- ----------------------------
-- Indexes structure for table ComponentConfig
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ComponentConfig
-- ----------------------------
ALTER TABLE ComponentConfig   ADD PRIMARY KEY (  param_id  );

-- ----------------------------
-- Indexes structure for table Permission
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Permission
-- ----------------------------
ALTER TABLE Permission   ADD PRIMARY KEY (  permissionId  );

-- ----------------------------
-- Indexes structure for table PermissionJoinScope
-- ----------------------------

-- ----------------------------
-- Checks structure for table PermissionJoinScope
-- ----------------------------
ALTER TABLE PermissionJoinScope   ADD CHECK (  permissionId   IS NOT NULL);
ALTER TABLE PermissionJoinScope   ADD CHECK (  scopeId   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table PermissionJoinScope
-- ----------------------------
ALTER TABLE PermissionJoinScope   ADD PRIMARY KEY (  permissionId  ,   scopeId  );

-- ----------------------------
-- Indexes structure for table Role
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Role
-- ----------------------------
ALTER TABLE Role   ADD PRIMARY KEY (  roleId  );

-- ----------------------------
-- Indexes structure for table RoleJoinPermission
-- ----------------------------

-- ----------------------------
-- Checks structure for table RoleJoinPermission
-- ----------------------------
ALTER TABLE RoleJoinPermission   ADD CHECK (  permissionId   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table RoleJoinPermission
-- ----------------------------
ALTER TABLE RoleJoinPermission   ADD PRIMARY KEY (  roleId  ,   permissionId  );

-- ----------------------------
-- Indexes structure for table Scenario
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Scenario
-- ----------------------------
ALTER TABLE Scenario   ADD PRIMARY KEY (  scenario_id  );

-- ----------------------------
-- Indexes structure for table ScenarioCategory
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioCategory
-- ----------------------------
ALTER TABLE ScenarioCategory   ADD PRIMARY KEY (  cate_id  );

-- ----------------------------
-- Indexes structure for table ScenarioCategoryDetail
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioCategoryDetail
-- ----------------------------
ALTER TABLE ScenarioCategoryDetail   ADD PRIMARY KEY (  detail_id  );

-- ----------------------------
-- Indexes structure for table ScenarioExecuteHistory
-- ----------------------------

-- ----------------------------
-- Checks structure for table ScenarioExecuteHistory
-- ----------------------------
ALTER TABLE ScenarioExecuteHistory   ADD CHECK (  task_id   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table ScenarioExecuteHistory
-- ----------------------------
ALTER TABLE ScenarioExecuteHistory   ADD PRIMARY KEY (  execution_id  ,   task_id  );

-- ----------------------------
-- Indexes structure for table ScenarioGraphdag
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioGraphdag
-- ----------------------------
ALTER TABLE ScenarioGraphdag   ADD PRIMARY KEY (  graph_id  );

-- ----------------------------
-- Indexes structure for table ScenarioGraphedge
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioGraphedge
-- ----------------------------
ALTER TABLE ScenarioGraphedge   ADD PRIMARY KEY (  edge_id  );

-- ----------------------------
-- Indexes structure for table ScenarioGraphvertex
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioGraphvertex
-- ----------------------------
ALTER TABLE ScenarioGraphvertex   ADD PRIMARY KEY (  vertex_id  );

-- ----------------------------
-- Indexes structure for table ScenarioUservariable
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioUservariable
-- ----------------------------
ALTER TABLE ScenarioUservariable   ADD PRIMARY KEY (  variable_id  );

-- ----------------------------
-- Indexes structure for table ScenarioVariable
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioVariable
-- ----------------------------
ALTER TABLE ScenarioVariable   ADD PRIMARY KEY (  variable_id  );

-- ----------------------------
-- Indexes structure for table ScenarioVersion
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScenarioVersion
-- ----------------------------
ALTER TABLE ScenarioVersion   ADD PRIMARY KEY (  version_id  );

-- ----------------------------
-- Indexes structure for table Schedule
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Schedule
-- ----------------------------
ALTER TABLE Schedule   ADD PRIMARY KEY (  scenario_id  );

-- ----------------------------
-- Indexes structure for table ScheduleHistory
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table ScheduleHistory
-- ----------------------------
ALTER TABLE ScheduleHistory   ADD PRIMARY KEY (  scenario_id  );

-- ----------------------------
-- Indexes structure for table Scope
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Scope
-- ----------------------------
ALTER TABLE Scope   ADD PRIMARY KEY (  scopeId  );

-- ----------------------------
-- Indexes structure for table Task
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Task
-- ----------------------------
ALTER TABLE Task   ADD PRIMARY KEY (  task_id  );

-- ----------------------------
-- Indexes structure for table TaskConfig
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table TaskConfig
-- ----------------------------
ALTER TABLE TaskConfig   ADD PRIMARY KEY (  config_id  );

-- ----------------------------
-- Indexes structure for table Users
-- ----------------------------

-- ----------------------------
-- Checks structure for table Users
-- ----------------------------
ALTER TABLE Users   ADD CHECK (  userId   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table Users
-- ----------------------------
ALTER TABLE Users   ADD PRIMARY KEY (  userId  );

-- ----------------------------
-- Indexes structure for table UserGroup
-- ----------------------------

-- ----------------------------
-- Checks structure for table UserGroup
-- ----------------------------
ALTER TABLE UserGroup   ADD CHECK (  groupId   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table UserGroup
-- ----------------------------
ALTER TABLE UserGroup   ADD PRIMARY KEY (  groupId  );

-- ----------------------------
-- Indexes structure for table UserGroupJoinUser
-- ----------------------------

-- ----------------------------
-- Checks structure for table UserGroupJoinUser
-- ----------------------------
ALTER TABLE UserGroupJoinUser   ADD CHECK (  userId   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table UserGroupJoinUser
-- ----------------------------
ALTER TABLE UserGroupJoinUser   ADD PRIMARY KEY (  groupId  ,   userId  );

-- ----------------------------
-- Indexes structure for table UserJoinRole
-- ----------------------------

-- ----------------------------
-- Checks structure for table UserJoinRole
-- ----------------------------
ALTER TABLE UserJoinRole   ADD CHECK (  roleId   IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table UserJoinRole
-- ----------------------------
ALTER TABLE UserJoinRole   ADD PRIMARY KEY (  userId  ,   roleId  );
