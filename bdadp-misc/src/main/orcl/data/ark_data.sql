-- ----------------------------
-- Records of Permission
-- ----------------------------
INSERT INTO Permission   VALUES ('eb5a66dc25974daba14d03e16d877a82', 'admin', 'administrator', TO_DATE('2016-10-08 12:10:23', 'YYYY-MM-DD HH24:MI:SS'), '9cee02c6a9f848eeba3119b8a828c66d', null, null);

-- ----------------------------
-- Records of PermissionJoinScope
-- ----------------------------
INSERT INTO PermissionJoinScope   VALUES ('eb5a66dc25974daba14d03e16d877a82', 'c535d63fb2ec4f97b54aa37325a66d90');

-- ----------------------------
-- Records of Role
-- ----------------------------
INSERT INTO Role   VALUES ('1a9f8265c51e4be395b18a4f0cb6c782', 'dev', 'develop', TO_DATE('2016-10-08 12:10:24', 'YYYY-MM-DD HH24:MI:SS'), '9cee02c6a9f848eeba3119b8a828c66d', null, null);
INSERT INTO Role   VALUES ('4a8ee65731324e5fbb71e1be0c0c60cc', 'oper', 'operation', TO_DATE('2016-10-08 12:10:24', 'YYYY-MM-DD HH24:MI:SS'), '9cee02c6a9f848eeba3119b8a828c66d', null, null);
INSERT INTO Role   VALUES ('5297eac423d845308c656cfc1808c320', 'admin', 'administrator', TO_DATE('2016-10-08 12:10:24', 'YYYY-MM-DD HH24:MI:SS'), '9cee02c6a9f848eeba3119b8a828c66d', null, null);

-- ----------------------------
-- Records of RoleJoinPermission
-- ----------------------------
INSERT INTO RoleJoinPermission   VALUES ('5297eac423d845308c656cfc1808c320', 'eb5a66dc25974daba14d03e16d877a82');

-- ----------------------------
-- Records of Scope
-- ----------------------------
INSERT INTO Scope   VALUES ('c535d63fb2ec4f97b54aa37325a66d90', 'admin', 'administrator', 'url', '/**', TO_DATE('2016-10-08 12:10:24', 'YYYY-MM-DD HH24:MI:SS'), '9cee02c6a9f848eeba3119b8a828c66d', null, null);


-- ----------------------------
-- Records of User
-- ----------------------------
INSERT INTO Users   VALUES ('9cee02c6a9f848eeba3119b8a828c66d', 'admin', 'Q2hhbmdlbWVfMTIz', 'administrator', '0', TO_DATE('2016-09-20 15:15:03', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2016-10-28 16:37:30', 'YYYY-MM-DD HH24:MI:SS'));


-- ----------------------------
-- Records of UserJoinRole
-- ----------------------------
INSERT INTO UserJoinRole   VALUES ('2209a47118f04a3bad37b6192c92d4f7', '5297eac423d845308c656cfc1808c320');
INSERT INTO UserJoinRole   VALUES ('60d29edaa1174031b837c72ad146e93c', '5297eac423d845308c656cfc1808c320');
INSERT INTO UserJoinRole   VALUES ('87fc148b44764466a238ac6f9c5d6a10', '5297eac423d845308c656cfc1808c320');
INSERT INTO UserJoinRole   VALUES ('972024f17c2c44a59da30ae7d29fd96c', '5297eac423d845308c656cfc1808c320');
INSERT INTO UserJoinRole   VALUES ('9cee02c6a9f848eeba3119b8a828c66d', '5297eac423d845308c656cfc1808c320');
INSERT INTO UserJoinRole   VALUES ('a30d9dc1f28540219d676ba2919e8b2e', '1a9f8265c51e4be395b18a4f0cb6c782');
INSERT INTO UserJoinRole   VALUES ('a35538df5f2a4b94a8a90918982cdb6b', '4a8ee65731324e5fbb71e1be0c0c60cc');
INSERT INTO UserJoinRole   VALUES ('fb666fb759df4efe85019370d487c9fe', '1a9f8265c51e4be395b18a4f0cb6c782');

