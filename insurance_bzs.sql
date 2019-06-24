/*
Navicat MySQL Data Transfer

Source Server         : bzs
Source Server Version : 50725
Source Host           : 192.168.1.100:3306
Source Database       : insurance_bzs

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-06-18 17:44:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account_info
-- ----------------------------
DROP TABLE IF EXISTS `account_info`;
CREATE TABLE `account_info` (
  `account_id` varchar(64) NOT NULL COMMENT 'id',
  `role_id` varchar(64) DEFAULT NULL,
  `role_name` varchar(20) DEFAULT NULL,
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父级id',
  `ancestor_id` varchar(64) DEFAULT NULL COMMENT '祖id',
  `account_state` int(32) DEFAULT '0' COMMENT '账号状态 0启用1禁用2待审核3关闭',
  `login_name` varchar(32) DEFAULT NULL COMMENT '账号登陆名',
  `login_pwd` varchar(64) DEFAULT NULL COMMENT '账号密码',
  `user_name` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `area_code` varchar(32) DEFAULT NULL COMMENT '账号所属区域',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `wechat` varchar(32) DEFAULT NULL COMMENT '微信号',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱号',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '是否删除0默认1删除',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `login_time` datetime DEFAULT NULL COMMENT '乐观锁',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `inviteCode` int(20) DEFAULT NULL COMMENT '邀请码',
  `superiorInviteCode` int(20) DEFAULT NULL COMMENT '上级邀请码',
  `superior` varchar(255) DEFAULT NULL COMMENT '上级业务员',
  `association_level` varchar(255) DEFAULT NULL COMMENT '等级关系',
  `invite_code_level` int(11) DEFAULT NULL COMMENT '关联等级，从1开始',
  `balance_total` decimal(10,2) DEFAULT '0.00' COMMENT '余额',
  `commission_total` decimal(10,2) DEFAULT '0.00' COMMENT '佣金累计总额',
  `draw_percentage_total` decimal(10,2) DEFAULT '0.00' COMMENT '提成累计总额',
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账号列表 ';

-- ----------------------------
-- Records of account_info
-- ----------------------------
INSERT INTO `account_info` VALUES ('1', '1', '系统管理员', '', null, '0', 'admin', 'b95dedfd5a3c2882e9c4d745a3a29349', 'admin', null, null, null, null, null, '0', null, null, '2019-06-17 14:17:02', '2019-04-15 10:56:15', '2019-06-17 14:14:54', '55412', null, null, '55412', '1', '22799.00', '3333.00', '5573.86');
INSERT INTO `account_info` VALUES ('2', null, null, '20190617143519301790', null, '0', null, null, '13', null, null, null, null, null, '0', null, null, null, '2019-06-18 11:19:57', '2019-06-18 11:20:04', null, null, null, null, null, '0.00', '0.00', '0.00');
INSERT INTO `account_info` VALUES ('20190617132506123299', '3', null, '1', null, '0', '15518727891', '91ae0331ca778e58a9d88312fc6aa996', 'asdasd', null, '15518727891', null, null, null, '0', null, null, null, '2019-06-17 13:25:12', '2019-06-17 14:28:48', '285149', null, null, null, null, '0.00', '0.00', '0.00');
INSERT INTO `account_info` VALUES ('20190617143519301790', '3', null, '1', null, '0', '123123', 'cf49ee242288dcc02f888a3633c44a71', '123', null, '12312312312', null, null, null, '0', null, null, '2019-06-17 15:36:07', '2019-06-17 14:35:25', '2019-06-17 14:58:59', '611602', null, null, null, null, '0.00', '0.00', '0.00');

-- ----------------------------
-- Table structure for account_role_info
-- ----------------------------
DROP TABLE IF EXISTS `account_role_info`;
CREATE TABLE `account_role_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `account_id` varchar(64) DEFAULT NULL COMMENT '与account_info表关联',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account_role_info
-- ----------------------------
INSERT INTO `account_role_info` VALUES ('8', '1', '1', '2019-06-16 14:11:37', null, null, null);
INSERT INTO `account_role_info` VALUES ('9', '4', '2', '2019-06-16 14:11:37', '2019-06-18 16:45:15', null, null);
INSERT INTO `account_role_info` VALUES ('10', '4', '20190617143519301790', '2019-06-16 14:11:37', '2019-06-18 16:45:39', null, null);

-- ----------------------------
-- Table structure for action_column_info
-- ----------------------------
DROP TABLE IF EXISTS `action_column_info`;
CREATE TABLE `action_column_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '权限名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of action_column_info
-- ----------------------------

-- ----------------------------
-- Table structure for action_info
-- ----------------------------
DROP TABLE IF EXISTS `action_info`;
CREATE TABLE `action_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '权限名称',
  `action` varchar(32) DEFAULT NULL COMMENT '用于区分权限,如果使用id,id可能会改变',
  `column_id` int(11) DEFAULT NULL COMMENT '权限分栏表,与action_column_info表关联',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of action_info
-- ----------------------------

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `login_name` varchar(50) DEFAULT NULL,
  `login_pwd` varchar(100) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `sex` char(1) DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `descripition` varchar(100) DEFAULT '' COMMENT '描述',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `last_login_time` datetime DEFAULT NULL,
  `status` char(1) DEFAULT '1' COMMENT '状态 0锁定 1有效',
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', '1', 'admin', 'b95dedfd5a3c2882e9c4d745a3a29349', '', '0', 'admin', '2019-06-19 16:50:52', '2019-06-18 17:27:05', '2019-06-18 17:27:02', '0', '1');

-- ----------------------------
-- Table structure for admin_menu
-- ----------------------------
DROP TABLE IF EXISTS `admin_menu`;
CREATE TABLE `admin_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `parent_id` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单/按钮名称',
  `path` varchar(255) DEFAULT NULL COMMENT '对应路由path',
  `component` varchar(255) DEFAULT NULL COMMENT '对应路由组件component',
  `perms` varchar(50) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `type` char(2) NOT NULL COMMENT '类型 0菜单 1按钮',
  `order_num` double(20,0) DEFAULT NULL COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of admin_menu
-- ----------------------------
INSERT INTO `admin_menu` VALUES ('1', '0', '系统管理', 'system', 'Layout', null, 'appstore-o', '0', '1', '2017-12-27 16:39:07', '2019-01-05 11:13:14');
INSERT INTO `admin_menu` VALUES ('4', '1', '角色管理', 'role', 'system/role/index', 'role:view', '', '0', '2', '2017-12-27 16:48:09', '2018-04-25 09:01:12');
INSERT INTO `admin_menu` VALUES ('5', '1', '菜单管理', '/system/menu', 'system/menu/Menu', 'menu:view', '', '0', '3', '2017-12-27 16:48:57', '2018-04-25 09:01:30');
INSERT INTO `admin_menu` VALUES ('6', '1', '用户管理', 'user', 'system/user/index', null, 'peoples', '0', '2', '2017-12-27 16:45:51', '2019-01-23 06:27:12');
INSERT INTO `admin_menu` VALUES ('8', '2', '在线用户', '/monitor/online', 'monitor/Online', 'user:online', '', '0', '1', '2017-12-27 16:59:33', '2018-04-25 09:02:04');
INSERT INTO `admin_menu` VALUES ('10', '2', '系统日志', '/monitor/systemlog', 'monitor/SystemLog', 'log:view', '', '0', '2', '2017-12-27 17:00:50', '2018-04-25 09:02:18');
INSERT INTO `admin_menu` VALUES ('11', '3', '新增用户', '', '', 'user:add', null, '1', null, '2017-12-27 17:02:58', null);
INSERT INTO `admin_menu` VALUES ('12', '3', '修改用户', '', '', 'user:update', null, '1', null, '2017-12-27 17:04:07', null);
INSERT INTO `admin_menu` VALUES ('13', '3', '删除用户', '', '', 'user:delete', null, '1', null, '2017-12-27 17:04:58', null);
INSERT INTO `admin_menu` VALUES ('14', '4', '新增角色', '', '', 'role:add', null, '1', null, '2017-12-27 17:06:38', null);
INSERT INTO `admin_menu` VALUES ('15', '4', '修改角色', '', '', 'role:update', null, '1', null, '2017-12-27 17:06:38', null);
INSERT INTO `admin_menu` VALUES ('16', '4', '删除角色', '', '', 'role:delete', null, '1', null, '2017-12-27 17:06:38', null);
INSERT INTO `admin_menu` VALUES ('17', '5', '新增菜单', '', '', 'menu:add', null, '1', null, '2017-12-27 17:08:02', null);
INSERT INTO `admin_menu` VALUES ('18', '5', '修改菜单', '', '', 'menu:update', null, '1', null, '2017-12-27 17:08:02', null);
INSERT INTO `admin_menu` VALUES ('19', '5', '删除菜单', '', '', 'menu:delete', null, '1', null, '2017-12-27 17:08:02', null);
INSERT INTO `admin_menu` VALUES ('20', '6', '新增部门', '', '', 'dept:add', null, '1', null, '2017-12-27 17:09:24', null);
INSERT INTO `admin_menu` VALUES ('21', '6', '修改部门', '', '', 'dept:update', null, '1', null, '2017-12-27 17:09:24', null);
INSERT INTO `admin_menu` VALUES ('22', '6', '删除部门', '', '', 'dept:delete', null, '1', null, '2017-12-27 17:09:24', null);
INSERT INTO `admin_menu` VALUES ('23', '8', '踢出用户', '', '', 'user:kickout', null, '1', null, '2017-12-27 17:11:13', null);
INSERT INTO `admin_menu` VALUES ('24', '10', '删除日志', '', '', 'log:delete', null, '1', null, '2017-12-27 17:11:45', null);
INSERT INTO `admin_menu` VALUES ('58', '0', '网络资源', '/web', 'PageView', null, 'compass', '0', '4', '2018-01-12 15:28:48', '2018-01-22 19:49:26');
INSERT INTO `admin_menu` VALUES ('59', '58', '天气查询', '/web/weather', 'web/Weather', 'weather:view', '', '0', '1', '2018-01-12 15:40:02', '2019-01-22 05:43:19');
INSERT INTO `admin_menu` VALUES ('61', '58', '每日一文', '/web/dailyArticle', 'web/DailyArticle', 'article:view', '', '0', '2', '2018-01-15 17:17:14', '2019-01-22 05:43:27');
INSERT INTO `admin_menu` VALUES ('64', '1', '字典管理', '/system/dict', 'system/dict/Dict', 'dict:view', '', '0', '5', '2018-01-18 10:38:25', '2018-04-25 09:01:50');
INSERT INTO `admin_menu` VALUES ('65', '64', '新增字典', '', '', 'dict:add', null, '1', null, '2018-01-18 19:10:08', null);
INSERT INTO `admin_menu` VALUES ('66', '64', '修改字典', '', '', 'dict:update', null, '1', null, '2018-01-18 19:10:27', null);
INSERT INTO `admin_menu` VALUES ('67', '64', '删除字典', '', '', 'dict:delete', null, '1', null, '2018-01-18 19:10:47', null);
INSERT INTO `admin_menu` VALUES ('81', '58', '影视资讯', '/web/movie', 'EmptyPageView', null, null, '0', '3', '2018-01-22 14:12:59', '2019-01-22 05:43:35');
INSERT INTO `admin_menu` VALUES ('82', '81', '正在热映', '/web/movie/hot', 'web/MovieHot', 'movie:hot', '', '0', '1', '2018-01-22 14:13:47', '2019-01-22 05:43:52');
INSERT INTO `admin_menu` VALUES ('83', '81', '即将上映', '/web/movie/coming', 'web/MovieComing', 'movie:coming', '', '0', '2', '2018-01-22 14:14:36', '2019-01-22 05:43:58');
INSERT INTO `admin_menu` VALUES ('101', '0', '任务调度', '/job', 'PageView', null, 'clock-circle-o', '0', '3', '2018-01-11 15:52:57', null);
INSERT INTO `admin_menu` VALUES ('102', '101', '定时任务', '/job/job', 'quartz/job/Job', 'job:view', '', '0', '1', '2018-02-24 15:53:53', '2019-01-22 05:42:50');
INSERT INTO `admin_menu` VALUES ('103', '102', '新增任务', '', '', 'job:add', null, '1', null, '2018-02-24 15:55:10', null);
INSERT INTO `admin_menu` VALUES ('104', '102', '修改任务', '', '', 'job:update', null, '1', null, '2018-02-24 15:55:53', null);
INSERT INTO `admin_menu` VALUES ('105', '102', '删除任务', '', '', 'job:delete', null, '1', null, '2018-02-24 15:56:18', null);
INSERT INTO `admin_menu` VALUES ('106', '102', '暂停任务', '', '', 'job:pause', null, '1', null, '2018-02-24 15:57:08', null);
INSERT INTO `admin_menu` VALUES ('107', '102', '恢复任务', '', '', 'job:resume', null, '1', null, '2018-02-24 15:58:21', null);
INSERT INTO `admin_menu` VALUES ('108', '102', '立即执行任务', '', '', 'job:run', null, '1', null, '2018-02-24 15:59:45', null);
INSERT INTO `admin_menu` VALUES ('109', '101', '调度日志', '/job/log', 'quartz/log/JobLog', 'jobLog:view', '', '0', '2', '2018-02-24 16:00:45', '2019-01-22 05:42:59');
INSERT INTO `admin_menu` VALUES ('110', '109', '删除日志', '', '', 'jobLog:delete', null, '1', null, '2018-02-24 16:01:21', null);
INSERT INTO `admin_menu` VALUES ('113', '2', 'Redis监控', '/monitor/redis/info', 'monitor/RedisInfo', 'redis:view', '', '0', '3', '2018-06-28 14:29:42', null);
INSERT INTO `admin_menu` VALUES ('121', '2', '请求追踪', '/monitor/httptrace', 'monitor/Httptrace', null, null, '0', '4', '2019-01-18 02:30:29', null);
INSERT INTO `admin_menu` VALUES ('122', '2', '系统信息', '/monitor/system', 'EmptyPageView', null, null, '0', '5', '2019-01-18 02:31:48', '2019-01-18 02:39:46');
INSERT INTO `admin_menu` VALUES ('123', '122', 'Tomcat信息', '/monitor/system/tomcatinfo', 'monitor/TomcatInfo', null, null, '0', '2', '2019-01-18 02:32:53', '2019-01-18 02:46:57');
INSERT INTO `admin_menu` VALUES ('124', '122', 'JVM信息', '/monitor/system/jvminfo', 'monitor/JvmInfo', null, null, '0', '1', '2019-01-18 02:33:30', '2019-01-18 02:46:51');
INSERT INTO `admin_menu` VALUES ('127', '122', '服务器信息', '/monitor/system/info', 'monitor/SystemInfo', null, null, '0', '3', '2019-01-21 07:53:43', '2019-01-21 07:57:00');
INSERT INTO `admin_menu` VALUES ('128', '0', '其他模块', '/others', 'PageView', null, 'coffee', '0', '5', '2019-01-22 06:49:59', '2019-01-22 06:50:13');
INSERT INTO `admin_menu` VALUES ('129', '128', '导入导出', '/others/excel', 'others/Excel', null, null, '0', '1', '2019-01-22 06:51:36', '2019-01-22 07:06:45');
INSERT INTO `admin_menu` VALUES ('130', '3', '导出Excel', null, null, 'user:export', null, '1', null, '2019-01-23 06:35:16', null);
INSERT INTO `admin_menu` VALUES ('131', '4', '导出Excel', null, null, 'role:export', null, '1', null, '2019-01-23 06:35:36', null);
INSERT INTO `admin_menu` VALUES ('132', '5', '导出Excel', null, null, 'menu:export', null, '1', null, '2019-01-23 06:36:05', null);
INSERT INTO `admin_menu` VALUES ('133', '6', '导出Excel', null, null, 'dept:export', null, '1', null, '2019-01-23 06:36:25', null);
INSERT INTO `admin_menu` VALUES ('134', '64', '导出Excel', null, null, 'dict:export', null, '1', null, '2019-01-23 06:36:43', null);
INSERT INTO `admin_menu` VALUES ('135', '3', '密码重置', null, null, 'user:reset', null, '1', null, '2019-01-23 06:37:00', null);
INSERT INTO `admin_menu` VALUES ('136', '10', '导出Excel', null, null, 'log:export', null, '1', null, '2019-01-23 06:37:27', null);
INSERT INTO `admin_menu` VALUES ('137', '102', '导出Excel', null, null, 'job:export', null, '1', null, '2019-01-23 06:37:59', null);
INSERT INTO `admin_menu` VALUES ('138', '109', '导出Excel', null, null, 'jobLog:export', null, '1', null, '2019-01-23 06:38:32', null);
INSERT INTO `admin_menu` VALUES ('6111', '1', '部门管理', '/system/dept', 'system/dept/Dept', 'dept:view', '', '0', '4', '2017-12-27 16:57:33', '2018-04-25 09:01:40');

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `code` varchar(32) NOT NULL COMMENT '角色编码',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `data_scope` varchar(255) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `is_enable_del` int(11) DEFAULT '0' COMMENT '是否可以删除，默认0可删除1不可删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_role
-- ----------------------------
INSERT INTO `admin_role` VALUES ('1', '2019-06-16 11:15:19', '超级管理员', 'SADMIN', '系统所有权', '全部', '1', '0');
INSERT INTO `admin_role` VALUES ('3', '2019-06-16 11:15:42', '代理用户', 'AGENT', '用于测试菜单与权限', '本级', '3', '0');
INSERT INTO `admin_role` VALUES ('4', '2019-06-18 13:33:06', '爬取', 'CRAWLING', '爬取数据', null, '3', '0');
INSERT INTO `admin_role` VALUES ('12', '2019-06-16 11:15:42', '普通管理员', 'CADMIN', '普通管理员级别为2，使用该角色新增用户时只能赋予比普通管理员级别低的角色', '全部', '2', '0');

-- ----------------------------
-- Table structure for admin_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `admin_role_menu`;
CREATE TABLE `admin_role_menu` (
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_role_menu
-- ----------------------------
INSERT INTO `admin_role_menu` VALUES ('1', '1');
INSERT INTO `admin_role_menu` VALUES ('3', '1');
INSERT INTO `admin_role_menu` VALUES ('11', '1');
INSERT INTO `admin_role_menu` VALUES ('12', '1');
INSERT INTO `admin_role_menu` VALUES ('13', '1');
INSERT INTO `admin_role_menu` VALUES ('4', '1');
INSERT INTO `admin_role_menu` VALUES ('14', '1');
INSERT INTO `admin_role_menu` VALUES ('15', '1');
INSERT INTO `admin_role_menu` VALUES ('16', '1');
INSERT INTO `admin_role_menu` VALUES ('5', '1');
INSERT INTO `admin_role_menu` VALUES ('17', '1');
INSERT INTO `admin_role_menu` VALUES ('18', '1');
INSERT INTO `admin_role_menu` VALUES ('19', '1');
INSERT INTO `admin_role_menu` VALUES ('6', '1');
INSERT INTO `admin_role_menu` VALUES ('20', '1');
INSERT INTO `admin_role_menu` VALUES ('21', '1');
INSERT INTO `admin_role_menu` VALUES ('22', '1');
INSERT INTO `admin_role_menu` VALUES ('64', '1');
INSERT INTO `admin_role_menu` VALUES ('65', '1');
INSERT INTO `admin_role_menu` VALUES ('66', '1');
INSERT INTO `admin_role_menu` VALUES ('67', '1');
INSERT INTO `admin_role_menu` VALUES ('2', '1');
INSERT INTO `admin_role_menu` VALUES ('8', '1');
INSERT INTO `admin_role_menu` VALUES ('23', '1');
INSERT INTO `admin_role_menu` VALUES ('10', '1');
INSERT INTO `admin_role_menu` VALUES ('24', '1');
INSERT INTO `admin_role_menu` VALUES ('113', '1');
INSERT INTO `admin_role_menu` VALUES ('121', '1');
INSERT INTO `admin_role_menu` VALUES ('122', '1');
INSERT INTO `admin_role_menu` VALUES ('124', '1');
INSERT INTO `admin_role_menu` VALUES ('123', '1');
INSERT INTO `admin_role_menu` VALUES ('125', '1');
INSERT INTO `admin_role_menu` VALUES ('101', '1');
INSERT INTO `admin_role_menu` VALUES ('102', '1');
INSERT INTO `admin_role_menu` VALUES ('103', '1');
INSERT INTO `admin_role_menu` VALUES ('104', '1');
INSERT INTO `admin_role_menu` VALUES ('105', '1');
INSERT INTO `admin_role_menu` VALUES ('106', '1');
INSERT INTO `admin_role_menu` VALUES ('107', '1');
INSERT INTO `admin_role_menu` VALUES ('108', '1');
INSERT INTO `admin_role_menu` VALUES ('109', '1');
INSERT INTO `admin_role_menu` VALUES ('110', '1');
INSERT INTO `admin_role_menu` VALUES ('58', '1');
INSERT INTO `admin_role_menu` VALUES ('59', '1');
INSERT INTO `admin_role_menu` VALUES ('61', '1');
INSERT INTO `admin_role_menu` VALUES ('81', '1');
INSERT INTO `admin_role_menu` VALUES ('82', '1');
INSERT INTO `admin_role_menu` VALUES ('83', '1');
INSERT INTO `admin_role_menu` VALUES ('127', '1');
INSERT INTO `admin_role_menu` VALUES ('128', '1');
INSERT INTO `admin_role_menu` VALUES ('129', '1');
INSERT INTO `admin_role_menu` VALUES ('130', '1');
INSERT INTO `admin_role_menu` VALUES ('135', '1');
INSERT INTO `admin_role_menu` VALUES ('131', '1');
INSERT INTO `admin_role_menu` VALUES ('132', '1');
INSERT INTO `admin_role_menu` VALUES ('133', '1');
INSERT INTO `admin_role_menu` VALUES ('134', '1');
INSERT INTO `admin_role_menu` VALUES ('136', '1');
INSERT INTO `admin_role_menu` VALUES ('137', '1');
INSERT INTO `admin_role_menu` VALUES ('138', '1');
INSERT INTO `admin_role_menu` VALUES ('1', '72');
INSERT INTO `admin_role_menu` VALUES ('3', '72');
INSERT INTO `admin_role_menu` VALUES ('4', '72');
INSERT INTO `admin_role_menu` VALUES ('5', '72');
INSERT INTO `admin_role_menu` VALUES ('6', '72');
INSERT INTO `admin_role_menu` VALUES ('64', '72');
INSERT INTO `admin_role_menu` VALUES ('2', '72');
INSERT INTO `admin_role_menu` VALUES ('8', '72');
INSERT INTO `admin_role_menu` VALUES ('10', '72');
INSERT INTO `admin_role_menu` VALUES ('113', '72');
INSERT INTO `admin_role_menu` VALUES ('121', '72');
INSERT INTO `admin_role_menu` VALUES ('122', '72');
INSERT INTO `admin_role_menu` VALUES ('124', '72');
INSERT INTO `admin_role_menu` VALUES ('123', '72');
INSERT INTO `admin_role_menu` VALUES ('127', '72');
INSERT INTO `admin_role_menu` VALUES ('101', '72');
INSERT INTO `admin_role_menu` VALUES ('102', '72');
INSERT INTO `admin_role_menu` VALUES ('109', '72');
INSERT INTO `admin_role_menu` VALUES ('58', '72');
INSERT INTO `admin_role_menu` VALUES ('59', '72');
INSERT INTO `admin_role_menu` VALUES ('61', '72');
INSERT INTO `admin_role_menu` VALUES ('81', '72');
INSERT INTO `admin_role_menu` VALUES ('82', '72');
INSERT INTO `admin_role_menu` VALUES ('83', '72');
INSERT INTO `admin_role_menu` VALUES ('128', '72');
INSERT INTO `admin_role_menu` VALUES ('129', '72');
INSERT INTO `admin_role_menu` VALUES ('3', '2');
INSERT INTO `admin_role_menu` VALUES ('1', '2');
INSERT INTO `admin_role_menu` VALUES ('4', '2');
INSERT INTO `admin_role_menu` VALUES ('5', '2');
INSERT INTO `admin_role_menu` VALUES ('6', '2');
INSERT INTO `admin_role_menu` VALUES ('64', '2');
INSERT INTO `admin_role_menu` VALUES ('2', '2');
INSERT INTO `admin_role_menu` VALUES ('8', '2');
INSERT INTO `admin_role_menu` VALUES ('10', '2');
INSERT INTO `admin_role_menu` VALUES ('113', '2');
INSERT INTO `admin_role_menu` VALUES ('121', '2');
INSERT INTO `admin_role_menu` VALUES ('122', '2');
INSERT INTO `admin_role_menu` VALUES ('124', '2');
INSERT INTO `admin_role_menu` VALUES ('123', '2');
INSERT INTO `admin_role_menu` VALUES ('125', '2');
INSERT INTO `admin_role_menu` VALUES ('101', '2');
INSERT INTO `admin_role_menu` VALUES ('102', '2');
INSERT INTO `admin_role_menu` VALUES ('109', '2');
INSERT INTO `admin_role_menu` VALUES ('58', '2');
INSERT INTO `admin_role_menu` VALUES ('59', '2');
INSERT INTO `admin_role_menu` VALUES ('61', '2');
INSERT INTO `admin_role_menu` VALUES ('81', '2');
INSERT INTO `admin_role_menu` VALUES ('82', '2');
INSERT INTO `admin_role_menu` VALUES ('83', '2');
INSERT INTO `admin_role_menu` VALUES ('127', '2');
INSERT INTO `admin_role_menu` VALUES ('128', '2');
INSERT INTO `admin_role_menu` VALUES ('129', '2');
INSERT INTO `admin_role_menu` VALUES ('130', '2');
INSERT INTO `admin_role_menu` VALUES ('14', '2');
INSERT INTO `admin_role_menu` VALUES ('17', '2');
INSERT INTO `admin_role_menu` VALUES ('132', '2');
INSERT INTO `admin_role_menu` VALUES ('20', '2');
INSERT INTO `admin_role_menu` VALUES ('133', '2');
INSERT INTO `admin_role_menu` VALUES ('65', '2');
INSERT INTO `admin_role_menu` VALUES ('134', '2');
INSERT INTO `admin_role_menu` VALUES ('136', '2');
INSERT INTO `admin_role_menu` VALUES ('103', '2');
INSERT INTO `admin_role_menu` VALUES ('137', '2');
INSERT INTO `admin_role_menu` VALUES ('138', '2');
INSERT INTO `admin_role_menu` VALUES ('131', '2');

-- ----------------------------
-- Table structure for car_info
-- ----------------------------
DROP TABLE IF EXISTS `car_info`;
CREATE TABLE `car_info` (
  `car_info_id` varchar(64) NOT NULL COMMENT 'ID',
  `car_number` varchar(64) DEFAULT NULL COMMENT '车牌号',
  `engine_number` varchar(32) DEFAULT NULL COMMENT '发动机号',
  `frame_number` varchar(1024) DEFAULT NULL COMMENT '车架号',
  `register_date` varchar(128) DEFAULT NULL COMMENT '注册日期',
  `brand_model` varchar(1024) DEFAULT NULL COMMENT '品牌型号',
  `car_model` varchar(1024) DEFAULT NULL COMMENT '车型',
  `purchase_price` int(11) DEFAULT NULL COMMENT '新车购置价',
  `seat_number` int(11) DEFAULT NULL COMMENT '座位数',
  `displacement` decimal(4,1) DEFAULT NULL COMMENT '排量',
  `isTransfer_car` int(11) DEFAULT '0' COMMENT '过户车 0是   1否',
  `isLoan_car` int(11) DEFAULT '0' COMMENT '贷款车 0是   1否',
  `remarks_car` varchar(128) DEFAULT NULL COMMENT '备注',
  `follow_count` int(11) DEFAULT NULL COMMENT '本年跟进次数',
  `follow_time` varchar(32) DEFAULT NULL COMMENT '最后跟进时间',
  `follow_content` varchar(1024) DEFAULT NULL COMMENT '最后跟进内容',
  `plan_return_time` varchar(32) DEFAULT NULL COMMENT '计划回访时间',
  `customer_status` int(11) DEFAULT '0' COMMENT '客户状态 0回访1未回访',
  `customer_type` varchar(32) DEFAULT NULL COMMENT '客户类别',
  `salesman` varchar(32) DEFAULT NULL COMMENT '业务员',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `choice_last_year_insurance_name` varchar(64) DEFAULT NULL COMMENT '手动选择的上年投保公司',
  `choice_last_year_source` varchar(32) DEFAULT NULL COMMENT '手动选择的上年投保公司枚举值',
  `insured_area` varchar(32) DEFAULT NULL COMMENT '投保的地区名称',
  `license_owner` varchar(32) DEFAULT NULL COMMENT '车主',
  `license_owner_id_card` varchar(32) DEFAULT NULL COMMENT '车主证件号码',
  `license_owner_id_card_type` varchar(32) DEFAULT '0' COMMENT '车主证件类型 证件类型 0：没有取到 1：身份证 2: 组织机构代码证 3：护照',
  `customer_Id` varchar(64) DEFAULT NULL COMMENT '客户id',
  `status` int(2) DEFAULT '0',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `channel_type` varchar(255) CHARACTER SET utf8mb4 DEFAULT '-1' COMMENT '车辆信息获取方式默认-1手动添加还未查询过，0使用车牌1使用车架2车牌和车架均使用',
  `is_addtion` varchar(255) DEFAULT '0' COMMENT '是否手动添加,默认0查询获取,1是',
  `is_renew_success` varchar(2) DEFAULT '0' COMMENT '续保状态0默认失败1成功',
  `is_enable` varchar(2) DEFAULT '0' COMMENT '续保状态0默认 1作废',
  PRIMARY KEY (`car_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车辆信息表 ';

-- ----------------------------
-- Records of car_info
-- ----------------------------
INSERT INTO `car_info` VALUES ('20190616163023135881', '苏A0Q1Q4', '171240978', 'LSGBL5348HF087266', '2017-07-03', '雪佛兰SGM7156DAAA', null, '120900', '5', null, '0', '0', null, null, null, null, null, '0', null, null, '1', '2019-06-16 16:30:51', null, '2019-06-16 16:42:07', null, null, null, '胡乐宏', '34262619841001509X', '1', null, '0', '', '0', '0', '1', '0');
INSERT INTO `car_info` VALUES ('c349ba55b5e642e9a6954836f74b02b8', '苏A77D6T', '0373964', 'LWVEA3M44JB126703', '2018-07-02', '吉普GFA7140ETCA', null, '195800', '5', null, '0', '0', null, null, null, null, null, '0', null, null, '1', '2019-06-16 16:48:11', null, null, null, null, null, '狄平', '320106197401252816', '1', null, '0', '', '-1', '0', '1', '0');
INSERT INTO `car_info` VALUES ('c3dd2a7e46894ffab11c7dc0a9b67c95', '苏A23A9E', '11023428', 'LE40G4GB5JL235756', '2018-07-02', '梅赛德斯-奔驰BJ6466G4E多用途乘用车', null, '401000', '5', null, '0', '0', null, null, null, null, null, '0', null, null, '1', '2019-06-16 16:50:28', null, null, null, null, null, '方方', '320102198307090418', '1', null, '0', '', '-1', '0', '1', '0');
INSERT INTO `car_info` VALUES ('cd9f8b306b31452ca8811c6fe6a08414', '苏A82FU5', '170715554', 'LSGXE83LXHD205774', '2017-07-10', '别克SGM6475DAX3多用途乘用车', null, '240900', '5', null, '0', '0', null, null, null, null, null, '0', null, null, '1', '2019-06-16 16:53:13', null, null, null, null, null, '邬海涛', '320923198306302138', '1', null, '0', '', '-1', '0', '1', '0');
INSERT INTO `car_info` VALUES ('eb18b18b6c99484b8a4058b9cc35adf0', '苏A0M0C4', 'BT0609', 'LSVWY4183H2108608', '2017-07-03', '大众汽车SVW71617MM', null, '100900', '5', null, '0', '0', null, null, null, null, null, '0', null, null, '1', '2019-06-16 16:45:57', null, null, null, null, null, '李奎', '420684198408265059', '1', null, '0', '', '-1', '0', '1', '0');

-- ----------------------------
-- Table structure for car_out_danger_info
-- ----------------------------
DROP TABLE IF EXISTS `car_out_danger_info`;
CREATE TABLE `car_out_danger_info` (
  `out_danger_id` varchar(64) NOT NULL COMMENT 'id',
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `biz_count` int(11) DEFAULT NULL COMMENT '商业险次数',
  `biz_money` decimal(32,8) DEFAULT NULL COMMENT '商业出险金额',
  `force_count` int(11) DEFAULT NULL COMMENT '交强险次数',
  `force_money` decimal(32,8) DEFAULT NULL COMMENT '交强出险金额',
  `car_info_id` varchar(64) DEFAULT NULL COMMENT '车辆信息id',
  PRIMARY KEY (`out_danger_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车俩出险信息表 ';

-- ----------------------------
-- Records of car_out_danger_info
-- ----------------------------

-- ----------------------------
-- Table structure for check_info
-- ----------------------------
DROP TABLE IF EXISTS `check_info`;
CREATE TABLE `check_info` (
  `check_info_id` varchar(64) NOT NULL,
  `create_by` varchar(64) DEFAULT NULL COMMENT '操作者id',
  `car_info_id` varchar(64) DEFAULT NULL COMMENT '操作车辆id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `is_first_time` varchar(10) DEFAULT '0' COMMENT '是否首次查询:默认0 是 不是',
  `check_type` varchar(4) DEFAULT '0' COMMENT '查询方式0车牌查询1车架查询',
  `send_time` varchar(32) DEFAULT NULL,
  `car_no` varchar(12) DEFAULT NULL COMMENT '查询使用的车牌',
  `vin_no` varchar(32) DEFAULT NULL COMMENT '查询使用的车架号',
  `is_check_success` varchar(2) DEFAULT '1' COMMENT '是否查询成功默认1查询成功0查询失败',
  `is_renew_success` varchar(2) DEFAULT '0' COMMENT '是否续保成功默认0失败1成功',
  PRIMARY KEY (`check_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of check_info
-- ----------------------------
INSERT INTO `check_info` VALUES ('20190616163023116934', '1', '20190616163023116934', '2019-06-16 16:30:34', null, '0', '0', '2019-06-16 16:30:23', '苏A0Q1Q4', null, '0', '0');
INSERT INTO `check_info` VALUES ('20190616163023125370', '1', '20190616163023125370', '2019-06-16 16:30:59', null, '0', '0', '2019-06-16 16:30:23', '苏A0Q1Q4', null, '0', '0');
INSERT INTO `check_info` VALUES ('20190616163023135881', '1', '20190616163023135881', '2019-06-16 16:30:51', null, '0', '0', '2019-06-16 16:30:23', '苏A0Q1Q4', null, '1', '0');

-- ----------------------------
-- Table structure for commission_percentage
-- ----------------------------
DROP TABLE IF EXISTS `commission_percentage`;
CREATE TABLE `commission_percentage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `biz_percentage` varchar(10) DEFAULT NULL COMMENT '商业险佣金百分点',
  `force_percentage` varchar(10) DEFAULT NULL COMMENT '交强险佣金百分点',
  `level_one` varchar(10) DEFAULT NULL COMMENT '下一级提成百分点',
  `level_two` varchar(10) DEFAULT NULL COMMENT '下二级提成百分点',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `source` varchar(12) DEFAULT NULL COMMENT '保司枚举值',
  `status` varchar(2) DEFAULT '1' COMMENT '状态默认1可用0废弃',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of commission_percentage
-- ----------------------------
INSERT INTO `commission_percentage` VALUES ('1', '15', '4', '2', '1', '2019-06-13 09:46:04', '2019-06-18 10:55:42', '1', '', '太平洋保险', '1');
INSERT INTO `commission_percentage` VALUES ('2', '15', '4', '1', '2', '2019-06-13 11:12:05', '2019-06-18 09:52:23', '2', null, '2', '0');
INSERT INTO `commission_percentage` VALUES ('3', '15', '4', '1', '2', '2019-06-13 11:23:49', '2019-06-18 09:22:25', '2', null, '4', '1');

-- ----------------------------
-- Table structure for crawling_car_info
-- ----------------------------
DROP TABLE IF EXISTS `crawling_car_info`;
CREATE TABLE `crawling_car_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `car_no` varchar(32) DEFAULT NULL COMMENT '车牌',
  `new_car_no` varchar(32) DEFAULT NULL COMMENT '新车牌',
  `is_new_car_no` varchar(64) DEFAULT NULL COMMENT '是否有新车牌0默认1有',
  `car_owner` varchar(64) DEFAULT NULL COMMENT '车主',
  `new_car_owner` varchar(64) DEFAULT NULL COMMENT '新车主',
  `is_new_car_owner` varchar(64) DEFAULT NULL COMMENT '是否有新车主0默认1有',
  `vin_no` varchar(64) DEFAULT NULL COMMENT '车架号',
  `new_vin_no` varchar(64) DEFAULT NULL COMMENT '新车架号',
  `is_new_vin_no` varchar(64) DEFAULT NULL COMMENT '是否有新车架0默认1有',
  `brand` varchar(64) DEFAULT NULL COMMENT '品牌',
  `model` varchar(64) DEFAULT NULL COMMENT '车辆型号',
  `engine_no` varchar(64) DEFAULT NULL COMMENT '发动机号',
  `register_date` varchar(64) DEFAULT NULL COMMENT '登记日期',
  `transfer_date` varchar(64) DEFAULT NULL COMMENT '过户日期',
  `force_company` varchar(64) DEFAULT NULL COMMENT '交强险承保公司',
  `force_end_date` varchar(64) DEFAULT NULL COMMENT '交强险到期日期',
  `biz_company` varchar(64) DEFAULT NULL COMMENT '商业险承保公司',
  `biz_end_date` varchar(64) DEFAULT NULL COMMENT '商业险到期日期',
  `out_danger_count` varchar(64) DEFAULT NULL COMMENT '出险次数',
  `break_reles_count` varchar(64) DEFAULT NULL COMMENT '违章次数',
  `id_card` varchar(64) DEFAULT NULL COMMENT '证件号',
  `mobile` varchar(64) DEFAULT NULL COMMENT '电话',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `series_no` varchar(32) DEFAULT NULL COMMENT '批次号',
  `is_drawling` varchar(2) DEFAULT '0' COMMENT '是否已经爬取0未爬取1车牌爬取2车架爬取3车牌车架都爬取',
  `is_last_drawling` varchar(2) DEFAULT '0' COMMENT '是否上次暂停数据默认0不是1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of crawling_car_info
-- ----------------------------

-- ----------------------------
-- Table structure for crawling_excel_info
-- ----------------------------
DROP TABLE IF EXISTS `crawling_excel_info`;
CREATE TABLE `crawling_excel_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL COMMENT '表名',
  `series_no` varchar(64) DEFAULT NULL COMMENT '批次号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `is_finish` varchar(2) DEFAULT '0' COMMENT '默认0未完成1完成',
  `finish_date` datetime DEFAULT NULL COMMENT '完成时间',
  `type` varchar(2) DEFAULT '0' COMMENT '0车牌1车架',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of crawling_excel_info
-- ----------------------------

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `customer_Id` varchar(64) NOT NULL COMMENT 'ID',
  `customer_name` varchar(128) DEFAULT NULL COMMENT '客户名称',
  `customer_tel1` int(11) DEFAULT NULL COMMENT '客户电话1',
  `customer_tel2` int(11) DEFAULT NULL COMMENT '客户电话2',
  `customer_sort` varchar(32) DEFAULT NULL COMMENT '客户类别',
  `insured_area` varchar(32) DEFAULT NULL COMMENT '投保地区',
  `customer_address` varchar(32) DEFAULT NULL COMMENT '地址',
  `customer_remarks1` varchar(1024) DEFAULT NULL COMMENT '客户备注1',
  `customer_remarks2` varchar(1024) DEFAULT NULL COMMENT '客户备注2',
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int(11) DEFAULT NULL COMMENT '状态 0正常1删除...',
  PRIMARY KEY (`customer_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户信息表 ';

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES ('4d4b43f2f8e54268b3f4a010aeaa4a34', '22222', '123123123', '123123123', '0', null, null, '123123123123', null, null, null, '2019-05-10 14:49:36', null, null, null);
INSERT INTO `customer` VALUES ('6283596187234f288c6b50c807a37ecf', '1111', '111', '111', '0', null, null, '1111', null, null, null, '2019-05-10 14:50:00', null, null, null);
INSERT INTO `customer` VALUES ('7af4d06b433f4c0eb6a29f238d4ac381', '2213', '123', '123', '0', null, null, '123132', null, null, null, '2019-05-10 14:55:17', null, null, null);
INSERT INTO `customer` VALUES ('ad228b58f4ae48598f4987d54d5cccfa', '213', '123', '123', '0', null, null, '123', null, null, null, '2019-05-10 14:53:57', null, null, null);
INSERT INTO `customer` VALUES ('fd9688be884d4e6ea9bf34ffe338b5b2', '1321322222', '12312312', null, '', '', '11', '', '', null, '', '2019-05-17 16:38:33', '', '2019-05-17 16:39:03', null);

-- ----------------------------
-- Table structure for draw_cash
-- ----------------------------
DROP TABLE IF EXISTS `draw_cash`;
CREATE TABLE `draw_cash` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(64) DEFAULT NULL COMMENT '订单号',
  `commission_percentage_id` int(11) DEFAULT NULL COMMENT '佣金分成比例,百分点',
  `biz_cash` varchar(12) DEFAULT NULL COMMENT '商业险佣金',
  `force_cash` varchar(12) DEFAULT NULL COMMENT '交强险险佣金',
  `cash` varchar(12) DEFAULT NULL COMMENT '提成',
  `status` varchar(2) DEFAULT '0' COMMENT '默认0审核中,1通过2驳回3取消',
  `serial_no` varchar(64) DEFAULT NULL COMMENT '流水批次号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `descriton` varchar(255) DEFAULT NULL COMMENT '描述信息',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `income_person` varchar(64) DEFAULT NULL,
  `type` int(1) DEFAULT '0' COMMENT '0佣金，1提成',
  `biz_percentage` varchar(10) DEFAULT '0' COMMENT '交强百分比',
  `force_percentage` varchar(10) DEFAULT '0' COMMENT '商业险百分比',
  `draw_percentage` varchar(10) DEFAULT '0' COMMENT '提成百分比',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of draw_cash
-- ----------------------------

-- ----------------------------
-- Table structure for enable_date_info
-- ----------------------------
DROP TABLE IF EXISTS `enable_date_info`;
CREATE TABLE `enable_date_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `enable_start_date` varchar(32) DEFAULT NULL COMMENT '有效期起始日期',
  `enable_end_date` varchar(32) DEFAULT NULL COMMENT '有效期终止日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of enable_date_info
-- ----------------------------

-- ----------------------------
-- Table structure for insurance_follow_info
-- ----------------------------
DROP TABLE IF EXISTS `insurance_follow_info`;
CREATE TABLE `insurance_follow_info` (
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `insurance_follow_id` varchar(64) NOT NULL COMMENT 'id',
  `created_name` varchar(32) DEFAULT NULL COMMENT '跟进人名称',
  `quote_id` varchar(64) DEFAULT NULL COMMENT '跟进的报价信息id',
  `remark_info` varchar(128) DEFAULT NULL COMMENT '备注信息',
  `source` varchar(32) DEFAULT NULL COMMENT '跟进保司枚举值',
  `car_info_id` varchar(64) DEFAULT NULL COMMENT '车辆信息id',
  PRIMARY KEY (`insurance_follow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='跟进信息表 报价成功后，车主有意向购买，业务员可跟进';

-- ----------------------------
-- Records of insurance_follow_info
-- ----------------------------

-- ----------------------------
-- Table structure for insurance_type_info
-- ----------------------------
DROP TABLE IF EXISTS `insurance_type_info`;
CREATE TABLE `insurance_type_info` (
  `insurance_type_id` int(64) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `insurance_name` varchar(32) DEFAULT NULL COMMENT '承保险种名称',
  `insurance_amount` decimal(32,0) DEFAULT '0' COMMENT '保额 >0投保',
  `insurance_premium` decimal(32,2) DEFAULT '0.00' COMMENT '保费 -保单保费',
  `info_type` varchar(2) DEFAULT NULL COMMENT '类型 0投保1报价（0）',
  `type_id` varchar(64) DEFAULT NULL COMMENT '类型id info_type=0表示续保id，info_type=1表示报价id',
  `excluding_deductible` decimal(32,2) DEFAULT '0.00' COMMENT '不计免',
  `send_time` varchar(32) DEFAULT NULL COMMENT '请求发送日期',
  `standard_premium` varchar(32) DEFAULT NULL COMMENT '保费-标准保费',
  PRIMARY KEY (`insurance_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8 COMMENT='保险分类信息 ';

-- ----------------------------
-- Records of insurance_type_info
-- ----------------------------
INSERT INTO `insurance_type_info` VALUES ('1', null, '1', null, null, null, '交强险', '1', null, '0', '8823941a8146421daa71a6a43773c6b0', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('2', null, '1', null, null, null, '机动车损失保险', '94241', null, '0', '8823941a8146421daa71a6a43773c6b0', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('3', null, '1', null, null, null, '机动车损失险_不计免', '1', null, '0', '8823941a8146421daa71a6a43773c6b0', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('4', null, '1', null, null, null, '商业第三者责任险', '1000000', null, '0', '8823941a8146421daa71a6a43773c6b0', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('5', null, '1', null, null, null, '商业第三者责任险_不计免', '1', null, '0', '8823941a8146421daa71a6a43773c6b0', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('6', null, '1', null, null, null, '交强险', '1', null, '0', '8b8202e3606e4a24abc8611678c70eff', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('7', null, '1', null, null, null, '机动车损失保险', '168800', null, '0', '8b8202e3606e4a24abc8611678c70eff', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('8', null, '1', null, null, null, '机动车损失险_不计免', '1', null, '0', '8b8202e3606e4a24abc8611678c70eff', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('9', null, '1', null, null, null, '商业第三者责任险', '1000000', null, '0', '8b8202e3606e4a24abc8611678c70eff', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('10', null, '1', null, null, null, '商业第三者责任险_不计免', '1', null, '0', '8b8202e3606e4a24abc8611678c70eff', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('11', null, '1', '2019-06-16 16:48:45', null, null, '交强险', '1', null, '1', '20190616164817198448', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('12', null, '1', '2019-06-16 16:48:45', null, null, '机动车损失保险', '181702', '1736.06', '1', '20190616164817198448', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('13', null, '1', '2019-06-16 16:48:45', null, null, '机动车损失险_不计免', '1', '260.41', '1', '20190616164817198448', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('14', null, '1', '2019-06-16 16:48:45', null, null, '商业第三者责任险', '1000000', '1139.02', '1', '20190616164817198448', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('15', null, '1', '2019-06-16 16:48:45', null, null, '商业第三者责任险_不计免', '1', '170.85', '1', '20190616164817198448', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('16', null, '1', '2019-06-16 16:48:45', null, null, '全车盗抢险', '181702', '478.56', '1', '20190616164817198448', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('17', null, '1', '2019-06-16 16:48:45', null, null, '全车盗抢险_不计免', '1', '95.71', '1', '20190616164817198448', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('18', null, '1', null, null, null, '交强险', '1', null, '0', 'aaaec98a750e434e99aab0fe0841ffac', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('19', null, '1', null, null, null, '机动车损失保险', '399800', null, '0', 'aaaec98a750e434e99aab0fe0841ffac', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('20', null, '1', null, null, null, '机动车损失险_不计免', '1', null, '0', 'aaaec98a750e434e99aab0fe0841ffac', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('21', null, '1', null, null, null, '商业第三者责任险', '500000', null, '0', 'aaaec98a750e434e99aab0fe0841ffac', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('22', null, '1', null, null, null, '商业第三者责任险_不计免', '1', null, '0', 'aaaec98a750e434e99aab0fe0841ffac', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('23', null, '1', '2019-06-16 16:50:45', null, null, '交强险', '1', null, '1', '20190616165032914150', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('24', null, '1', '2019-06-16 16:50:45', null, null, '机动车损失保险', '374534', '3577.08', '1', '20190616165032914150', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('25', null, '1', '2019-06-16 16:50:45', null, null, '机动车损失险_不计免', '1', '536.56', '1', '20190616165032914150', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('26', null, '1', '2019-06-16 16:50:45', null, null, '商业第三者责任险', '1000000', '1139.07', '1', '20190616165032914150', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('27', null, '1', '2019-06-16 16:50:45', null, null, '商业第三者责任险_不计免', '1', '170.86', '1', '20190616165032914150', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('28', null, '1', '2019-06-16 16:50:45', null, null, '全车盗抢险', '374534', '917.46', '1', '20190616165032914150', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('29', null, '1', '2019-06-16 16:50:45', null, null, '全车盗抢险_不计免', '1', '183.49', '1', '20190616165032914150', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('30', null, '1', '2019-06-16 16:50:56', null, null, '交强险', '1', null, '1', '20190616165032885959', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('31', null, '1', '2019-06-16 16:50:56', null, null, '机动车损失保险', '374534', '3576.91', '1', '20190616165032885959', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('32', null, '1', '2019-06-16 16:50:56', null, null, '机动车损失险_不计免', '1', '536.54', '1', '20190616165032885959', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('33', null, '1', '2019-06-16 16:50:56', null, null, '商业第三者责任险', '1000000', '1139.02', '1', '20190616165032885959', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('34', null, '1', '2019-06-16 16:50:56', null, null, '商业第三者责任险_不计免', '1', '170.85', '1', '20190616165032885959', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('35', null, '1', '2019-06-16 16:50:56', null, null, '全车盗抢险', '374534', '917.42', '1', '20190616165032885959', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('36', null, '1', '2019-06-16 16:50:56', null, null, '全车盗抢险_不计免', '1', '183.48', '1', '20190616165032885959', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('37', null, '1', null, null, null, '交强险', '1', null, '0', 'a45f7792ce854870b8cb5fc3c0f0abe5', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('38', null, '1', null, null, null, '机动车损失保险', '225001', null, '0', 'a45f7792ce854870b8cb5fc3c0f0abe5', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('39', null, '1', null, null, null, '机动车损失险_不计免', '1', null, '0', 'a45f7792ce854870b8cb5fc3c0f0abe5', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('40', null, '1', null, null, null, '商业第三者责任险', '2000000', null, '0', 'a45f7792ce854870b8cb5fc3c0f0abe5', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('41', null, '1', null, null, null, '商业第三者责任险_不计免', '1', null, '0', 'a45f7792ce854870b8cb5fc3c0f0abe5', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('42', null, '1', '2019-06-16 16:53:40', null, null, '交强险', '1', null, '1', '20190616165328865890', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('43', null, '1', '2019-06-16 16:53:40', null, null, '机动车损失保险', '207656', '1813.95', '1', '20190616165328865890', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('44', null, '1', '2019-06-16 16:53:40', null, null, '机动车损失险_不计免', '1', '272.09', '1', '20190616165328865890', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('45', null, '1', '2019-06-16 16:53:40', null, null, '商业第三者责任险', '1000000', '938.12', '1', '20190616165328865890', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('46', null, '1', '2019-06-16 16:53:40', null, null, '商业第三者责任险_不计免', '1', '140.72', '1', '20190616165328865890', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('47', null, '1', '2019-06-16 16:53:40', null, null, '全车盗抢险', '207656', '442.80', '1', '20190616165328865890', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('48', null, '1', '2019-06-16 16:53:40', null, null, '全车盗抢险_不计免', '1', '88.56', '1', '20190616165328865890', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('49', null, '1', '2019-06-16 16:53:42', null, null, '交强险', '1', null, '1', '20190616165328839886', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('50', null, '1', '2019-06-16 16:53:42', null, null, '机动车损失保险', '207656', '1813.95', '1', '20190616165328839886', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('51', null, '1', '2019-06-16 16:53:42', null, null, '机动车损失险_不计免', '1', '272.09', '1', '20190616165328839886', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('52', null, '1', '2019-06-16 16:53:42', null, null, '商业第三者责任险', '1000000', '938.12', '1', '20190616165328839886', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('53', null, '1', '2019-06-16 16:53:42', null, null, '商业第三者责任险_不计免', '1', '140.72', '1', '20190616165328839886', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('54', null, '1', '2019-06-16 16:53:42', null, null, '全车盗抢险', '207656', '442.80', '1', '20190616165328839886', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('55', null, '1', '2019-06-16 16:53:42', null, null, '全车盗抢险_不计免', '1', '88.56', '1', '20190616165328839886', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('56', null, '1', '2019-06-16 16:53:52', null, null, '交强险', '1', null, '1', '20190616165328841562', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('57', null, '1', '2019-06-16 16:53:52', null, null, '机动车损失保险', '207656', '1813.75', '1', '20190616165328841562', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('58', null, '1', '2019-06-16 16:53:52', null, null, '机动车损失险_不计免', '1', '272.06', '1', '20190616165328841562', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('59', null, '1', '2019-06-16 16:53:52', null, null, '商业第三者责任险', '1000000', '938.02', '1', '20190616165328841562', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('60', null, '1', '2019-06-16 16:53:52', null, null, '商业第三者责任险_不计免', '1', '140.70', '1', '20190616165328841562', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('61', null, '1', '2019-06-16 16:53:52', null, null, '全车盗抢险', '207656', '442.75', '1', '20190616165328841562', null, null, null);
INSERT INTO `insurance_type_info` VALUES ('62', null, '1', '2019-06-16 16:53:52', null, null, '全车盗抢险_不计免', '1', '88.55', '1', '20190616165328841562', null, null, null);

-- ----------------------------
-- Table structure for insured_info
-- ----------------------------
DROP TABLE IF EXISTS `insured_info`;
CREATE TABLE `insured_info` (
  `insured_id` varchar(64) NOT NULL COMMENT 'id',
  `create_id` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(11) DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `insurance_company` varchar(32) DEFAULT NULL COMMENT '选择的投保公司',
  `choice_insurance_source` varchar(32) DEFAULT NULL COMMENT '选择的投保公司枚举值',
  `mechanism_number` varchar(32) DEFAULT NULL COMMENT '机构名称',
  `busines_expire_date` varchar(128) DEFAULT NULL COMMENT '商业险到期时间',
  `force_expire_date` varchar(128) DEFAULT NULL COMMENT '交强险到期时间',
  `next_busines_start_date` varchar(128) DEFAULT NULL COMMENT '商业险起保时间',
  `next_force_start_date` varchar(128) DEFAULT NULL COMMENT '交强险起保时间',
  `force_is_insured` varchar(32) DEFAULT NULL COMMENT '交强险是否投保',
  `biz_is_insured` varchar(32) DEFAULT NULL COMMENT '商业险是否投保',
  `last_year_insurance_company` varchar(32) DEFAULT NULL COMMENT '上年投保公司',
  `last_year_source` varchar(32) DEFAULT NULL COMMENT '上年投保公司的枚举值',
  `license_owner` varchar(32) DEFAULT NULL COMMENT '车主',
  `license_owner_id_card` varchar(32) DEFAULT NULL COMMENT '车主证件号码',
  `license_owner_id_card_type` varchar(32) DEFAULT '0' COMMENT '车主证件类型 证件类型 0：没有取到 1：身份证 2: 组织机构代码证 3：护照',
  `posted_name` varchar(32) DEFAULT NULL COMMENT '投保人姓名',
  `holder_id_card` varchar(32) DEFAULT NULL COMMENT '投报人证件号码',
  `holder_id_card_type` varchar(32) DEFAULT NULL COMMENT '投保人证件类型',
  `insured_name` varchar(32) DEFAULT NULL COMMENT '被保险人',
  `insured__id_card` varchar(32) DEFAULT NULL COMMENT '被保险人证件号码',
  `insured_id_card_type` varchar(32) DEFAULT NULL COMMENT '被保险人证件类型',
  `busines_number` varchar(32) DEFAULT NULL COMMENT '商业险保单号',
  `traffic_number` varchar(32) DEFAULT NULL COMMENT '交强险保单号',
  `force_last_year_out_danger_count` varchar(32) DEFAULT NULL COMMENT '上一年交强险出险次数',
  `biz_last_year_out_danger_count` varchar(32) DEFAULT NULL COMMENT '上一年商业险出险次数',
  `force_last_year_out_danger` varchar(32) DEFAULT NULL COMMENT '上一年交强险出险情况',
  `biz_last_year_out_danger` varchar(32) DEFAULT NULL COMMENT '上一年商业险出险情况',
  `car_info_id` varchar(64) DEFAULT NULL COMMENT '车辆信息表id 关联相关车辆的续保信息',
  `status` int(2) DEFAULT '0',
  PRIMARY KEY (`insured_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投保信息 ';

-- ----------------------------
-- Records of insured_info
-- ----------------------------
INSERT INTO `insured_info` VALUES ('8823941a8146421daa71a6a43773c6b0', '1', '2019-06-16 16:45:57', null, null, null, null, null, '2019-06-30', '2019-06-30', '2019-07-01', '2019-06-30', null, null, '太平洋保险', '1', '李奎', '420684198408265059', '1', '李奎', '420684198408265059', '1', '李奎', '420684198408265059', '1', null, null, null, null, null, null, 'eb18b18b6c99484b8a4058b9cc35adf0', '0');
INSERT INTO `insured_info` VALUES ('8b8202e3606e4a24abc8611678c70eff', '1', '2019-06-16 16:48:11', null, null, null, null, null, '2019-07-01', '2019-07-01', '2019-07-02', '2019-07-02', null, null, '太平洋保险', '1', '狄平', '320106197401252816', '1', '狄平', '320106197401252816', '1', '狄平', '320106197401252816', '1', null, null, null, null, null, null, 'c349ba55b5e642e9a6954836f74b02b8', '0');
INSERT INTO `insured_info` VALUES ('a45f7792ce854870b8cb5fc3c0f0abe5', '1', '2019-06-16 16:53:13', null, null, null, null, null, '2019-07-04', '2019-07-05', '2019-07-05', '2019-07-06', null, null, '平安保险', '2', '邬海涛', '320923198306302138', '1', '邬海涛', '320923198306302138', '1', '邬海涛', '320923198306302138', '1', null, null, null, null, null, null, 'cd9f8b306b31452ca8811c6fe6a08414', '0');
INSERT INTO `insured_info` VALUES ('aaaec98a750e434e99aab0fe0841ffac', '1', '2019-06-16 16:50:28', null, null, null, null, null, '2019-06-29', '2019-07-02', '2019-06-30', '2019-07-02', null, null, '人民保险', '4', '方方', '320102198307090418', '1', '方方', '320102198307090418', '1', '方方', '320102198307090418', '1', null, null, null, null, null, null, 'c3dd2a7e46894ffab11c7dc0a9b67c95', '0');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `i_frame` bit(1) DEFAULT NULL COMMENT '是否外链',
  `name` varchar(255) DEFAULT NULL COMMENT '菜单名称',
  `component` varchar(255) DEFAULT NULL COMMENT '组件',
  `pid` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `sort` bigint(20) NOT NULL COMMENT '排序',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `path` varchar(255) DEFAULT NULL COMMENT '链接地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '2018-12-18 15:11:29', '\0', '系统管理', null, '0', '1', 'system', 'system');
INSERT INTO `menu` VALUES ('2', '2018-12-18 15:14:44', '\0', '用户管理', 'system/user/index', '1', '2', 'peoples', 'user');
INSERT INTO `menu` VALUES ('3', '2018-12-18 15:16:07', '\0', '角色管理', 'system/role/index', '1', '3', 'role', 'role');
INSERT INTO `menu` VALUES ('4', '2018-12-18 15:16:45', '\0', '权限管理', 'system/permission/index', '1', '4', 'permission', 'permission');
INSERT INTO `menu` VALUES ('5', '2018-12-18 15:17:28', '\0', '菜单管理', 'system/menu/index', '1', '5', 'menu', 'menu');
INSERT INTO `menu` VALUES ('6', '2018-12-18 15:17:48', '\0', '系统监控', null, '0', '10', 'monitor', 'monitor');
INSERT INTO `menu` VALUES ('7', '2018-12-18 15:18:26', '\0', '操作日志', 'monitor/log/index', '6', '11', 'log', 'logs');
INSERT INTO `menu` VALUES ('8', '2018-12-18 15:19:01', '\0', '系统缓存', 'monitor/redis/index', '6', '13', 'redis', 'redis');
INSERT INTO `menu` VALUES ('9', '2018-12-18 15:19:34', '\0', 'SQL监控', 'monitor/sql/index', '6', '14', 'sqlMonitor', 'druid');
INSERT INTO `menu` VALUES ('10', '2018-12-19 13:38:16', '\0', '组件管理', null, '0', '50', 'zujian', 'components');
INSERT INTO `menu` VALUES ('11', '2018-12-19 13:38:49', '\0', '图标库', 'components/IconSelect', '10', '51', 'icon', 'icon');
INSERT INTO `menu` VALUES ('12', '2018-12-24 20:37:35', '\0', '实时控制台', 'monitor/log/msg', '6', '16', 'codeConsole', 'msg');
INSERT INTO `menu` VALUES ('14', '2018-12-27 10:13:09', '\0', '邮件工具', 'tools/email/index', '36', '24', 'email', 'email');
INSERT INTO `menu` VALUES ('15', '2018-12-27 11:58:25', '\0', '富文本', 'components/Editor', '10', '52', 'fwb', 'tinymce');
INSERT INTO `menu` VALUES ('16', '2018-12-28 09:36:53', '\0', '图床管理', 'tools/picture/index', '36', '25', 'image', 'pictures');
INSERT INTO `menu` VALUES ('18', '2018-12-31 11:12:15', '\0', '七牛云存储', 'tools/qiniu/index', '36', '26', 'qiniu', 'qiniu');
INSERT INTO `menu` VALUES ('19', '2018-12-31 14:52:38', '\0', '支付宝工具', 'tools/aliPay/index', '36', '27', 'alipay', 'aliPay');
INSERT INTO `menu` VALUES ('24', '2019-01-04 16:24:48', '\0', '三级菜单1', 'nested/menu1/menu1-1', '22', '999', 'menu', 'menu1-1');
INSERT INTO `menu` VALUES ('27', '2019-01-07 17:27:32', '\0', '三级菜单2', 'nested/menu1/menu1-2', '22', '999', 'menu', 'menu1-2');
INSERT INTO `menu` VALUES ('28', '2019-01-07 20:34:40', '\0', '定时任务', 'system/timing/index', '36', '21', 'timing', 'timing');
INSERT INTO `menu` VALUES ('30', '2019-01-11 15:45:55', '\0', '代码生成', 'generator/index', '36', '22', 'dev', 'generator');
INSERT INTO `menu` VALUES ('32', '2019-01-13 13:49:03', '\0', '异常日志', 'monitor/log/errorLog', '6', '12', 'error', 'errorLog');
INSERT INTO `menu` VALUES ('33', '2019-03-08 13:46:44', '\0', 'Markdown', 'components/MarkDown', '10', '53', 'markdown', 'markdown');
INSERT INTO `menu` VALUES ('34', '2019-03-08 15:49:40', '\0', 'Yaml编辑器', 'components/YamlEdit', '10', '54', 'dev', 'yaml');
INSERT INTO `menu` VALUES ('35', '2019-03-25 09:46:00', '\0', '部门管理', 'system/dept/index', '1', '6', 'dept', 'dept');
INSERT INTO `menu` VALUES ('36', '2019-03-29 10:57:35', '\0', '系统工具', '', '0', '20', 'sys-tools', 'sys-tools');
INSERT INTO `menu` VALUES ('37', '2019-03-29 13:51:18', '\0', '岗位管理', 'system/job/index', '1', '7', 'Steve-Jobs', 'job');
INSERT INTO `menu` VALUES ('38', '2019-03-29 19:57:53', '\0', '接口文档', 'tools/swagger/index', '36', '23', 'swagger', 'swagger2');
INSERT INTO `menu` VALUES ('39', '2019-04-10 11:49:04', '\0', '字典管理', 'system/dict/index', '1', '8', 'dictionary', 'dict');
INSERT INTO `menu` VALUES ('40', '2019-05-21 12:56:50', '\0', '123123', 'system/dict/index', '123', '1', 'Steve-Jobs', '');
INSERT INTO `menu` VALUES ('42', '2019-05-21 13:28:40', '', '哈哈哈哈', 'system/dict/index', '40', '66', 'anq', 'http://localhost:9528/#/user/addQuote');
INSERT INTO `menu` VALUES ('44', '2019-05-21 13:37:03', '\0', '错误', '', '0', '999', 'error', '');

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `order_id` varchar(64) NOT NULL COMMENT '订单号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单生成日期',
  `finish_time` datetime DEFAULT NULL COMMENT '订单完成日期',
  `pay_status` int(4) DEFAULT '0' COMMENT '支付状态0待支付,1完成2取消3过期4作废5已重新获取',
  `pay_type` varchar(4) DEFAULT '1' COMMENT '支付用途：1购买账号,2保单',
  `pay_type_id` varchar(32) DEFAULT NULL COMMENT '关联的账号id或者报价id',
  `payment` varchar(4) DEFAULT '1' COMMENT '支付方式1支付宝2微信3pos',
  `pay_money` decimal(10,4) DEFAULT NULL COMMENT '支付金额',
  `car_info_id` varchar(64) DEFAULT NULL COMMENT '车辆信息id',
  `delivery_way` int(4) DEFAULT NULL,
  `delivery_address` varchar(64) DEFAULT NULL,
  `contact_name` varchar(11) DEFAULT NULL COMMENT '联系人',
  `contact_tel` int(11) DEFAULT NULL COMMENT '联系电话',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `cancel_msg` varchar(255) DEFAULT NULL COMMENT '取消信息描述',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `is_draw_cash` varchar(2) DEFAULT '0' COMMENT '是否提现0默认未提现1完成2审核中3驳回',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES ('20190616165802896991', '2019-06-16 16:58:04', null, '0', '2', '20190616165328865890', '3', '4816.2400', 'cd9f8b306b31452ca8811c6fe6a08414', null, null, null, null, '1', null, null, null, '0');

-- ----------------------------
-- Table structure for pay_account
-- ----------------------------
DROP TABLE IF EXISTS `pay_account`;
CREATE TABLE `pay_account` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `account_id` varchar(20) DEFAULT NULL,
  `type` int(1) DEFAULT NULL COMMENT '0支付宝 1微信 2银行卡',
  `amount` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pay_account
-- ----------------------------
INSERT INTO `pay_account` VALUES ('1', '1', '0', '123', '123', '2019-06-16 17:11:06');

-- ----------------------------
-- Table structure for policy_payment_info
-- ----------------------------
DROP TABLE IF EXISTS `policy_payment_info`;
CREATE TABLE `policy_payment_info` (
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `policy_id` varchar(64) NOT NULL COMMENT 'id',
  `car_info_id` varchar(64) DEFAULT NULL COMMENT '车辆信息id',
  `payment` varchar(32) DEFAULT NULL COMMENT '支付方式 1支付宝2微信3pos',
  `pay_money` decimal(32,8) DEFAULT NULL COMMENT '支付金额',
  `order_state` varchar(32) DEFAULT NULL COMMENT '订单状态 0待支付1待出单2已承保3已取消4已过期5已完成',
  `receipt_way` varchar(32) DEFAULT '0' COMMENT '收款方式 0全款',
  `source_name` varchar(32) DEFAULT NULL COMMENT '保险公司名称',
  `source` varchar(32) DEFAULT NULL COMMENT '保险公司枚举值',
  `pay_finish_time` varchar(32) DEFAULT NULL COMMENT '支付成功日期',
  PRIMARY KEY (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='保单支付信息表 ';

-- ----------------------------
-- Records of policy_payment_info
-- ----------------------------

-- ----------------------------
-- Table structure for quote_info
-- ----------------------------
DROP TABLE IF EXISTS `quote_info`;
CREATE TABLE `quote_info` (
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `quote_id` varchar(64) NOT NULL COMMENT 'id',
  `car_info_id` varchar(64) DEFAULT NULL COMMENT '车辆信息表id',
  `quote_status` int(11) DEFAULT '-1' COMMENT '报价状态 报价状态，-1=未报价， 0=报价失败，>0报价成功',
  `quote_result` text COMMENT '报价信息',
  `repeat_submit_result` varchar(32) DEFAULT NULL COMMENT '是否重复投保',
  `submit_status` int(11) DEFAULT '-1' COMMENT '核保状态 核保状态-1未核保 0=核保失败， 1=核保成功, 2=未到期未核保（无需再核保）,',
  `SubmitResult` text COMMENT '核保状态描述 核保状态描述（备注字符最大长度：1000）',
  `force_rate` varchar(64) DEFAULT NULL COMMENT '交强车船险费率（核保通过之后才会有值',
  `no_reparation_sale_rate` varchar(32) DEFAULT NULL COMMENT '无赔偿优惠系数 无赔偿优惠系数',
  `independent_channel_date` varchar(32) DEFAULT NULL COMMENT '自主渠道系数',
  `independent_submit_rate` varchar(32) DEFAULT NULL COMMENT '自主核保系数',
  `traffic_illegal_rate` varchar(32) DEFAULT NULL COMMENT '交通违法活动系数',
  `discount_rate` varchar(32) DEFAULT NULL COMMENT '折扣系数',
  `quote_channel` varchar(32) DEFAULT NULL COMMENT '报价渠道',
  `car_used_type` varchar(32) DEFAULT NULL COMMENT '车辆使用性质 使用性质 1：家庭自用车（默认）， 2：党政机关、事业团体， 3：非营业企业客车， 6：营业货车， 7：非营业货车',
  `car_model` varchar(128) DEFAULT NULL COMMENT '车辆型号',
  `biz_total` decimal(32,2) DEFAULT NULL COMMENT '商业险保费合计',
  `force_total` decimal(32,2) DEFAULT NULL COMMENT '交强险保费合计',
  `tax_total` decimal(32,2) DEFAULT NULL COMMENT '车船税',
  `total` decimal(32,2) DEFAULT NULL COMMENT '保费总额',
  `quote_insurance_name` varchar(32) DEFAULT NULL COMMENT '报价保司名称',
  `quote_source` varchar(32) DEFAULT NULL COMMENT '报价保司枚举值 1太保2平安4人保',
  `biz_no` varchar(64) DEFAULT NULL COMMENT '商业险保单号',
  `force_no` varchar(64) DEFAULT NULL COMMENT '交强险保单号',
  `status` int(2) DEFAULT '0',
  `proposal_no` varchar(32) DEFAULT NULL COMMENT '报价单号 获取支付接口用',
  `pay_url` text COMMENT '支付路径',
  `pay_time` varchar(32) DEFAULT NULL COMMENT '支付日期',
  `ref_id` varchar(64) DEFAULT NULL COMMENT '报价流水号',
  `adv_discount_rate` varchar(32) DEFAULT NULL COMMENT '建议折扣率',
  `force_start_time` varchar(32) DEFAULT NULL COMMENT '交强险起保日期',
  `biz_start_time` varchar(32) DEFAULT NULL COMMENT '商业险起保日期',
  `force_ecompensation_rate` varchar(128) DEFAULT NULL COMMENT '交强险预期赔付率',
  `biz_ecompensation_rate` varchar(128) DEFAULT NULL COMMENT '商业险预期赔付率',
  `biz_premium` varchar(32) DEFAULT NULL COMMENT '商业险标准保费',
  `biz_premiumBy_dis` varchar(32) DEFAULT NULL COMMENT '商业险折后保费',
  `real_discount_rate` varchar(32) DEFAULT NULL COMMENT '实际折扣率',
  `non_claim_discount_rate` varchar(32) DEFAULT NULL COMMENT '无赔款折扣系数',
  `check_no` varchar(32) DEFAULT NULL COMMENT '太保-校验码',
  `pay_no` varchar(32) DEFAULT NULL COMMENT '太保-支付号',
  `serial_no` varchar(32) DEFAULT NULL COMMENT '流水号',
  `payment_notice` varchar(32) DEFAULT NULL COMMENT '交费通知单',
  `pay_end_date` varchar(32) DEFAULT NULL COMMENT '支付的截止日期',
  `pay_msg` text COMMENT '支付信息描述',
  `buid` varchar(64) DEFAULT NULL COMMENT '支付需要',
  `excluding_deductible_total` decimal(10,2) DEFAULT NULL COMMENT '不计免总额',
  `total_rate` varchar(64) DEFAULT NULL COMMENT 'TotalRate(新增)',
  `biz_rate` varchar(64) DEFAULT NULL COMMENT '商业险费率（核保通过才会有值）',
  `channel_id` varchar(64) DEFAULT NULL COMMENT '核保渠道Id(在线支付业务用到)',
  PRIMARY KEY (`quote_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报价信息 ';

-- ----------------------------
-- Records of quote_info
-- ----------------------------
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 16:42:44', null, null, '20190616164244940274', '20190616163023135881', '0', '报价失败：交强险重复:交强险重复投保提示 :【发动机号】:171240978。 【号牌号码】:苏A0Q1Q4”的保单发生重复投保，与其重复投保的其它公司的保单信息如下：车架号 LSGBL5348HF087266 ', '1', '-1', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, '平安保险', '2', null, null, '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 16:42:44', null, null, '20190616164244966478', '20190616163023135881', '0', '报价失败：1.商业险重复投保！\r\n2.重复投保的保险公司：\r\n3.投保确认码：\r\n4.保单号：\r\n5.车牌号：\r\n6.号牌种类：\r\n7.车架号：LSGBL5348HF087266\r\n 8.发动机号：171240978\r\n 9.起保日期：\r\n10.终保日期：\r\n11.签单日期：\r\n12.已投保子险为：', '0', '-1', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, '太平洋保险', '1', null, null, '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 16:42:44', null, null, '20190616164244978462', '20190616163023135881', '0', '报价失败：双险都重复投保:车牌号“苏A0Q1Q4”的保单发生重复投保，与其重复投保的其它公司的保单信息如下：车架号 LSGBL5348HF087266;发动机号 171240978。\r\n商业险重复投保！\r\n车辆识别代号: LSGBL5348HF087266\r\n发动机号:171240978\r\n投保险别信息:\r\n平台未返回', '3', '-1', '3', null, null, null, null, null, null, null, null, null, null, null, null, null, '人民保险', '4', null, null, '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 16:46:34', null, null, '20190616164634724740', 'eb18b18b6c99484b8a4058b9cc35adf0', '0', '报价失败：交强险重复:交强险重复投保提示 :【发动机号】:BT0609。 【号牌号码】:苏A0M0C4”的保单发生重复投保，与其重复投保的其它公司的保单信息如下：车架号 LSVWY4183H2108608 ', '1', '-1', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, '平安保险', '2', null, null, '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 16:46:34', null, null, '20190616164634748801', 'eb18b18b6c99484b8a4058b9cc35adf0', '0', '报价失败：1.商业险重复投保！\r\n2.重复投保的保险公司：\r\n3.投保确认码：\r\n4.保单号：\r\n5.车牌号：\r\n6.号牌种类：\r\n7.车架号：LSVWY4183H2108608\r\n 8.发动机号：BT0609\r\n 9.起保日期：\r\n10.终保日期：\r\n11.签单日期：\r\n12.已投保子险为：\r\n机动车第三者责任保险\r\n不计免赔率险（机动车损失保险）\r\n不计免赔率险（机动车第三者责任保险）\r\n机动车损失保险', '0', '-1', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, '太平洋保险', '1', null, null, '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 16:46:34', null, null, '20190616164634763548', 'eb18b18b6c99484b8a4058b9cc35adf0', '0', '报价失败：双险都重复投保:车牌号“苏A0M0C4”的保单发生重复投保，与其重复投保的其它公司的保单信息如下：车架号 LSVWY4183H2108608;发动机号 BT0609。\r\n商业险重复投保！\r\n车辆识别代号: LSVWY4183H2108608\r\n发动机号:BT0609\r\n投保险别信息:\r\n平台未返回', '3', '-1', '3', null, null, null, null, null, null, null, null, null, null, null, null, null, '人民保险', '4', null, null, '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 16:48:17', null, '2019-06-16 16:48:52', '20190616164817198448', 'c349ba55b5e642e9a6954836f74b02b8', '1', '报价成功', '0', '0', '提交核保失败!交强险：[核保流程]交强险重复投保,已经被占单,交强险投保单号:ANAJA99CTP19A052900S 投保单状态:暂存 投保单系统来源代码:移动承保 经办人姓名:贾欢欢 经办人部门:江宁支公司 出单员姓名:贾欢欢 出单员部门:江宁支公司 保单起保日期：2019-07-02 00 保单终保日期：2020-07-02 00\r\n商业险：[核保流程]商业险重复投保,已经被占单,商业险投保单号:ANAJA99Y1419A040993S 投保单状态:暂存 投保单系统来源代码:移动承保 经办人姓名:贾欢欢 经办人部门:江宁支公司 出单员姓名:贾欢欢 出单员部门:江宁支公司 保单起保日期：2019-07-02 00 保单终保日期：2020-07-02 00', null, '0.85', '0.75', '0.85', '1.0', null, null, null, '吉普GFA7140ETCA轿车//1.368/5/0.00/195800.0/2018', '3880.61', '855.00', '300.00', '5035.61', '太平洋保险', '1', null, null, '0', null, null, null, null, null, '2019-07-02', '2019-07-02', null, null, null, '3880.61', null, null, null, null, null, null, null, null, '220577131', '529.97', '0.54187500', null, null);
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 16:50:32', null, '2019-06-16 16:52:27', '20190616165032885959', 'c3dd2a7e46894ffab11c7dc0a9b67c95', '1', '报价成功', '0', '0', '提交核保失败!交强险：[核保流程]交强险重复投保,已经被占单,交强险投保单号:ANAJ00LCTP19F061752F 投保单状态:核保通过 投保单系统来源代码:统一接入平台 经办人姓名:耿雪琴 经办人部门:代理部 出单员姓名:南京新达新保险 出单员部门:代理部 保单起保日期：2019-07-02 11 保单终保日期：2020-07-02 11\r\n商业险：[核保流程]商业险重复投保,已经被占单,商业险投保单号:ANAJ00LY1419F067766Y 投保单状态:核保通过 投保单系统来源代码:统一接入平台 经办人姓名:耿雪琴 经办人部门:代理部 出单员姓名:南京新达新保险 出单员部门:代理部 保单起保日期：2019-06-30 00 保单终保日期：2020-06-30 00', null, '0.85', '0.75', '0.85', '1.0', null, null, null, '梅赛德斯-奔驰BJ6466G4E多用途乘用车//1.991/5/0.00/401000.0/2018', '6524.22', '855.00', '360.00', '7739.22', '太平洋保险', '1', null, null, '0', null, null, null, null, null, '2019-07-02', '2019-06-30', null, null, null, '6524.22', null, null, null, null, null, null, null, null, '220577769', '893.87', '0.54187500', null, null);
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 16:50:32', null, '2019-06-16 16:52:39', '20190616165032914150', 'c3dd2a7e46894ffab11c7dc0a9b67c95', '1', '报价成功', '0', '3', '商业险：TDAA201932010001808807没有在人保系统中获取到核保意见或没有核保意见	交强险：TDZA201932010001848485没有在人保系统中获取到核保意见或没有核保意见	', null, '0.85', '0.7500346', '0.85', '1.0', null, null, null, '梅赛德斯-奔驰BJ6466G4E多用途乘用车/梅赛德斯-奔驰BJ6466G4E多用途乘用车/1.991/5/0.00/401000.0/', '6524.52', '855.00', '360.00', '7739.52', '人民保险', '4', null, null, '0', null, null, null, null, null, '2019-07-02', '2019-06-30', null, null, null, '6524.52', null, null, null, null, null, null, null, null, '220577769', '893.91', '0.54190000', null, null);
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 16:53:28', null, null, '20190616165328839886', 'cd9f8b306b31452ca8811c6fe6a08414', '1', '报价成功', '0', '-1', '0', null, '0.7', '0.75', '0.85', '1.0', null, null, null, '别克SGM6475DAX3多用途乘用车/别克SGM6475DAX3多用途乘用车1.998手自一体 四驱领先型 国Ⅴ/1.998/5/0.00/240900.0/2016', '3696.24', '760.00', '360.00', '4816.24', '平安保险', '2', null, null, '0', null, null, null, null, null, '2019-07-06', '2019-07-05', null, null, null, '3696.24', null, null, null, null, null, null, null, null, '220578635', '504.37', '0.44625000', null, null);
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 16:53:28', null, null, '20190616165328841562', 'cd9f8b306b31452ca8811c6fe6a08414', '1', '报价成功', '0', '-1', '0', null, '0.7', '0.75', '0.85', '1.0', null, null, null, '别克SGM6475DAX3多用途乘用车//1.998/5/0.00/240900.0/2017', '3695.83', '760.00', '360.00', '4815.83', '太平洋保险', '1', null, null, '0', null, null, null, null, null, '2019-07-06', '2019-07-05', null, null, null, '3695.83', null, null, null, null, null, null, null, null, '220578635', '504.31', '0.44625000', null, null);
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 16:53:28', null, '2019-06-16 16:58:04', '20190616165328865890', 'cd9f8b306b31452ca8811c6fe6a08414', '1', '报价成功', '0', '1', '商业险：满足自动核保规则，自动核保通过，通过原因为： 20160412-3200-对于全业务分类视图已设定折扣的业务，且折扣大于等于全业务分类视图的设定值的商业险业务自动通过（NEW）	交强险：满足自动核保规则，自动核保通过，通过原因为： 20161212-3201转入业务承保交强险的自动核保通过	', '0', '0.7', '0.75', '0.85', '1.0', null, null, null, '别克SGM6475DAX3多用途乘用车/别克SGM6475DAX3多用途乘用车/1.998/5/0.00/240900.0/', '3696.24', '760.00', '360.00', '4816.24', '人民保险', '4', 'TDAA201932010001808815', 'TDZA201932010001848493', '0', null, 'http://pay.epicc.com.cn/s3-modules-gateway/wx/getWechatAuthUrl.action?rdseq=JFCD-JS201906161658000361123&sign=08378eb36749c1ca649c9e65fac11d12', '2019-06-16 16:57:35', null, null, '2019-07-06', '2019-07-05', null, null, null, '3696.24', null, null, 'JFCD-JS201906161658000361123', null, 'JFCD-JS201906161658000361123', '3201190616901123', '2019-06-25 23:59:59', '获取成功！', '220578635', '504.37', '0.44620000', '0', '3251');
INSERT INTO `quote_info` VALUES (null, '1', '2019-06-16 17:11:32', null, '2019-06-16 17:12:04', '20190616165328865891', 'cd9f8b306b31452ca8811c6fe6a08414', '1', null, null, '-1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '4', null, null, '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `code` varchar(32) NOT NULL COMMENT '角色编码',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `data_scope` varchar(255) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `is_enable_del` int(11) DEFAULT '0' COMMENT '是否可以删除，默认0可删除1不可删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '2019-06-16 11:15:19', '超级管理员', 'SADMIN', '系统所有权', '全部', '1', '0');
INSERT INTO `role` VALUES ('3', '2019-06-16 11:15:42', '代理用户', 'AGENT', '用于测试菜单与权限', '本级', '3', '0');
INSERT INTO `role` VALUES ('4', '2019-06-18 13:33:06', '爬取', 'CRAWLING', '爬取数据', null, '3', '0');
INSERT INTO `role` VALUES ('12', '2019-06-16 11:15:42', '普通管理员', 'CADMIN', '普通管理员级别为2，使用该角色新增用户时只能赋予比普通管理员级别低的角色', '全部', '2', '0');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES ('1', '1');
INSERT INTO `role_menu` VALUES ('3', '1');
INSERT INTO `role_menu` VALUES ('11', '1');
INSERT INTO `role_menu` VALUES ('12', '1');
INSERT INTO `role_menu` VALUES ('13', '1');
INSERT INTO `role_menu` VALUES ('4', '1');
INSERT INTO `role_menu` VALUES ('14', '1');
INSERT INTO `role_menu` VALUES ('15', '1');
INSERT INTO `role_menu` VALUES ('16', '1');
INSERT INTO `role_menu` VALUES ('5', '1');
INSERT INTO `role_menu` VALUES ('17', '1');
INSERT INTO `role_menu` VALUES ('18', '1');
INSERT INTO `role_menu` VALUES ('19', '1');
INSERT INTO `role_menu` VALUES ('6', '1');
INSERT INTO `role_menu` VALUES ('20', '1');
INSERT INTO `role_menu` VALUES ('21', '1');
INSERT INTO `role_menu` VALUES ('22', '1');
INSERT INTO `role_menu` VALUES ('64', '1');
INSERT INTO `role_menu` VALUES ('65', '1');
INSERT INTO `role_menu` VALUES ('66', '1');
INSERT INTO `role_menu` VALUES ('67', '1');
INSERT INTO `role_menu` VALUES ('2', '1');
INSERT INTO `role_menu` VALUES ('8', '1');
INSERT INTO `role_menu` VALUES ('23', '1');
INSERT INTO `role_menu` VALUES ('10', '1');
INSERT INTO `role_menu` VALUES ('24', '1');
INSERT INTO `role_menu` VALUES ('113', '1');
INSERT INTO `role_menu` VALUES ('121', '1');
INSERT INTO `role_menu` VALUES ('122', '1');
INSERT INTO `role_menu` VALUES ('124', '1');
INSERT INTO `role_menu` VALUES ('123', '1');
INSERT INTO `role_menu` VALUES ('125', '1');
INSERT INTO `role_menu` VALUES ('101', '1');
INSERT INTO `role_menu` VALUES ('102', '1');
INSERT INTO `role_menu` VALUES ('103', '1');
INSERT INTO `role_menu` VALUES ('104', '1');
INSERT INTO `role_menu` VALUES ('105', '1');
INSERT INTO `role_menu` VALUES ('106', '1');
INSERT INTO `role_menu` VALUES ('107', '1');
INSERT INTO `role_menu` VALUES ('108', '1');
INSERT INTO `role_menu` VALUES ('109', '1');
INSERT INTO `role_menu` VALUES ('110', '1');
INSERT INTO `role_menu` VALUES ('58', '1');
INSERT INTO `role_menu` VALUES ('59', '1');
INSERT INTO `role_menu` VALUES ('61', '1');
INSERT INTO `role_menu` VALUES ('81', '1');
INSERT INTO `role_menu` VALUES ('82', '1');
INSERT INTO `role_menu` VALUES ('83', '1');
INSERT INTO `role_menu` VALUES ('127', '1');
INSERT INTO `role_menu` VALUES ('128', '1');
INSERT INTO `role_menu` VALUES ('129', '1');
INSERT INTO `role_menu` VALUES ('130', '1');
INSERT INTO `role_menu` VALUES ('135', '1');
INSERT INTO `role_menu` VALUES ('131', '1');
INSERT INTO `role_menu` VALUES ('132', '1');
INSERT INTO `role_menu` VALUES ('133', '1');
INSERT INTO `role_menu` VALUES ('134', '1');
INSERT INTO `role_menu` VALUES ('136', '1');
INSERT INTO `role_menu` VALUES ('137', '1');
INSERT INTO `role_menu` VALUES ('138', '1');
INSERT INTO `role_menu` VALUES ('1', '72');
INSERT INTO `role_menu` VALUES ('3', '72');
INSERT INTO `role_menu` VALUES ('4', '72');
INSERT INTO `role_menu` VALUES ('5', '72');
INSERT INTO `role_menu` VALUES ('6', '72');
INSERT INTO `role_menu` VALUES ('64', '72');
INSERT INTO `role_menu` VALUES ('2', '72');
INSERT INTO `role_menu` VALUES ('8', '72');
INSERT INTO `role_menu` VALUES ('10', '72');
INSERT INTO `role_menu` VALUES ('113', '72');
INSERT INTO `role_menu` VALUES ('121', '72');
INSERT INTO `role_menu` VALUES ('122', '72');
INSERT INTO `role_menu` VALUES ('124', '72');
INSERT INTO `role_menu` VALUES ('123', '72');
INSERT INTO `role_menu` VALUES ('127', '72');
INSERT INTO `role_menu` VALUES ('101', '72');
INSERT INTO `role_menu` VALUES ('102', '72');
INSERT INTO `role_menu` VALUES ('109', '72');
INSERT INTO `role_menu` VALUES ('58', '72');
INSERT INTO `role_menu` VALUES ('59', '72');
INSERT INTO `role_menu` VALUES ('61', '72');
INSERT INTO `role_menu` VALUES ('81', '72');
INSERT INTO `role_menu` VALUES ('82', '72');
INSERT INTO `role_menu` VALUES ('83', '72');
INSERT INTO `role_menu` VALUES ('128', '72');
INSERT INTO `role_menu` VALUES ('129', '72');
INSERT INTO `role_menu` VALUES ('3', '2');
INSERT INTO `role_menu` VALUES ('1', '2');
INSERT INTO `role_menu` VALUES ('4', '2');
INSERT INTO `role_menu` VALUES ('5', '2');
INSERT INTO `role_menu` VALUES ('6', '2');
INSERT INTO `role_menu` VALUES ('64', '2');
INSERT INTO `role_menu` VALUES ('2', '2');
INSERT INTO `role_menu` VALUES ('8', '2');
INSERT INTO `role_menu` VALUES ('10', '2');
INSERT INTO `role_menu` VALUES ('113', '2');
INSERT INTO `role_menu` VALUES ('121', '2');
INSERT INTO `role_menu` VALUES ('122', '2');
INSERT INTO `role_menu` VALUES ('124', '2');
INSERT INTO `role_menu` VALUES ('123', '2');
INSERT INTO `role_menu` VALUES ('125', '2');
INSERT INTO `role_menu` VALUES ('101', '2');
INSERT INTO `role_menu` VALUES ('102', '2');
INSERT INTO `role_menu` VALUES ('109', '2');
INSERT INTO `role_menu` VALUES ('58', '2');
INSERT INTO `role_menu` VALUES ('59', '2');
INSERT INTO `role_menu` VALUES ('61', '2');
INSERT INTO `role_menu` VALUES ('81', '2');
INSERT INTO `role_menu` VALUES ('82', '2');
INSERT INTO `role_menu` VALUES ('83', '2');
INSERT INTO `role_menu` VALUES ('127', '2');
INSERT INTO `role_menu` VALUES ('128', '2');
INSERT INTO `role_menu` VALUES ('129', '2');
INSERT INTO `role_menu` VALUES ('130', '2');
INSERT INTO `role_menu` VALUES ('14', '2');
INSERT INTO `role_menu` VALUES ('17', '2');
INSERT INTO `role_menu` VALUES ('132', '2');
INSERT INTO `role_menu` VALUES ('20', '2');
INSERT INTO `role_menu` VALUES ('133', '2');
INSERT INTO `role_menu` VALUES ('65', '2');
INSERT INTO `role_menu` VALUES ('134', '2');
INSERT INTO `role_menu` VALUES ('136', '2');
INSERT INTO `role_menu` VALUES ('103', '2');
INSERT INTO `role_menu` VALUES ('137', '2');
INSERT INTO `role_menu` VALUES ('138', '2');
INSERT INTO `role_menu` VALUES ('131', '2');

-- ----------------------------
-- Table structure for temporary_relationship_info
-- ----------------------------
DROP TABLE IF EXISTS `temporary_relationship_info`;
CREATE TABLE `temporary_relationship_info` (
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `temporary_id` varchar(64) NOT NULL COMMENT 'id',
  `name` varchar(32) DEFAULT NULL COMMENT '名称',
  `id_card` varchar(32) DEFAULT NULL COMMENT '证件号',
  `id_card_type` varchar(32) DEFAULT NULL COMMENT '证件类型 1：身份证 2: 组织机构代码证5：港澳居民来往内地通行证 6：其他 7:港澳通行证 8:出生证 9: 营业执照（社会统一信用代码）',
  `mobile` varchar(32) DEFAULT NULL COMMENT '电话',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `is_public` varchar(32) DEFAULT '0' COMMENT '是公户还是个户 0个人1公户默认0',
  `car_info_id` varchar(64) DEFAULT NULL COMMENT '车辆信息id',
  PRIMARY KEY (`temporary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='临时关系人列表 ';

-- ----------------------------
-- Records of temporary_relationship_info
-- ----------------------------

-- ----------------------------
-- Table structure for third_insurance_account_date_info
-- ----------------------------
DROP TABLE IF EXISTS `third_insurance_account_date_info`;
CREATE TABLE `third_insurance_account_date_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `start_time` varchar(20) DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(20) DEFAULT NULL COMMENT '结束时间',
  `enable_years` tinyint(10) DEFAULT '0' COMMENT '有效期年',
  `enable_months` tinyint(10) DEFAULT '0' COMMENT '有效期月',
  `enable_days` tinyint(10) DEFAULT '0' COMMENT '有效期天',
  `account_id` varchar(32) DEFAULT NULL COMMENT '父账号账号id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of third_insurance_account_date_info
-- ----------------------------
INSERT INTO `third_insurance_account_date_info` VALUES ('13', null, '2019-05-06 15:07:35', null, '2019-05-06 15:07:35', '2019-05-07 15:07:35', '0', '0', '0', 'sss');
INSERT INTO `third_insurance_account_date_info` VALUES ('14', null, '2019-05-06 15:07:36', null, '2019-05-06 15:07:36', '2019-05-07 15:07:36', '0', '0', '0', 'sss');
INSERT INTO `third_insurance_account_date_info` VALUES ('15', null, '2019-05-06 15:07:38', null, '2019-05-06 15:07:38', '2019-05-07 15:07:38', '0', '0', '0', 'sss');
INSERT INTO `third_insurance_account_date_info` VALUES ('16', null, '2019-05-06 15:07:39', null, '2019-05-06 15:07:39', '2019-05-07 15:07:39', '0', '0', '0', 'sss');
INSERT INTO `third_insurance_account_date_info` VALUES ('17', null, '2019-05-06 15:07:39', null, '2019-05-06 15:07:39', '2019-05-07 15:07:39', '0', '0', '0', 'sss');
INSERT INTO `third_insurance_account_date_info` VALUES ('18', null, '2019-05-06 15:07:40', null, '2019-05-06 15:07:40', '2019-05-07 15:07:40', '0', '0', '0', 'sss');
INSERT INTO `third_insurance_account_date_info` VALUES ('19', null, '2019-05-06 15:18:53', null, '2019-05-06 15:18:53', '2020-07-06 15:18:53', '0', '0', '0', 'sss');
INSERT INTO `third_insurance_account_date_info` VALUES ('20', null, '2019-05-06 15:19:22', null, '2019-05-06 15:19:22', '2020-07-06 15:19:22', '0', '0', '0', 'sss');
INSERT INTO `third_insurance_account_date_info` VALUES ('21', null, '2019-05-06 15:20:10', null, '2019-05-06 15:20:10', '2020-07-06 15:20:10', '0', '0', '0', 'sss');
INSERT INTO `third_insurance_account_date_info` VALUES ('22', null, '2019-05-06 15:20:51', null, '2019-05-06 15:20:51', '2019-06-06 15:20:51', '0', '0', '0', 'sss');
INSERT INTO `third_insurance_account_date_info` VALUES ('23', null, '2019-05-06 15:21:18', null, '2019-05-06 15:21:18', '2019-04-06 15:21:18', '0', '0', '0', 'sss');
INSERT INTO `third_insurance_account_date_info` VALUES ('24', null, '2019-05-06 15:49:23', null, '2019-05-06 15:49:23', '2019-05-06 15:49:23', '0', '0', '0', 'sss');
INSERT INTO `third_insurance_account_date_info` VALUES ('25', '111111', '2019-05-06 17:20:15', null, '2019-05-06 17:19:45', '2020-06-07 17:20:15', '1', '1', '1', '111');
INSERT INTO `third_insurance_account_date_info` VALUES ('27', '111111', '2019-05-06 17:28:46', null, '2019-05-06 17:28:46', '2019-05-06 17:28:46', '0', '0', '0', '111');
INSERT INTO `third_insurance_account_date_info` VALUES ('29', '111111', '2019-05-06 17:37:46', null, '2019-05-06 17:37:46', '2019-05-06 17:37:46', '0', '0', '0', '111');

-- ----------------------------
-- Table structure for third_insurance_account_info
-- ----------------------------
DROP TABLE IF EXISTS `third_insurance_account_info`;
CREATE TABLE `third_insurance_account_info` (
  `third_insurance_id` varchar(64) NOT NULL,
  `create_id` varchar(64) DEFAULT NULL COMMENT '添加人id',
  `account_name` varchar(64) DEFAULT NULL COMMENT '账号',
  `account_pwd` varchar(64) DEFAULT NULL COMMENT '密码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `vpn` varchar(64) DEFAULT NULL COMMENT '保险公司vpn',
  `account_id` varchar(64) DEFAULT NULL COMMENT '账号id',
  `is_pay` varchar(4) DEFAULT '0' COMMENT '是否支付成功,0未支付1支付',
  `account_type` varchar(32) DEFAULT '0' COMMENT '账号类型默认0 1太保2平安4人保',
  `ip` varchar(225) DEFAULT NULL COMMENT '地址',
  `port` varchar(12) DEFAULT NULL COMMENT '端口',
  `status` varchar(4) DEFAULT '1' COMMENT '状态,是否可用默认启用0废弃',
  `enable_start_date` varchar(20) DEFAULT NULL COMMENT '有效期起止日期',
  `enable_end_date` varchar(20) DEFAULT NULL COMMENT '有效期截止日期',
  `level` varchar(2) DEFAULT '1' COMMENT '0总部账号1子账号',
  PRIMARY KEY (`third_insurance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方保险公司账号,用于续保报价 ';

-- ----------------------------
-- Records of third_insurance_account_info
-- ----------------------------
INSERT INTO `third_insurance_account_info` VALUES ('1111', null, 'ssss', null, '2019-06-18 11:26:55', '2019-06-18 13:27:22', null, null, '0', '1', null, null, '1', null, null, '0');
INSERT INTO `third_insurance_account_info` VALUES ('20190514151438427127', null, null, null, '2019-05-14 15:14:55', '2019-06-18 13:27:19', null, '1', '1', '1', 'http://47.103.61.241', '5000', '1', null, null, '0');
INSERT INTO `third_insurance_account_info` VALUES ('20190514151622163223', null, null, null, '2019-05-14 15:16:38', '2019-06-18 13:27:18', null, '1', '1', '4', 'http://111.231.91.188', '4050', '1', null, null, '0');
INSERT INTO `third_insurance_account_info` VALUES ('20190514151745194517', null, 'JSHSBXDL-00002', 'NJXXKJ147!', '2019-05-14 15:18:01', '2019-06-18 13:27:17', null, '1', '1', '2', 'http://47.103.61.241', '4000', '1', null, null, '0');
INSERT INTO `third_insurance_account_info` VALUES ('20190618170822288535', null, null, null, '2019-06-18 17:09:38', null, null, null, '0', '0', null, 'sss', '1', null, null, '1');

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `MENU_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `PARENT_ID` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `MENU_NAME` varchar(50) NOT NULL COMMENT '菜单/按钮名称',
  `PATH` varchar(255) DEFAULT NULL COMMENT '对应路由path',
  `COMPONENT` varchar(255) DEFAULT NULL COMMENT '对应路由组件component',
  `PERMS` varchar(50) DEFAULT NULL COMMENT '权限标识',
  `ICON` varchar(50) DEFAULT NULL COMMENT '图标',
  `TYPE` char(2) NOT NULL COMMENT '类型 0菜单 1按钮',
  `ORDER_NUM` double(20,0) DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`MENU_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES ('1', '0', '系统管理', '/system', 'PageView', null, 'appstore-o', '0', '1', '2017-12-27 16:39:07', '2019-01-05 11:13:14');
INSERT INTO `t_menu` VALUES ('2', '0', '系统监控', '/monitor', 'PageView', null, 'dashboard', '0', '2', '2017-12-27 16:45:51', '2019-01-23 06:27:12');
INSERT INTO `t_menu` VALUES ('3', '1', '用户管理', '/system/user', 'system/user/User', 'user:view', '', '0', '1', '2017-12-27 16:47:13', '2019-01-22 06:45:55');
INSERT INTO `t_menu` VALUES ('4', '1', '角色管理', '/system/role', 'system/role/Role', 'role:view', '', '0', '2', '2017-12-27 16:48:09', '2018-04-25 09:01:12');
INSERT INTO `t_menu` VALUES ('5', '1', '菜单管理', '/system/menu', 'system/menu/Menu', 'menu:view', '', '0', '3', '2017-12-27 16:48:57', '2018-04-25 09:01:30');
INSERT INTO `t_menu` VALUES ('6', '1', '部门管理', '/system/dept', 'system/dept/Dept', 'dept:view', '', '0', '4', '2017-12-27 16:57:33', '2018-04-25 09:01:40');
INSERT INTO `t_menu` VALUES ('8', '2', '在线用户', '/monitor/online', 'monitor/Online', 'user:online', '', '0', '1', '2017-12-27 16:59:33', '2018-04-25 09:02:04');
INSERT INTO `t_menu` VALUES ('10', '2', '系统日志', '/monitor/systemlog', 'monitor/SystemLog', 'log:view', '', '0', '2', '2017-12-27 17:00:50', '2018-04-25 09:02:18');
INSERT INTO `t_menu` VALUES ('11', '3', '新增用户', '', '', 'user:add', null, '1', null, '2017-12-27 17:02:58', null);
INSERT INTO `t_menu` VALUES ('12', '3', '修改用户', '', '', 'user:update', null, '1', null, '2017-12-27 17:04:07', null);
INSERT INTO `t_menu` VALUES ('13', '3', '删除用户', '', '', 'user:delete', null, '1', null, '2017-12-27 17:04:58', null);
INSERT INTO `t_menu` VALUES ('14', '4', '新增角色', '', '', 'role:add', null, '1', null, '2017-12-27 17:06:38', null);
INSERT INTO `t_menu` VALUES ('15', '4', '修改角色', '', '', 'role:update', null, '1', null, '2017-12-27 17:06:38', null);
INSERT INTO `t_menu` VALUES ('16', '4', '删除角色', '', '', 'role:delete', null, '1', null, '2017-12-27 17:06:38', null);
INSERT INTO `t_menu` VALUES ('17', '5', '新增菜单', '', '', 'menu:add', null, '1', null, '2017-12-27 17:08:02', null);
INSERT INTO `t_menu` VALUES ('18', '5', '修改菜单', '', '', 'menu:update', null, '1', null, '2017-12-27 17:08:02', null);
INSERT INTO `t_menu` VALUES ('19', '5', '删除菜单', '', '', 'menu:delete', null, '1', null, '2017-12-27 17:08:02', null);
INSERT INTO `t_menu` VALUES ('20', '6', '新增部门', '', '', 'dept:add', null, '1', null, '2017-12-27 17:09:24', null);
INSERT INTO `t_menu` VALUES ('21', '6', '修改部门', '', '', 'dept:update', null, '1', null, '2017-12-27 17:09:24', null);
INSERT INTO `t_menu` VALUES ('22', '6', '删除部门', '', '', 'dept:delete', null, '1', null, '2017-12-27 17:09:24', null);
INSERT INTO `t_menu` VALUES ('23', '8', '踢出用户', '', '', 'user:kickout', null, '1', null, '2017-12-27 17:11:13', null);
INSERT INTO `t_menu` VALUES ('24', '10', '删除日志', '', '', 'log:delete', null, '1', null, '2017-12-27 17:11:45', null);
INSERT INTO `t_menu` VALUES ('58', '0', '网络资源', '/web', 'PageView', null, 'compass', '0', '4', '2018-01-12 15:28:48', '2018-01-22 19:49:26');
INSERT INTO `t_menu` VALUES ('59', '58', '天气查询', '/web/weather', 'web/Weather', 'weather:view', '', '0', '1', '2018-01-12 15:40:02', '2019-01-22 05:43:19');
INSERT INTO `t_menu` VALUES ('61', '58', '每日一文', '/web/dailyArticle', 'web/DailyArticle', 'article:view', '', '0', '2', '2018-01-15 17:17:14', '2019-01-22 05:43:27');
INSERT INTO `t_menu` VALUES ('64', '1', '字典管理', '/system/dict', 'system/dict/Dict', 'dict:view', '', '0', '5', '2018-01-18 10:38:25', '2018-04-25 09:01:50');
INSERT INTO `t_menu` VALUES ('65', '64', '新增字典', '', '', 'dict:add', null, '1', null, '2018-01-18 19:10:08', null);
INSERT INTO `t_menu` VALUES ('66', '64', '修改字典', '', '', 'dict:update', null, '1', null, '2018-01-18 19:10:27', null);
INSERT INTO `t_menu` VALUES ('67', '64', '删除字典', '', '', 'dict:delete', null, '1', null, '2018-01-18 19:10:47', null);
INSERT INTO `t_menu` VALUES ('81', '58', '影视资讯', '/web/movie', 'EmptyPageView', null, null, '0', '3', '2018-01-22 14:12:59', '2019-01-22 05:43:35');
INSERT INTO `t_menu` VALUES ('82', '81', '正在热映', '/web/movie/hot', 'web/MovieHot', 'movie:hot', '', '0', '1', '2018-01-22 14:13:47', '2019-01-22 05:43:52');
INSERT INTO `t_menu` VALUES ('83', '81', '即将上映', '/web/movie/coming', 'web/MovieComing', 'movie:coming', '', '0', '2', '2018-01-22 14:14:36', '2019-01-22 05:43:58');
INSERT INTO `t_menu` VALUES ('101', '0', '任务调度', '/job', 'PageView', null, 'clock-circle-o', '0', '3', '2018-01-11 15:52:57', null);
INSERT INTO `t_menu` VALUES ('102', '101', '定时任务', '/job/job', 'quartz/job/Job', 'job:view', '', '0', '1', '2018-02-24 15:53:53', '2019-01-22 05:42:50');
INSERT INTO `t_menu` VALUES ('103', '102', '新增任务', '', '', 'job:add', null, '1', null, '2018-02-24 15:55:10', null);
INSERT INTO `t_menu` VALUES ('104', '102', '修改任务', '', '', 'job:update', null, '1', null, '2018-02-24 15:55:53', null);
INSERT INTO `t_menu` VALUES ('105', '102', '删除任务', '', '', 'job:delete', null, '1', null, '2018-02-24 15:56:18', null);
INSERT INTO `t_menu` VALUES ('106', '102', '暂停任务', '', '', 'job:pause', null, '1', null, '2018-02-24 15:57:08', null);
INSERT INTO `t_menu` VALUES ('107', '102', '恢复任务', '', '', 'job:resume', null, '1', null, '2018-02-24 15:58:21', null);
INSERT INTO `t_menu` VALUES ('108', '102', '立即执行任务', '', '', 'job:run', null, '1', null, '2018-02-24 15:59:45', null);
INSERT INTO `t_menu` VALUES ('109', '101', '调度日志', '/job/log', 'quartz/log/JobLog', 'jobLog:view', '', '0', '2', '2018-02-24 16:00:45', '2019-01-22 05:42:59');
INSERT INTO `t_menu` VALUES ('110', '109', '删除日志', '', '', 'jobLog:delete', null, '1', null, '2018-02-24 16:01:21', null);
INSERT INTO `t_menu` VALUES ('113', '2', 'Redis监控', '/monitor/redis/info', 'monitor/RedisInfo', 'redis:view', '', '0', '3', '2018-06-28 14:29:42', null);
INSERT INTO `t_menu` VALUES ('121', '2', '请求追踪', '/monitor/httptrace', 'monitor/Httptrace', null, null, '0', '4', '2019-01-18 02:30:29', null);
INSERT INTO `t_menu` VALUES ('122', '2', '系统信息', '/monitor/system', 'EmptyPageView', null, null, '0', '5', '2019-01-18 02:31:48', '2019-01-18 02:39:46');
INSERT INTO `t_menu` VALUES ('123', '122', 'Tomcat信息', '/monitor/system/tomcatinfo', 'monitor/TomcatInfo', null, null, '0', '2', '2019-01-18 02:32:53', '2019-01-18 02:46:57');
INSERT INTO `t_menu` VALUES ('124', '122', 'JVM信息', '/monitor/system/jvminfo', 'monitor/JvmInfo', null, null, '0', '1', '2019-01-18 02:33:30', '2019-01-18 02:46:51');
INSERT INTO `t_menu` VALUES ('127', '122', '服务器信息', '/monitor/system/info', 'monitor/SystemInfo', null, null, '0', '3', '2019-01-21 07:53:43', '2019-01-21 07:57:00');
INSERT INTO `t_menu` VALUES ('128', '0', '其他模块', '/others', 'PageView', null, 'coffee', '0', '5', '2019-01-22 06:49:59', '2019-01-22 06:50:13');
INSERT INTO `t_menu` VALUES ('129', '128', '导入导出', '/others/excel', 'others/Excel', null, null, '0', '1', '2019-01-22 06:51:36', '2019-01-22 07:06:45');
INSERT INTO `t_menu` VALUES ('130', '3', '导出Excel', null, null, 'user:export', null, '1', null, '2019-01-23 06:35:16', null);
INSERT INTO `t_menu` VALUES ('131', '4', '导出Excel', null, null, 'role:export', null, '1', null, '2019-01-23 06:35:36', null);
INSERT INTO `t_menu` VALUES ('132', '5', '导出Excel', null, null, 'menu:export', null, '1', null, '2019-01-23 06:36:05', null);
INSERT INTO `t_menu` VALUES ('133', '6', '导出Excel', null, null, 'dept:export', null, '1', null, '2019-01-23 06:36:25', null);
INSERT INTO `t_menu` VALUES ('134', '64', '导出Excel', null, null, 'dict:export', null, '1', null, '2019-01-23 06:36:43', null);
INSERT INTO `t_menu` VALUES ('135', '3', '密码重置', null, null, 'user:reset', null, '1', null, '2019-01-23 06:37:00', null);
INSERT INTO `t_menu` VALUES ('136', '10', '导出Excel', null, null, 'log:export', null, '1', null, '2019-01-23 06:37:27', null);
INSERT INTO `t_menu` VALUES ('137', '102', '导出Excel', null, null, 'job:export', null, '1', null, '2019-01-23 06:37:59', null);
INSERT INTO `t_menu` VALUES ('138', '109', '导出Excel', null, null, 'jobLog:export', null, '1', null, '2019-01-23 06:38:32', null);

-- ----------------------------
-- Table structure for upload_file_info
-- ----------------------------
DROP TABLE IF EXISTS `upload_file_info`;
CREATE TABLE `upload_file_info` (
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '上传人',
  `CREATED_TIME` datetime DEFAULT NULL COMMENT '上传时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `upload_id` varchar(64) DEFAULT NULL COMMENT 'id',
  `file_name` varchar(32) DEFAULT NULL COMMENT '上传的文件名称',
  `total_upload` varchar(32) DEFAULT NULL COMMENT '上传总数',
  `success_upload` varchar(32) DEFAULT NULL COMMENT '上传成功数量',
  `failure_upload` varchar(32) DEFAULT NULL COMMENT '上传失败数量',
  `renewal_state` varchar(32) DEFAULT NULL COMMENT '续保状态 0未完成1已完成'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='批量续保信息 ';

-- ----------------------------
-- Records of upload_file_info
-- ----------------------------

-- ----------------------------
-- Table structure for verification
-- ----------------------------
DROP TABLE IF EXISTS `verification`;
CREATE TABLE `verification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `money` varchar(10) DEFAULT NULL COMMENT '金额',
  `status` varchar(2) DEFAULT '0' COMMENT '审核状态0待审核1通过2驳回',
  `pay_account_id` int(11) DEFAULT NULL COMMENT '收款账号',
  `description` varchar(255) DEFAULT NULL COMMENT '反馈信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `verification_time` timestamp NULL DEFAULT NULL COMMENT '审核日期',
  `create_by` varchar(64) DEFAULT NULL COMMENT '提交人',
  `verification_by` varchar(64) DEFAULT NULL COMMENT '审核人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of verification
-- ----------------------------
INSERT INTO `verification` VALUES ('1', '321', '1', '1', null, '2019-06-16 17:11:21', '2019-06-18 08:52:09', '1', '');
INSERT INTO `verification` VALUES ('2', '2560', '1', '1', null, '2019-06-16 17:14:59', '2019-06-18 08:52:08', '1', '');
INSERT INTO `verification` VALUES ('3', '123', '1', '1', null, '2019-06-16 17:15:06', '2019-06-18 08:52:05', '1', '');
INSERT INTO `verification` VALUES ('4', '123', '1', '1', null, '2019-06-16 17:15:09', '2019-06-18 08:52:04', '1', '');

-- ----------------------------
-- Procedure structure for createChildListByPro
-- ----------------------------
DROP PROCEDURE IF EXISTS `createChildListByPro`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `createChildListByPro`(IN rootId INT,IN nDepth INT)
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE b INT;
	DECLARE cur1 CURSOR FOR SELECT account_id FROM account_info where parent_id=rootId;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	insert into tmpLst values(null,id,depth);
	OPEN cur1;
	FETCH cur1 INTO b;
	WHILE done=0 DO
	CALL createChildListByPro(b,nDepth+1);
		FETCH cur1 INTO b;
	END WHILE;
	CLOSE cur1;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getEndDateByDate
-- ----------------------------
DROP PROCEDURE IF EXISTS `getEndDateByDate`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `getEndDateByDate`(in enableDays INT,in enableMonths INT ,in enableYears INT ,INOUT endDate VARCHAR(32))
    COMMENT '根据年月日计算结束日期'
BEGIN

	SET endDate=DATE_ADD(DATE_ADD(DATE_ADD(NOW(),INTERVAL enableDays DAY) ,INTERVAL enableMonths MONTH),INTERVAL enableYears YEAR);
 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for saveThirdAccountForEndTime
-- ----------------------------
DROP PROCEDURE IF EXISTS `saveThirdAccountForEndTime`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `saveThirdAccountForEndTime`(IN `createBy` varchar(32),IN `startTime` varchar(32),IN `endTime` varchar(32),IN `enableDays` tinyint,IN `enableMonths` tinyint,IN `enableYears` tinyint,IN `accountId` varchar(32))
BEGIN
	INSERT INTO third_insurance_account_date_info(create_by,start_time,end_time,enable_days,enable_months,enable_years,account_id)VALUES
(createBy,now(),getEndDateByDateFun(enableDays,enableMonths,enableYears),enableDays,enableMonths,enableYears,accountId);


END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for showAccountInfoChildLstByPro
-- ----------------------------
DROP PROCEDURE IF EXISTS `showAccountInfoChildLstByPro`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `showAccountInfoChildLstByPro`(IN rootid INT)
BEGIN
CREATE TEMPORARY TABLE IF NOT EXISTS tmpLst (sno INT PRIMARY KEY auto_increment,id VARCHAR(64),depth int);
DELETE FROM tmpLst;
CALL createChildListByPro(rootId,0);
select tmpLst.*,account_info.* from tmpLst,account_info where tmpLst.id=account_info.account_id order by tmpLst.sno;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for getAccountInfoChildList
-- ----------------------------
DROP FUNCTION IF EXISTS `getAccountInfoChildList`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `getAccountInfoChildList`(rootId varchar(100),deep INT) RETURNS varchar(2000) CHARSET utf8
BEGIN   
DECLARE str varchar(2000);  
DECLARE cid varchar(200);   
DECLARE n INT ; 
SET str = '$';   
SET cid = rootId;   
SET n=deep;
WHILE (cid is not null and n>=0) DO   
    SET str = concat(str, ',', cid);   
    SELECT group_concat(account_id) INTO cid FROM account_info where FIND_IN_SET(parent_id, cid) > 0;   
SET n=n-1;
END WHILE;   
RETURN str;   
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for getAccountInfoParentList
-- ----------------------------
DROP FUNCTION IF EXISTS `getAccountInfoParentList`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `getAccountInfoParentList`(rootId varchar(100),deep INT) RETURNS varchar(1000) CHARSET utf8
BEGIN   
DECLARE fid varchar(200) default '';   
DECLARE str varchar(1000) default rootId;   
DECLARE n INT ; 
SET n=deep;
  
WHILE (rootId is not null and n>=1)  do   
    SET fid =(SELECT parent_id FROM account_info WHERE account_id = rootId);   
    IF fid is not null THEN   
        SET str = concat(str, ',', fid);   
        SET rootId = fid;   
    ELSE   
        SET rootId = fid;   
    END IF;  
SET n=n-1; 
END WHILE;   
return str;  
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for getEndDateByDateFun
-- ----------------------------
DROP FUNCTION IF EXISTS `getEndDateByDateFun`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `getEndDateByDateFun`(enableDays INT,enableMonths INT , enableYears INT ) RETURNS varchar(32) CHARSET utf8
BEGIN
DECLARE results VARCHAR(32);	 
SET results=DATE_ADD(DATE_ADD(DATE_ADD(NOW(),INTERVAL enableDays DAY) ,INTERVAL enableMonths MONTH),INTERVAL enableYears YEAR);
 return (results);
END
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_account_role_info_trigger`;
DELIMITER ;;
CREATE TRIGGER `update_account_role_info_trigger` BEFORE UPDATE ON `account_role_info` FOR EACH ROW SET NEW.update_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_action_column_info_trigger`;
DELIMITER ;;
CREATE TRIGGER `update_action_column_info_trigger` BEFORE UPDATE ON `action_column_info` FOR EACH ROW SET NEW.update_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_action_info_trigger`;
DELIMITER ;;
CREATE TRIGGER `update_action_info_trigger` BEFORE UPDATE ON `action_info` FOR EACH ROW SET NEW.update_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_commission_percentage_trigger`;
DELIMITER ;;
CREATE TRIGGER `update_commission_percentage_trigger` BEFORE UPDATE ON `commission_percentage` FOR EACH ROW SET 

NEW.update_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_draw_cash_trigger`;
DELIMITER ;;
CREATE TRIGGER `update_draw_cash_trigger` BEFORE UPDATE ON `draw_cash` FOR EACH ROW SET NEW.update_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_insured_info_trigger`;
DELIMITER ;;
CREATE TRIGGER `update_insured_info_trigger` BEFORE UPDATE ON `insured_info` FOR EACH ROW SET NEW.`update_time` = NOW()
;
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `third_insurance_account_date_info_update_trigger`;
DELIMITER ;;
CREATE TRIGGER `third_insurance_account_date_info_update_trigger` BEFORE UPDATE ON `third_insurance_account_date_info` FOR EACH ROW SET new.update_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_third_insurance_account_info`;
DELIMITER ;;
CREATE TRIGGER `update_third_insurance_account_info` BEFORE UPDATE ON `third_insurance_account_info` FOR EACH ROW SET NEW.`update_time` = NOW()
;;
DELIMITER ;
