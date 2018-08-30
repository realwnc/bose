/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50527
Source Host           : 127.0.0.1:3306
Source Database       : manutd

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2015-12-07 20:44:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_sys_organization
-- ----------------------------
DROP TABLE IF EXISTS tb_sys_organization;
CREATE TABLE tb_sys_organization (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(64) NOT NULL,
  address varchar(255) DEFAULT NULL,
  codex varchar(64) NOT NULL,
  icon varchar(255) DEFAULT NULL,
  pid int(11) NOT NULL DEFAULT 0,
  seq tinyint(2) NOT NULL DEFAULT '0',
  insert_time timestamp NOT NULL DEFAULT now(),
  update_time timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='组织机构';

-- ----------------------------
-- Records of tb_sys_organization
-- ----------------------------
INSERT INTO tb_sys_organization VALUES ('1', '总经办', '', '01', 'icon-company', 0, '0', '2014-02-19 01:00:00', '2014-02-19 01:00:00');
INSERT INTO tb_sys_organization VALUES ('2', '技术部', '', '02', 'icon-company', 0, '1', '2015-10-01 13:10:42', '2014-02-19 01:00:00');
INSERT INTO tb_sys_organization VALUES ('3', '产品部', '', '03', 'icon-company', 0, '2', '2015-12-06 12:15:30', '2014-02-19 01:00:00');
INSERT INTO tb_sys_organization VALUES ('4', '测试组', '', '04', 'icon-folder', '3', '0', '2015-12-06 13:12:18', '2014-02-19 01:00:00');

-- ----------------------------
-- Table structure for tb_sys_resource
-- ----------------------------
DROP TABLE IF EXISTS tb_sys_resource;
CREATE TABLE tb_sys_resource (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(64) NOT NULL,
  url varchar(255) DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  icon varchar(255) DEFAULT NULL,
  pid int(11) DEFAULT NULL,
  seq tinyint(2) NOT NULL DEFAULT '0',
  statusx tinyint(2) NOT NULL DEFAULT '0',
  resource_type tinyint(2) NOT NULL DEFAULT '0',
  insert_time timestamp NOT NULL DEFAULT now(),
  update_time timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='资源';

-- ----------------------------
-- Records of tb_sys_resource
-- ----------------------------
INSERT INTO `tb_sys_resource` VALUES ('1', '系统管理', '/sys/root.do', '#', 'icon-company', '0', '0', '0', '0', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('11', '资源管理', '/resource/manager.do', '资源管理', 'icon-folder', '1', '1', '0', '0', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('12', '角色管理', '/role/manager.do', '角色管理', 'icon-folder', '1', '2', '0', '0', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('13', '用户管理', '/user/manager.do', '用户管理', 'icon-folder', '1', '3', '0', '0', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('14', '部门管理', '/organization/manager.do', '部门管理', 'icon-folder', '1', '4', '0', '0', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('111', '列表', '/resource/findTreeGrid.do', '资源列表', 'icon-list', '11', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('112', '添加', '/resource/add.do', '资源添加', 'icon-add', '11', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('113', '编辑', '/resource/edit.do', '资源编辑', 'icon-edit', '11', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('114', '删除', '/resource/delete.do', '资源删除', 'icon-del', '11', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('121', '列表', '/role/findDataGrid.do', '角色列表', 'icon-list', '12', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('122', '添加', '/role/add.do', '角色添加', 'icon-add', '12', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('123', '编辑', '/role/edit.do', '角色编辑', 'icon-edit', '12', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('124', '删除', '/role/delete.do', '角色删除', 'icon-del', '12', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('125', '授权', '/role/grant.do', '角色授权', 'icon-ok', '12', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('131', '列表', '/user/findDataGrid.do', '用户列表', 'icon-list', '13', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('132', '添加', '/user/add.do', '用户添加', 'icon-add', '13', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('133', '编辑', '/user/edit.do', '用户编辑', 'icon-edit', '13', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('134', '删除', '/user/delete.do', '用户删除', 'icon-del', '13', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('135', '修改密码', '/user/editPwdPage.do', '修改密码', 'icon-edit', '13', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('141', '列表', '/organization/treeGrid.do', '用户列表', 'icon-list', '14', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('142', '添加', '/organization/add.do', '部门添加', 'icon-add', '14', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('143', '编辑', '/organization/edit.do', '部门编辑', 'icon-edit', '14', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('144', '删除', '/organization/delete.do', '部门删除', 'icon-del', '14', '0', '0', '1', '2017-11-11 11:11:11', '2017-11-11 11:11:11');

INSERT INTO `tb_sys_resource` VALUES ('2', '日志管理', '/sysLog/root.do', '#', 'icon-company', '0', '2', '0', '0', '2017-11-11 11:11:11', '2017-11-11 11:11:11');
INSERT INTO `tb_sys_resource` VALUES ('21', '操作日志', '/sys/sysLog/manager.do', null, 'icon-company', '2', '1', '0', '0', '2017-11-11 11:11:11', '2017-11-11 11:11:11');


-- ----------------------------
-- Table structure for tb_sys_role
-- ----------------------------
DROP TABLE IF EXISTS tb_sys_role;
CREATE TABLE tb_sys_role (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(64) NOT NULL,
  seq tinyint(2) NOT NULL DEFAULT '0',
  description varchar(255) DEFAULT NULL,
  statusx tinyint(2) NOT NULL DEFAULT '0',
  insert_time timestamp NOT NULL DEFAULT now(),
  update_time timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of tb_sys_role
-- ----------------------------
INSERT INTO tb_sys_role VALUES ('1', '超级管理员', '0', '超级管理员', '0', now(), now());

-- ----------------------------
-- Table structure for tb_sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS tb_sys_role_resource;
CREATE TABLE tb_sys_role_resource (
  id int(11) NOT NULL AUTO_INCREMENT,
  role_id int(11) NOT NULL,
  resource_id int(11) NOT NULL,
  insert_time timestamp NOT NULL DEFAULT now(),
  update_time timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='角色资源';

-- ----------------------------
-- Records of tb_sys_role_resource
-- ----------------------------


INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (1, 1, 1);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (2, 1, 11);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (3, 1, 111);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (4, 1, 112);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (5, 1, 113);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (6, 1, 114);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (7, 1, 12);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (8, 1, 121);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (9, 1, 122);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (10, 1, 123);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (11, 1, 124);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (12, 1, 125);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (13, 1, 13);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (14, 1, 131);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (15, 1, 132);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (16, 1, 133);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (17, 1, 134);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (18, 1, 135);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (19, 1, 14);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (20, 1, 141);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (21, 1, 142);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (22, 1, 143);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (23, 1, 144);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (24, 1, 2);
INSERT INTO tb_sys_role_resource (id, role_id, resource_id) VALUES (25, 1, 21);



-- ----------------------------
-- Table structure for tb_sys_user
-- ----------------------------
DROP TABLE IF EXISTS tb_sys_user;
CREATE TABLE tb_sys_user (
  id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(64) NOT NULL,
  name varchar(64) NOT NULL,
  password varchar(128) NOT NULL,
  phone varchar(20) DEFAULT NULL,
  sex tinyint(2) NOT NULL DEFAULT '0',
  age tinyint(2) DEFAULT '0',
  user_type tinyint(2) NOT NULL DEFAULT '0' COMMENT '0:管理员,1:用户',
  statusx tinyint(2) NOT NULL DEFAULT '0',
  organization_id int(11) NOT NULL DEFAULT '0',
  insert_time timestamp NOT NULL DEFAULT now(),
  update_time timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Records of tb_sys_user
-- default password md5(123456)=e10adc3949ba59abbe56e057f20f883e
-- default password md5salt(admin123456)=a66abb5684c45962d887564f08346e8d
-- ----------------------------
INSERT INTO tb_sys_user VALUES ('1', 'admin', 'admin', 'a66abb5684c45962d887564f08346e8d', '18707173376', '0', '25', '0', '0', '1', '2015-12-06 13:14:05', '2015-12-06 13:14:05');

-- ----------------------------
-- Table structure for tb_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS tb_sys_user_role;
CREATE TABLE tb_sys_user_role (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  role_id int(11) NOT NULL,
  insert_time timestamp NOT NULL DEFAULT now(),
  update_time timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='用户角色';

-- ----------------------------
-- Records of tb_sys_user_role
-- ----------------------------

INSERT INTO tb_sys_user_role(id, user_id, role_id, insert_time, update_time) VALUES ('1', '1', '1', '2017-11-08 16:55:47', '2017-11-08 16:55:47');


-- ----------------------------
-- Table structure for tb_sys_syslog
-- ----------------------------
DROP TABLE IF EXISTS tb_sys_syslog;
CREATE TABLE tb_sys_syslog (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  username varchar(64) DEFAULT NULL,
  role_name varchar(64) DEFAULT NULL,
  opt_content varchar(1024) DEFAULT NULL,
  client_ip varchar(64) DEFAULT NULL,
  insert_time timestamp NOT NULL DEFAULT now(),
  update_time timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_sys_syslog
-- ----------------------------
INSERT INTO tb_sys_syslog VALUES ('1', 'admin', 'admin', '[类名]:com.mids.controller.LoginController,[方法]:login,[参数]:null', '127.0.0.1', '2015-10-30 17:58:44', '2015-10-30 17:58:44');
