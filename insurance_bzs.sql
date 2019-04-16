/*
Navicat MySQL Data Transfer

Source Server         : 1
Source Server Version : 50725
Source Host           : 192.168.1.104:3306
Source Database       : insurance_bzs

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-04-15 18:26:20
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
  `account_state` varchar(32) DEFAULT '0' COMMENT '账号状态 0启用1禁用',
  `login_name` varchar(32) DEFAULT NULL COMMENT '账号登陆名',
  `login_pwd` varchar(64) DEFAULT NULL COMMENT '账号密码',
  `user_name` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `area_code` varchar(32) DEFAULT NULL COMMENT '账号所属区域',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `wechat` varchar(32) DEFAULT NULL COMMENT '微信号',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱号',
  `delete_status` tinyint(4) DEFAULT '0',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `login_time` datetime DEFAULT NULL COMMENT '乐观锁',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账号列表 ';

-- ----------------------------
-- Records of account_info
-- ----------------------------
INSERT INTO `account_info` VALUES ('1', '1', '系统管理员', '', null, '0', 'admin', '123456', 'admin', null, null, null, null, null, '0', null, null, null, '2019-04-15 10:56:15', '2019-04-15 11:19:08');
INSERT INTO `account_info` VALUES ('2', '2', '管理员', '1', null, '0', 'asd', 'asd', 'hh', null, null, null, null, null, '0', null, null, null, '2019-04-15 11:18:52', '2019-04-15 11:19:22');
INSERT INTO `account_info` VALUES ('3', '3', '代理', '1', null, '0', 'qwe', 'qwe', 'qwe', null, null, null, null, null, '0', null, null, null, '2019-04-15 11:19:31', '2019-04-15 11:19:39');

-- ----------------------------
-- Table structure for account_role_info
-- ----------------------------
DROP TABLE IF EXISTS `account_role_info`;
CREATE TABLE `account_role_info` (
  `account_id` int(11) DEFAULT NULL COMMENT '账号id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账号角色关系表 ';

-- ----------------------------
-- Records of account_role_info
-- ----------------------------
INSERT INTO `account_role_info` VALUES ('1', '1');
INSERT INTO `account_role_info` VALUES ('2', '2');
INSERT INTO `account_role_info` VALUES ('3', '3');

-- ----------------------------
-- Table structure for car_info
-- ----------------------------
DROP TABLE IF EXISTS `car_info`;
CREATE TABLE `car_info` (
  `car_info_id` varchar(64) NOT NULL COMMENT 'ID',
  `car_number` varchar(32) DEFAULT NULL COMMENT '车牌号',
  `engine_number` varchar(32) DEFAULT NULL COMMENT '发动机号',
  `frame_number` varchar(1024) DEFAULT NULL COMMENT '车架号',
  `register_date` varchar(128) DEFAULT NULL COMMENT '注册日期',
  `brand_model` varchar(1024) DEFAULT NULL COMMENT '品牌型号',
  `car_model` varchar(1024) DEFAULT NULL COMMENT '车型',
  `purchase_price` int(11) DEFAULT NULL COMMENT '新车购置价',
  `seat_number` int(11) DEFAULT NULL COMMENT '座位数',
  `displacement` decimal(32,10) DEFAULT NULL COMMENT '排量',
  `isTransfer_car` int(11) DEFAULT NULL COMMENT '过户车 0是   1否',
  `isLoan_car` int(11) DEFAULT NULL COMMENT '贷款车 0是   1否',
  `remarks_car` varchar(128) DEFAULT NULL COMMENT '备注',
  `follow_count` int(11) DEFAULT NULL COMMENT '本年跟进次数',
  `follow_time` varchar(32) DEFAULT NULL COMMENT '最后跟进时间',
  `follow_content` varchar(1024) DEFAULT NULL COMMENT '最后跟进内容',
  `plan_return_time` varchar(32) DEFAULT NULL COMMENT '计划回访时间',
  `customer_status` int(11) DEFAULT '0' COMMENT '客户状态 0回访1未回访',
  `customer_type` varchar(32) DEFAULT NULL COMMENT '客户类别',
  `salesman` varchar(32) DEFAULT '0' COMMENT '业务员',
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
  PRIMARY KEY (`car_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车辆信息表 ';

-- ----------------------------
-- Records of car_info
-- ----------------------------
INSERT INTO `car_info` VALUES ('1', '1', '123', 'a', '2019-4-1', '宝马', '奔驰5座', '2054122', '6', '2.0000000000', '1', '1', '哈哈哈', '1', '2019-4-10', '哈哈哈哈哈', '2019-4-20', '0', '0', '0', '', '2019-04-13 11:14:40', '', '2019-04-12 11:39:09', '1', '', '南京', '张三', null, '0', '1', '0', null);
INSERT INTO `car_info` VALUES ('1213', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null, '0', null, '2019-04-12 14:44:01', null, null, null, null, null, null, null, '0', null, '0', null);
INSERT INTO `car_info` VALUES ('2', '2', '456', 'b', '2019-4-1', '宝马', '奔驰5座', '2054122', '6', '2.0000000000', '1', '1', '哈哈哈', '1', '2019-4-10', '哈哈哈哈哈', '2019-4-20', '0', '0', '1', '', '2019-04-13 11:14:40', '', '2019-04-12 11:39:10', '1', '', '南京', '李四', null, '0', '1', '0', null);
INSERT INTO `car_info` VALUES ('20190412133426574117', '苏A00VC9', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null, '0', '20190412133426574117', '2019-04-12 13:36:00', null, null, null, null, null, null, null, '0', null, '0', null);
INSERT INTO `car_info` VALUES ('20190412135138390401', '苏A00VC9', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null, '0', '20190412135138390401', '2019-04-12 13:53:11', null, null, null, null, null, null, null, '0', null, '0', null);
INSERT INTO `car_info` VALUES ('20190415132755756716', '苏AW961B', 'E30A034819', 'LS4A AB3R 4EG1 4656 8', '2014-05-14', null, 'SC6406AYA4', null, null, null, null, null, null, null, null, null, null, '0', null, '0', '20190415132755756716', '2019-04-15 13:29:34', null, null, null, null, null, '刘景龙', '37082619850502003X', '1', null, '0', '13770699784');
INSERT INTO `car_info` VALUES ('20190415134318664951', '苏AW961B', 'E30A034819', 'LS4A AB3R 4EG1 4656 8', '2014-05-14', null, 'SC6406AYA4', null, null, null, null, null, null, null, null, null, null, '0', null, '0', '20190415134318664951', '2019-04-15 13:44:57', null, null, null, null, null, '刘景龙', '37082619850502003X', '1', null, '0', '13770699784');
INSERT INTO `car_info` VALUES ('20190415152102631794', '苏AW961B', 'E30A034819', 'LS4A AB3R 4EG1 4656 8', '2014-05-14', null, 'SC6406AYA4', null, null, null, null, null, null, null, null, null, null, '0', null, '0', '20190415152102631794', '2019-04-15 15:22:41', null, null, null, null, null, '刘景龙', '37082619850502003X', '1', null, '0', '13770699784');
INSERT INTO `car_info` VALUES ('20190415154046492359', '苏AW961B', 'E30A034819', 'LS4A AB3R 4EG1 4656 8', '2014-05-14', null, 'SC6406AYA4', null, null, null, null, null, null, null, null, null, null, '0', null, '0', '20190415154046492359', '2019-04-15 15:42:25', null, null, null, null, null, '刘景龙', '37082619850502003X', '1', null, '0', '13770699784');
INSERT INTO `car_info` VALUES ('20190415155620897521', null, 'E30A034819', 'LS4A AB3R 4EG1 4656 8', '2014-05-14', null, 'SC6406AYA4', null, null, null, null, null, null, null, null, null, null, '0', null, '0', '20190415155620897521', '2019-04-15 15:58:04', null, null, null, null, null, '刘景龙', '37082619850502003X', '1', null, '0', '13770699784');
INSERT INTO `car_info` VALUES ('3', '3', '789', 'c', '2019-4-1', '宝马', '奔驰5座', '2054122', '6', '2.0000000000', '1', '1', '哈哈哈', '1', '2019-4-10', '哈哈哈哈哈', '2019-4-20', '1', '0', '1', '', '2019-04-13 11:14:40', '', '2019-04-12 11:39:11', '1', '', '南京', '王二', null, '01', '1', '0', null);
INSERT INTO `car_info` VALUES ('4', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null, '0', null, '2019-04-12 13:33:57', null, '2019-04-12 14:06:45', null, null, null, null, null, '0', null, '1', null);
INSERT INTO `car_info` VALUES ('5', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null, '0', null, '2019-04-12 13:33:59', null, '2019-04-12 14:06:45', null, null, null, null, null, '0', '0', '1', null);
INSERT INTO `car_info` VALUES ('6', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null, '0', null, '2019-04-12 13:34:01', null, '2019-04-12 14:06:45', null, null, null, null, null, '0', null, '1', null);

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
  `is_first_time` varchar(10) DEFAULT '0' COMMENT '是否首次查询:默认0 不是 1是',
  `check_type` varchar(4) DEFAULT '0' COMMENT '查询方式0车牌查询1车架查询',
  `send_time` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`check_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of check_info
-- ----------------------------
INSERT INTO `check_info` VALUES ('1', '1', '1', '2019-04-12 10:02:16', null, '0', null, null);
INSERT INTO `check_info` VALUES ('1w', '1', '1', '2019-04-12 14:00:28', null, '0', '1', null);
INSERT INTO `check_info` VALUES ('2', '1', '2', '2019-04-12 10:02:22', null, '0', null, null);
INSERT INTO `check_info` VALUES ('20190412133426574117', null, null, '2019-04-12 13:36:00', null, '0', null, null);
INSERT INTO `check_info` VALUES ('20190412135138390401', null, null, '2019-04-12 13:53:11', '2019-04-12 14:02:28', '1', null, null);
INSERT INTO `check_info` VALUES ('20190415132755756716', '20190415132755756716', '20190415132755756716', '2019-04-15 13:29:34', null, '0', null, '2019-04-15 13:29:21');
INSERT INTO `check_info` VALUES ('20190415134318664951', '20190415134318664951', '20190415134318664951', '2019-04-15 13:44:57', null, '0', null, '2019-04-15 13:44:44');
INSERT INTO `check_info` VALUES ('20190415152102631794', '20190415152102631794', '20190415152102631794', '2019-04-15 15:22:41', null, '0', null, '2019-04-15 15:22:28');
INSERT INTO `check_info` VALUES ('20190415154046492359', '20190415154046492359', '20190415154046492359', '2019-04-15 15:42:25', null, '0', null, '2019-04-15 15:42:11');
INSERT INTO `check_info` VALUES ('20190415155620897521', '20190415155620897521', '20190415155620897521', '2019-04-15 15:58:04', null, '0', null, '2019-04-15 15:57:21');
INSERT INTO `check_info` VALUES ('222', null, null, '2019-04-15 13:34:26', null, '0', null, null);
INSERT INTO `check_info` VALUES ('3', '1', '3', '2019-04-12 10:34:23', null, '0', null, null);
INSERT INTO `check_info` VALUES ('`12', null, '4', '2019-04-12 11:36:58', null, '0', null, null);

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
INSERT INTO `customer` VALUES ('1', 'hh', '155', null, null, null, null, null, null, null, null, '2019-04-12 10:18:30', null, null, null);

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
  `insurance_type_id` varchar(64) NOT NULL COMMENT 'id',
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `insurance_name` varchar(32) DEFAULT NULL COMMENT '承保险种名称',
  `insurance_amount` decimal(32,4) DEFAULT '0.0000' COMMENT '保额',
  `insurance_premium` decimal(32,4) DEFAULT '0.0000' COMMENT '保费',
  `info_type` varchar(32) DEFAULT NULL COMMENT '类型 0投保1报价（0）',
  `type_id` varchar(64) DEFAULT NULL COMMENT '类型id info_type=0表示投保id，info_type=1表示报价id',
  `excluding_deductible` decimal(32,4) DEFAULT '0.0000' COMMENT '不计免',
  `send_time` varchar(32) DEFAULT NULL COMMENT '请求发送日期',
  PRIMARY KEY (`insurance_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='保险分类信息 ';

-- ----------------------------
-- Records of insurance_type_info
-- ----------------------------
INSERT INTO `insurance_type_info` VALUES ('20190415134318665221', null, '20190415134318664951', '2019-04-15 13:44:57', null, null, '机动车损失保险', '1000000.0000', '1839.0000', '0', '20190415134318664951', '94.9600', null);
INSERT INTO `insurance_type_info` VALUES ('20190415152102632166', null, '20190415152102631794', '2019-04-15 15:22:41', null, null, '机动车损失保险', '1000000.0000', '1839.0000', '0', '20190415152102631794', '94.9600', null);
INSERT INTO `insurance_type_info` VALUES ('20190415154046492191', null, '20190415154046492359', '2019-04-15 15:42:25', null, null, '机动车损失保险', '1000000.0000', '1839.0000', '0', '20190415154046492359', '94.9600', null);
INSERT INTO `insurance_type_info` VALUES ('20190415155625983841', null, '20190415155620897521', '2019-04-15 15:58:04', null, null, '机动车损失保险', '1000000.0000', '1839.0000', '0', '20190415155620897521', '94.9600', null);

-- ----------------------------
-- Table structure for insured_info
-- ----------------------------
DROP TABLE IF EXISTS `insured_info`;
CREATE TABLE `insured_info` (
  `insured_id` varchar(64) NOT NULL COMMENT 'id',
  `create_id` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT NULL COMMENT '更新人',
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
  `insured__id_card` int(11) DEFAULT NULL COMMENT '被保险人证件号码',
  `insured_id_card_type` int(11) DEFAULT NULL COMMENT '被保险人证件类型',
  `busines_number` int(11) DEFAULT NULL COMMENT '商业险保单号',
  `traffic_number` int(11) DEFAULT NULL COMMENT '交强险保单号',
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
INSERT INTO `insured_info` VALUES ('1', null, '2019-04-10 14:15:48', null, '2019-04-12 14:54:00', null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null, null, null, '人保车险', null, null, null, null, null, null, null, null, '1', '0');
INSERT INTO `insured_info` VALUES ('20190412133426574117', '20190412133426574117', '2019-04-12 13:36:00', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0');
INSERT INTO `insured_info` VALUES ('20190412135138390401', '20190412135138390401', '2019-04-12 13:53:11', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0');
INSERT INTO `insured_info` VALUES ('20190415132755756716', '20190415132755756716', '2019-04-15 13:29:34', null, null, null, null, null, null, null, '2019-05-11 00:00', '2019-05-11 00:00', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '20190415132755756716', '0');
INSERT INTO `insured_info` VALUES ('20190415134318664951', '20190415134318664951', '2019-04-15 13:44:57', null, null, null, null, null, null, null, '2019-05-11 00:00', '2019-05-11 00:00', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '20190415134318664951', '0');
INSERT INTO `insured_info` VALUES ('20190415152102631794', '20190415152048745807', '2019-04-15 15:22:41', null, null, null, null, null, null, null, '2019-05-11 00:00', '2019-05-11 00:00', null, null, '太平洋保险', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '20190415152102631794', '0');
INSERT INTO `insured_info` VALUES ('20190415154046492359', '20190415154032291306', '2019-04-15 15:42:25', null, null, null, null, null, null, null, '2019-05-11 00:00', '2019-05-11 00:00', null, null, '太平洋保险', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '20190415154046492359', '0');
INSERT INTO `insured_info` VALUES ('20190415155620897521', '20190415155535607395', '2019-04-15 15:58:04', null, null, null, null, null, null, null, '2019-05-11 00:00', '2019-05-11 00:00', null, null, '太平洋保险', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '20190415155620897521', '0');

-- ----------------------------
-- Table structure for menu_info
-- ----------------------------
DROP TABLE IF EXISTS `menu_info`;
CREATE TABLE `menu_info` (
  `MENU_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `PARENT_ID` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `MENU_NAME` varchar(50) NOT NULL COMMENT '菜单/按钮名称',
  `URL` varchar(100) DEFAULT NULL COMMENT '菜单URL',
  `PERMS` text COMMENT '权限标识',
  `ICON` varchar(50) DEFAULT NULL COMMENT '图标',
  `TYPE` char(2) NOT NULL COMMENT '类型 0菜单 1按钮',
  `ORDER_NUM` bigint(20) DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`MENU_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu_info
-- ----------------------------
INSERT INTO `menu_info` VALUES ('1', '0', '系统管理', null, null, 'zmdi zmdi-settings', '0', '1', '2017-12-27 16:39:07', null);
INSERT INTO `menu_info` VALUES ('2', '0', '系统监控', null, null, 'zmdi zmdi-shield-security', '0', '2', '2017-12-27 16:45:51', '2018-01-17 17:08:28');
INSERT INTO `menu_info` VALUES ('3', '1', '用户管理', 'user', 'user:list', '', '0', '1', '2017-12-27 16:47:13', '2018-04-25 09:00:01');
INSERT INTO `menu_info` VALUES ('4', '1', '角色管理', 'role', 'role:list', '', '0', '2', '2017-12-27 16:48:09', '2018-04-25 09:01:12');
INSERT INTO `menu_info` VALUES ('5', '1', '菜单管理', 'menu', 'menu:list', '', '0', '3', '2017-12-27 16:48:57', '2018-04-25 09:01:30');
INSERT INTO `menu_info` VALUES ('6', '1', '部门管理', 'dept', 'dept:list', '', '0', '4', '2017-12-27 16:57:33', '2018-04-25 09:01:40');
INSERT INTO `menu_info` VALUES ('8', '2', '在线用户', 'session', 'session:list', '', '0', '1', '2017-12-27 16:59:33', '2018-04-25 09:02:04');
INSERT INTO `menu_info` VALUES ('10', '2', '系统日志', 'log', 'log:list', '', '0', '3', '2017-12-27 17:00:50', '2018-04-25 09:02:18');
INSERT INTO `menu_info` VALUES ('11', '3', '新增用户', null, 'user:add', null, '1', null, '2017-12-27 17:02:58', null);
INSERT INTO `menu_info` VALUES ('12', '3', '修改用户', null, 'user:update', null, '1', null, '2017-12-27 17:04:07', null);
INSERT INTO `menu_info` VALUES ('13', '3', '删除用户', null, 'user:delete', null, '1', null, '2017-12-27 17:04:58', null);
INSERT INTO `menu_info` VALUES ('14', '4', '新增角色', null, 'role:add', null, '1', null, '2017-12-27 17:06:38', null);
INSERT INTO `menu_info` VALUES ('15', '4', '修改角色', null, 'role:update', null, '1', null, '2017-12-27 17:06:38', null);
INSERT INTO `menu_info` VALUES ('16', '4', '删除角色', null, 'role:delete', null, '1', null, '2017-12-27 17:06:38', null);
INSERT INTO `menu_info` VALUES ('17', '5', '新增菜单', null, 'menu:add', null, '1', null, '2017-12-27 17:08:02', null);
INSERT INTO `menu_info` VALUES ('18', '5', '修改菜单', null, 'menu:update', null, '1', null, '2017-12-27 17:08:02', null);
INSERT INTO `menu_info` VALUES ('19', '5', '删除菜单', null, 'menu:delete', null, '1', null, '2017-12-27 17:08:02', null);
INSERT INTO `menu_info` VALUES ('20', '6', '新增部门', null, 'dept:add', null, '1', null, '2017-12-27 17:09:24', null);
INSERT INTO `menu_info` VALUES ('21', '6', '修改部门', null, 'dept:update', null, '1', null, '2017-12-27 17:09:24', null);
INSERT INTO `menu_info` VALUES ('22', '6', '删除部门', null, 'dept:delete', null, '1', null, '2017-12-27 17:09:24', null);
INSERT INTO `menu_info` VALUES ('23', '8', '踢出用户', null, 'user:kickout', null, '1', null, '2017-12-27 17:11:13', null);
INSERT INTO `menu_info` VALUES ('24', '10', '删除日志', null, 'log:delete', null, '1', null, '2017-12-27 17:11:45', null);
INSERT INTO `menu_info` VALUES ('58', '0', '网络资源', null, null, 'zmdi zmdi-globe-alt', '0', null, '2018-01-12 15:28:48', '2018-01-22 19:49:26');
INSERT INTO `menu_info` VALUES ('59', '58', '天气查询', 'weather', 'weather:list', '', '0', null, '2018-01-12 15:40:02', '2018-04-25 09:02:57');
INSERT INTO `menu_info` VALUES ('61', '58', '每日一文', 'article', 'article:list', '', '0', null, '2018-01-15 17:17:14', '2018-04-25 09:03:08');
INSERT INTO `menu_info` VALUES ('64', '1', '字典管理', 'dict', 'dict:list', '', '0', null, '2018-01-18 10:38:25', '2018-04-25 09:01:50');
INSERT INTO `menu_info` VALUES ('65', '64', '新增字典', null, 'dict:add', null, '1', null, '2018-01-18 19:10:08', null);
INSERT INTO `menu_info` VALUES ('66', '64', '修改字典', null, 'dict:update', null, '1', null, '2018-01-18 19:10:27', null);
INSERT INTO `menu_info` VALUES ('67', '64', '删除字典', null, 'dict:delete', null, '1', null, '2018-01-18 19:10:47', null);
INSERT INTO `menu_info` VALUES ('81', '58', '影视资讯', null, null, null, '0', null, '2018-01-22 14:12:59', null);
INSERT INTO `menu_info` VALUES ('82', '81', '正在热映', 'movie/hot', 'movie:hot', '', '0', null, '2018-01-22 14:13:47', '2018-04-25 09:03:48');
INSERT INTO `menu_info` VALUES ('83', '81', '即将上映', 'movie/coming', 'movie:coming', '', '0', null, '2018-01-22 14:14:36', '2018-04-25 09:04:05');
INSERT INTO `menu_info` VALUES ('86', '58', 'One 一个', null, null, null, '0', null, '2018-01-26 09:42:41', '2018-01-26 09:43:46');
INSERT INTO `menu_info` VALUES ('87', '86', '绘画', 'one/painting', 'one:painting', '', '0', null, '2018-01-26 09:47:14', '2018-04-25 09:04:17');
INSERT INTO `menu_info` VALUES ('88', '86', '语文', 'one/yuwen', 'one:yuwen', '', '0', null, '2018-01-26 09:47:40', '2018-04-25 09:04:30');
INSERT INTO `menu_info` VALUES ('89', '86', '散文', 'one/essay', 'one:essay', '', '0', null, '2018-01-26 09:48:05', '2018-04-25 09:04:42');
INSERT INTO `menu_info` VALUES ('101', '0', '任务调度', null, null, 'zmdi zmdi-alarm', '0', null, '2018-02-24 15:52:57', null);
INSERT INTO `menu_info` VALUES ('102', '101', '定时任务', 'job', 'job:list', '', '0', null, '2018-02-24 15:53:53', '2018-04-25 09:05:12');
INSERT INTO `menu_info` VALUES ('103', '102', '新增任务', null, 'job:add', null, '1', null, '2018-02-24 15:55:10', null);
INSERT INTO `menu_info` VALUES ('104', '102', '修改任务', null, 'job:update', null, '1', null, '2018-02-24 15:55:53', null);
INSERT INTO `menu_info` VALUES ('105', '102', '删除任务', null, 'job:delete', null, '1', null, '2018-02-24 15:56:18', null);
INSERT INTO `menu_info` VALUES ('106', '102', '暂停任务', null, 'job:pause', null, '1', null, '2018-02-24 15:57:08', null);
INSERT INTO `menu_info` VALUES ('107', '102', '恢复任务', null, 'job:resume', null, '1', null, '2018-02-24 15:58:21', null);
INSERT INTO `menu_info` VALUES ('108', '102', '立即执行任务', null, 'job:run', null, '1', null, '2018-02-24 15:59:45', null);
INSERT INTO `menu_info` VALUES ('109', '101', '调度日志', 'jobLog', 'jobLog:list', '', '0', null, '2018-02-24 16:00:45', '2018-04-25 09:05:25');
INSERT INTO `menu_info` VALUES ('110', '109', '删除日志', null, 'jobLog:delete', null, '1', null, '2018-02-24 16:01:21', null);
INSERT INTO `menu_info` VALUES ('113', '2', 'Redis监控', 'redis/info', 'redis:list', '', '0', null, '2018-06-28 14:29:42', null);
INSERT INTO `menu_info` VALUES ('114', '2', 'Redis终端', 'redis/terminal', 'redis:terminal', '', '0', null, '2018-06-28 15:35:21', null);

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `order_id` varchar(64) NOT NULL COMMENT '订单号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单生成日期',
  `finish_time` datetime DEFAULT NULL COMMENT '订单完成日期',
  `account_id` varchar(64) DEFAULT NULL COMMENT '创建者id',
  `pay_status` int(4) DEFAULT '0' COMMENT '支付状态0待支付,1完成2取消3退款4支付超时5支付失败',
  `pay_type` varchar(4) DEFAULT NULL COMMENT '支付用途：1购买账号,2保单',
  `payment` varchar(4) DEFAULT NULL COMMENT '支付方式1支付宝2微信3pos',
  `pay_money` decimal(10,4) DEFAULT NULL COMMENT '支付金额',
  `car_info_id` varchar(64) DEFAULT NULL,
  `delivery_way` int(4) DEFAULT NULL,
  `delivery_address` varchar(64) DEFAULT NULL,
  `contact_name` varchar(11) DEFAULT NULL,
  `contact_tel` int(11) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES ('1', '2019-04-12 14:50:37', null, '1', '0', null, null, null, '1', null, null, null, null);

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
-- Table structure for privilege_info
-- ----------------------------
DROP TABLE IF EXISTS `privilege_info`;
CREATE TABLE `privilege_info` (
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `privilege_id` varchar(64) NOT NULL COMMENT 'id',
  `name` varchar(32) DEFAULT NULL COMMENT '权限名称',
  `code` varchar(32) DEFAULT NULL COMMENT '权限代码 0超级管理员1省管理员2市管理员4区管理员8部门管理员16团队管理员，使用2的次幂',
  `p_des` varchar(128) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`privilege_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表 ';

-- ----------------------------
-- Records of privilege_info
-- ----------------------------
INSERT INTO `privilege_info` VALUES (null, null, '2019-04-15 11:14:05', null, '2019-04-15 11:15:02', '100', '客户列表', null, null);
INSERT INTO `privilege_info` VALUES (null, null, '2019-04-15 11:14:15', null, '2019-04-15 11:15:04', '200', '订单列表', '', null);
INSERT INTO `privilege_info` VALUES (null, null, '2019-04-15 11:14:23', null, '2019-04-15 11:15:06', '300', '客户', null, null);

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
  `quote_status` int(11) DEFAULT NULL COMMENT '报价状态 报价状态，-1=未报价， 0=报价失败，>0报价成功',
  `quote_result` text COMMENT '报价信息',
  `repeat_submit_result` varchar(32) DEFAULT NULL COMMENT '是否重复投保',
  `submit_status` int(11) DEFAULT '-1' COMMENT '核保状态 核保状态-1未核保 0=核保失败， 1=核保成功, 2=未到期未核保（无需再核保）,',
  `SubmitResult` text COMMENT '核保状态描述 核保状态描述（备注字符最大长度：1000）',
  `force_rate` varchar(32) DEFAULT NULL COMMENT '交强车船险费率 交强车船险费率（核保通过之后才会有值）',
  `no_reparation_sale_rate` varchar(32) DEFAULT NULL COMMENT '无赔偿优惠系数 无赔偿优惠系数',
  `independent_channel_date` varchar(32) DEFAULT NULL COMMENT '自主渠道系数',
  `independent_submit_rate` varchar(32) DEFAULT NULL COMMENT '自主核保系数',
  `traffic_illegal_rate` varchar(32) DEFAULT NULL COMMENT '交通违法活动系数',
  `discount_rate` varchar(32) DEFAULT NULL COMMENT '折扣系数',
  `quote_channel` varchar(32) DEFAULT NULL COMMENT '报价渠道',
  `car_used_type` varchar(32) DEFAULT NULL COMMENT '车辆使用性质 使用性质 1：家庭自用车（默认）， 2：党政机关、事业团体， 3：非营业企业客车， 6：营业货车， 7：非营业货车',
  `car_model` varchar(128) DEFAULT NULL COMMENT '车辆型号',
  `biz_total` decimal(32,8) DEFAULT NULL COMMENT '商业险保费合计',
  `force_total` decimal(32,8) DEFAULT NULL COMMENT '交强险保费合计',
  `tax_total` decimal(32,8) DEFAULT NULL COMMENT '车船税',
  `total` decimal(32,8) DEFAULT NULL COMMENT '保费总额',
  `quote_insurance_name` varchar(32) DEFAULT NULL COMMENT '报价保司名称',
  `quote_source` varchar(32) DEFAULT NULL COMMENT '报价保司枚举值 1太保2平安4人保',
  `biz_no` varchar(64) DEFAULT NULL COMMENT '商业险保单号',
  `force_no` varchar(64) DEFAULT NULL COMMENT '交强险保单号',
  `status` int(2) DEFAULT '0',
  PRIMARY KEY (`quote_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报价信息 ';

-- ----------------------------
-- Records of quote_info
-- ----------------------------
INSERT INTO `quote_info` VALUES (null, null, null, null, null, '1', '1', '1', null, null, '-1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `quote_info` VALUES (null, null, null, null, null, '2', '2', '1', null, null, '-1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `quote_info` VALUES (null, null, null, null, null, '3', '3', '1', null, null, '-1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for role_info
-- ----------------------------
DROP TABLE IF EXISTS `role_info`;
CREATE TABLE `role_info` (
  `ROLE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `ROLE_NAME` varchar(100) NOT NULL COMMENT '角色名称',
  `REMARK` varchar(100) DEFAULT NULL COMMENT '角色描述',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_info
-- ----------------------------
INSERT INTO `role_info` VALUES ('1', '管理员', '管理员', '2017-12-27 16:23:11', '2018-02-24 16:01:45');
INSERT INTO `role_info` VALUES ('2', '测试账号', '测试账号', '2017-12-27 16:25:09', '2018-01-23 09:11:11');
INSERT INTO `role_info` VALUES ('3', '注册账户', '注册账户，只可查看，不可操作', '2017-12-29 16:00:15', '2018-02-24 17:33:45');
INSERT INTO `role_info` VALUES ('23', '用户管理员', '负责用户的增删改操作', '2018-01-09 15:32:41', null);
INSERT INTO `role_info` VALUES ('24', '系统监控员', '可查看系统监控信息，但不可操作', '2018-01-09 15:52:01', '2018-03-07 19:05:33');
INSERT INTO `role_info` VALUES ('25', '用户查看', '查看用户，无相应操作权限', '2018-01-09 15:56:30', null);
INSERT INTO `role_info` VALUES ('63', '影院工作者', '可查看影视信息', '2018-02-06 08:48:28', '2018-03-07 19:05:26');
INSERT INTO `role_info` VALUES ('64', '天气预报员', '可查看天气预报信息', '2018-02-27 08:47:04', null);
INSERT INTO `role_info` VALUES ('65', '文章审核', '文章类', '2018-02-27 08:48:01', '2018-03-13 11:20:34');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID',
  `MENU_ID` bigint(20) NOT NULL COMMENT '菜单/按钮ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES ('3', '58');
INSERT INTO `role_menu` VALUES ('3', '59');
INSERT INTO `role_menu` VALUES ('3', '61');
INSERT INTO `role_menu` VALUES ('3', '81');
INSERT INTO `role_menu` VALUES ('3', '82');
INSERT INTO `role_menu` VALUES ('3', '83');
INSERT INTO `role_menu` VALUES ('3', '86');
INSERT INTO `role_menu` VALUES ('3', '87');
INSERT INTO `role_menu` VALUES ('3', '88');
INSERT INTO `role_menu` VALUES ('3', '89');
INSERT INTO `role_menu` VALUES ('3', '1');
INSERT INTO `role_menu` VALUES ('3', '3');
INSERT INTO `role_menu` VALUES ('3', '4');
INSERT INTO `role_menu` VALUES ('3', '5');
INSERT INTO `role_menu` VALUES ('3', '6');
INSERT INTO `role_menu` VALUES ('3', '64');
INSERT INTO `role_menu` VALUES ('3', '2');
INSERT INTO `role_menu` VALUES ('3', '8');
INSERT INTO `role_menu` VALUES ('3', '10');
INSERT INTO `role_menu` VALUES ('3', '101');
INSERT INTO `role_menu` VALUES ('3', '102');
INSERT INTO `role_menu` VALUES ('3', '109');
INSERT INTO `role_menu` VALUES ('63', '58');
INSERT INTO `role_menu` VALUES ('63', '81');
INSERT INTO `role_menu` VALUES ('63', '82');
INSERT INTO `role_menu` VALUES ('63', '83');
INSERT INTO `role_menu` VALUES ('24', '8');
INSERT INTO `role_menu` VALUES ('24', '2');
INSERT INTO `role_menu` VALUES ('24', '10');
INSERT INTO `role_menu` VALUES ('65', '86');
INSERT INTO `role_menu` VALUES ('65', '88');
INSERT INTO `role_menu` VALUES ('65', '89');
INSERT INTO `role_menu` VALUES ('65', '58');
INSERT INTO `role_menu` VALUES ('65', '61');
INSERT INTO `role_menu` VALUES ('2', '81');
INSERT INTO `role_menu` VALUES ('2', '61');
INSERT INTO `role_menu` VALUES ('2', '24');
INSERT INTO `role_menu` VALUES ('2', '82');
INSERT INTO `role_menu` VALUES ('2', '83');
INSERT INTO `role_menu` VALUES ('2', '58');
INSERT INTO `role_menu` VALUES ('2', '59');
INSERT INTO `role_menu` VALUES ('2', '2');
INSERT INTO `role_menu` VALUES ('2', '8');
INSERT INTO `role_menu` VALUES ('2', '10');
INSERT INTO `role_menu` VALUES ('23', '11');
INSERT INTO `role_menu` VALUES ('23', '12');
INSERT INTO `role_menu` VALUES ('23', '13');
INSERT INTO `role_menu` VALUES ('23', '3');
INSERT INTO `role_menu` VALUES ('23', '1');
INSERT INTO `role_menu` VALUES ('25', '1');
INSERT INTO `role_menu` VALUES ('25', '3');
INSERT INTO `role_menu` VALUES ('1', '59');
INSERT INTO `role_menu` VALUES ('1', '2');
INSERT INTO `role_menu` VALUES ('1', '3');
INSERT INTO `role_menu` VALUES ('1', '67');
INSERT INTO `role_menu` VALUES ('1', '1');
INSERT INTO `role_menu` VALUES ('1', '4');
INSERT INTO `role_menu` VALUES ('1', '5');
INSERT INTO `role_menu` VALUES ('1', '6');
INSERT INTO `role_menu` VALUES ('1', '20');
INSERT INTO `role_menu` VALUES ('1', '21');
INSERT INTO `role_menu` VALUES ('1', '22');
INSERT INTO `role_menu` VALUES ('1', '10');
INSERT INTO `role_menu` VALUES ('1', '8');
INSERT INTO `role_menu` VALUES ('1', '58');
INSERT INTO `role_menu` VALUES ('1', '66');
INSERT INTO `role_menu` VALUES ('1', '11');
INSERT INTO `role_menu` VALUES ('1', '12');
INSERT INTO `role_menu` VALUES ('1', '64');
INSERT INTO `role_menu` VALUES ('1', '13');
INSERT INTO `role_menu` VALUES ('1', '14');
INSERT INTO `role_menu` VALUES ('1', '65');
INSERT INTO `role_menu` VALUES ('1', '15');
INSERT INTO `role_menu` VALUES ('1', '16');
INSERT INTO `role_menu` VALUES ('1', '17');
INSERT INTO `role_menu` VALUES ('1', '18');
INSERT INTO `role_menu` VALUES ('1', '23');
INSERT INTO `role_menu` VALUES ('1', '81');
INSERT INTO `role_menu` VALUES ('1', '82');
INSERT INTO `role_menu` VALUES ('1', '83');
INSERT INTO `role_menu` VALUES ('1', '19');
INSERT INTO `role_menu` VALUES ('1', '24');
INSERT INTO `role_menu` VALUES ('1', '61');
INSERT INTO `role_menu` VALUES ('1', '86');
INSERT INTO `role_menu` VALUES ('1', '87');
INSERT INTO `role_menu` VALUES ('1', '88');
INSERT INTO `role_menu` VALUES ('1', '89');
INSERT INTO `role_menu` VALUES ('1', '101');
INSERT INTO `role_menu` VALUES ('1', '102');
INSERT INTO `role_menu` VALUES ('1', '103');
INSERT INTO `role_menu` VALUES ('1', '104');
INSERT INTO `role_menu` VALUES ('1', '105');
INSERT INTO `role_menu` VALUES ('1', '106');
INSERT INTO `role_menu` VALUES ('1', '107');
INSERT INTO `role_menu` VALUES ('1', '108');
INSERT INTO `role_menu` VALUES ('1', '109');
INSERT INTO `role_menu` VALUES ('1', '110');
INSERT INTO `role_menu` VALUES ('64', '59');
INSERT INTO `role_menu` VALUES ('64', '58');
INSERT INTO `role_menu` VALUES ('1', '113');
INSERT INTO `role_menu` VALUES ('1', '114');

-- ----------------------------
-- Table structure for role_privilege_info
-- ----------------------------
DROP TABLE IF EXISTS `role_privilege_info`;
CREATE TABLE `role_privilege_info` (
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `role_privilege_id` varchar(64) NOT NULL COMMENT 'id',
  `role_id` varchar(64) DEFAULT NULL COMMENT '角色id',
  `privilege_id` varchar(64) DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`role_privilege_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-权限关联表 ';

-- ----------------------------
-- Records of role_privilege_info
-- ----------------------------
INSERT INTO `role_privilege_info` VALUES (null, null, '2019-04-15 11:15:16', null, null, '1', '1', '100');
INSERT INTO `role_privilege_info` VALUES (null, null, '2019-04-15 11:15:21', null, null, '2', '2', '200');
INSERT INTO `role_privilege_info` VALUES (null, null, '2019-04-15 11:15:31', null, null, '3', '1', '200');
INSERT INTO `role_privilege_info` VALUES (null, null, '2019-04-15 11:15:37', null, null, '4', '1', '300');
INSERT INTO `role_privilege_info` VALUES (null, null, '2019-04-15 11:15:43', null, null, '5', '2', '200');
INSERT INTO `role_privilege_info` VALUES (null, null, '2019-04-15 11:15:49', null, null, '6', '3', '200');

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
  `status` varchar(4) DEFAULT NULL COMMENT '状态,是否可用0禁用1启用',
  `is_pay` varchar(4) DEFAULT NULL COMMENT '是否支付成功,0未支付1支付',
  PRIMARY KEY (`third_insurance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方保险公司账号,用于续保报价 ';

-- ----------------------------
-- Records of third_insurance_account_info
-- ----------------------------

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
DROP TRIGGER IF EXISTS `update_check_info_trigger`;
DELIMITER ;;
CREATE TRIGGER `update_check_info_trigger` BEFORE UPDATE ON `check_info` FOR EACH ROW SET NEW.update_time=NOW()
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_insured_info_trigger`;
DELIMITER ;;
CREATE TRIGGER `update_insured_info_trigger` BEFORE UPDATE ON `insured_info` FOR EACH ROW SET NEW.`update_time` = NOW()
;
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_third_insurance_account_info`;
DELIMITER ;;
CREATE TRIGGER `update_third_insurance_account_info` BEFORE UPDATE ON `third_insurance_account_info` FOR EACH ROW SET NEW.`update_time` = NOW()
;;
DELIMITER ;
