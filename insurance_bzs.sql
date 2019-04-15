/*
Navicat MySQL Data Transfer

Source Server         : 1
Source Server Version : 50725
Source Host           : 192.168.1.102:3306
Source Database       : insurance_bzs

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-04-11 17:16:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account_info
-- ----------------------------
DROP TABLE IF EXISTS `account_info`;
CREATE TABLE `account_info` (
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` int(11) DEFAULT NULL COMMENT '父级id',
  `area_code` varchar(32) DEFAULT NULL COMMENT '账号所属区域',
  `account_state` varchar(32) DEFAULT '0' COMMENT '账号状态 0启用1禁用',
  `login_name` varchar(32) DEFAULT NULL COMMENT '账号登陆名',
  `pwd` varchar(64) DEFAULT NULL COMMENT '账号密码',
  `user_name` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `wechat` varchar(32) DEFAULT NULL COMMENT '微信号',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱号',
  `ancestor_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='账号列表 ';

-- ----------------------------
-- Records of account_info
-- ----------------------------
INSERT INTO `account_info` VALUES ('1', '1', null, '1', null, '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', null);
INSERT INTO `account_info` VALUES ('2', '2', null, null, null, '2', null, null, '0', null, null, null, null, null, null, null, null);
INSERT INTO `account_info` VALUES ('3', '3', null, null, null, '3', null, null, '0', null, null, null, null, null, null, null, null);
INSERT INTO `account_info` VALUES ('4', '22', '2019-04-11 16:52:55', null, '2019-04-11 16:52:59', '4', null, null, '0', null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for account_role_info
-- ----------------------------
DROP TABLE IF EXISTS `account_role_info`;
CREATE TABLE `account_role_info` (
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account_id` int(11) DEFAULT NULL COMMENT '账号id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账号角色关系表 ';

-- ----------------------------
-- Records of account_role_info
-- ----------------------------

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
  `customer_status` int(11) DEFAULT NULL COMMENT '客户状态 0回访1未回访',
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
  PRIMARY KEY (`car_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车辆信息表 ';

-- ----------------------------
-- Records of car_info
-- ----------------------------
INSERT INTO `car_info` VALUES ('1', '苏A99C3G', '453684F', 'JKAHDUASGDHKWBA', '2019-4-1', '宝马', '奔驰5座', '2054122', '6', '2.0000000000', '1', '1', '哈哈哈', '1', '2019-4-10', '哈哈哈哈哈', '2019-4-20', '0', '0', '1', '', '2019-04-13 11:14:40', '', '2019-04-15 00:00:00', '1', '', '南京', '张三', null, '0', null);
INSERT INTO `car_info` VALUES ('2', '苏A99C3G', '453684F', 'JKAHDUASGDHKWBA', '2019-4-1', '宝马', '奔驰5座', '2054122', '6', '2.0000000000', '1', '1', '哈哈哈', '1', '2019-4-10', '哈哈哈哈哈', '2019-4-20', '0', '0', '1', '', '2019-04-13 11:14:40', '', '2019-04-15 00:00:00', '1', '', '南京', '李四', null, '0', null);
INSERT INTO `car_info` VALUES ('3', '苏A99C3G', '453684F', 'JKAHDUASGDHKWBA', '2019-4-1', '宝马', '奔驰5座', '2054122', '6', '2.0000000000', '1', '1', '哈哈哈', '1', '2019-4-10', '哈哈哈哈哈', '2019-4-20', '0', '0', '1', '', '2019-04-13 11:14:40', '', '2019-04-15 00:00:00', '1', '', '南京', '王二', null, '0', null);

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
  `create_by` varchar(64) DEFAULT NULL COMMENT '操作人id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `car_info_id` varchar(64) DEFAULT NULL COMMENT '操作车辆id',
  `is_first_time` varchar(10) DEFAULT '0' COMMENT '是否首次查询:默认0 不是 1是',
  `check_type` varchar(4) DEFAULT NULL COMMENT '查询方式0车牌查询1车架查询',
  PRIMARY KEY (`check_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of check_info
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
  `insuiance_type_id` varchar(64) NOT NULL COMMENT 'id',
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `insurance_name` varchar(32) DEFAULT NULL COMMENT '承保险种名称',
  `insurance_amount` decimal(32,8) DEFAULT '0.00000000' COMMENT '保额',
  `insurance_premium` decimal(32,8) DEFAULT '0.00000000' COMMENT '保费',
  `info_type` varchar(32) DEFAULT NULL COMMENT '类型 0投保1报价（0）',
  `type_id` varchar(64) DEFAULT NULL COMMENT '类型id info_type=0表示投保id，info_type=1表示报价id',
  PRIMARY KEY (`insuiance_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='保险分类信息 ';

-- ----------------------------
-- Records of insurance_type_info
-- ----------------------------

-- ----------------------------
-- Table structure for insured_info
-- ----------------------------
DROP TABLE IF EXISTS `insured_info`;
CREATE TABLE `insured_info` (
  `insured_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_id` int(11) DEFAULT NULL COMMENT '创建人',
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
  PRIMARY KEY (`insured_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='投保信息 ';

-- ----------------------------
-- Records of insured_info
-- ----------------------------
INSERT INTO `insured_info` VALUES ('1', null, '2019-04-10 14:15:48', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null, null, null, '人保车险', null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for menu_info
-- ----------------------------
DROP TABLE IF EXISTS `menu_info`;
CREATE TABLE `menu_info` (
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `menu_id` varchar(64) NOT NULL COMMENT 'id',
  `parent_id` varchar(11) DEFAULT NULL COMMENT '父级菜单 0是顶级菜单1是二级菜单，以此类推',
  `url` varchar(128) DEFAULT NULL COMMENT '菜单路径',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单列表 ';

-- ----------------------------
-- Records of menu_info
-- ----------------------------

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
  `policy_id` varchar(11) NOT NULL COMMENT 'id',
  `car_info_id` varchar(11) DEFAULT NULL COMMENT '车辆信息id',
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
  PRIMARY KEY (`quote_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报价信息 ';

-- ----------------------------
-- Records of quote_info
-- ----------------------------
INSERT INTO `quote_info` VALUES (null, null, null, null, null, '1', '1', '1', null, null, '-1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `quote_info` VALUES (null, null, null, null, null, '2', '2', '1', null, null, '-1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `quote_info` VALUES (null, null, null, null, null, '3', '3', '1', null, null, '-1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for role_info
-- ----------------------------
DROP TABLE IF EXISTS `role_info`;
CREATE TABLE `role_info` (
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `role_id` varchar(64) NOT NULL COMMENT 'id',
  `name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `code` varchar(32) DEFAULT NULL COMMENT '角色代码',
  `role_des` varchar(128) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息表 ';

-- ----------------------------
-- Records of role_info
-- ----------------------------

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
-- Table structure for third_insurance_info
-- ----------------------------
DROP TABLE IF EXISTS `third_insurance_info`;
CREATE TABLE `third_insurance_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方保险公司账号,用于续保报价 ';

-- ----------------------------
-- Records of third_insurance_info
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
