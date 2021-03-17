/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.6.17 : Database - bytecub
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bytecub` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `bytecub`;

/*Table structure for table `t_device` */

DROP TABLE IF EXISTS `t_device`;

CREATE TABLE `t_device` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `device_code` varchar(60) NOT NULL DEFAULT '' COMMENT '设备编码',
  `device_name` varchar(60) NOT NULL DEFAULT '' COMMENT '设备名称',
  `gw_dev_code` varchar(60) NOT NULL DEFAULT '' COMMENT '如果是子设备，关联的网关设备编码',
  `product_code` varchar(60) NOT NULL DEFAULT '' COMMENT '所属的产品编码',
  `del_flag` int(11) NOT NULL DEFAULT '0',
  `enable_status` int(11) NOT NULL DEFAULT '0' COMMENT '0:未启用 1:启用',
  `active_status` int(11) NOT NULL DEFAULT '0' COMMENT '0:离线 1:在线',
  `last_online_time` timestamp NOT NULL DEFAULT '1980-01-01 00:00:00' COMMENT '最近一次上线时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `device_secret` varchar(60) NOT NULL DEFAULT '' COMMENT '设备密钥',
  `firmware_version` varchar(60) NOT NULL DEFAULT '' COMMENT '固件版本',
  `dev_host` varchar(20) NOT NULL DEFAULT '' COMMENT '最近一次上线主机地址',
  `dev_port` int(11) NOT NULL DEFAULT '0' COMMENT '最近一次上线端口',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_CODE` (`device_code`),
  KEY `INDEX_CR_TIME` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=313 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_open_auth` */

DROP TABLE IF EXISTS `t_open_auth`;

CREATE TABLE `t_open_auth` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `app_key` varchar(60) NOT NULL DEFAULT '',
  `app_secret` varchar(60) NOT NULL DEFAULT '',
  `enable_status` tinyint(1) NOT NULL DEFAULT '1',
  `del_flag` tinyint(1) DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `app_desc` varchar(200) NOT NULL DEFAULT '',
  `app_name` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNI_KEY_SECRET` (`app_key`,`app_secret`),
  UNIQUE KEY `UNI_KEY` (`app_key`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_product` */

DROP TABLE IF EXISTS `t_product`;

CREATE TABLE `t_product` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `product_code` varchar(60) NOT NULL DEFAULT '' COMMENT '产品编码',
  `product_name` varchar(60) NOT NULL DEFAULT '' COMMENT '产品名称',
  `transport_list` varchar(200) NOT NULL DEFAULT '' COMMENT '支持的网络传输协议列表',
  `protocol_code` varchar(60) NOT NULL DEFAULT '' COMMENT '关联的消息协议',
  `node_type` varchar(60) NOT NULL DEFAULT '' COMMENT '节点类型 直连设备/子设备/网关设备',
  `product_desc` varchar(200) NOT NULL DEFAULT '' COMMENT '描述',
  `log_url` varchar(255) NOT NULL DEFAULT '' COMMENT '图标网址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` int(11) NOT NULL DEFAULT '0' COMMENT '0:未删除 1 删除',
  `product_status` int(11) NOT NULL DEFAULT '1' COMMENT '0:草稿 1:发布 2:暂停',
  `create_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_CODE` (`product_code`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_product_func` */

DROP TABLE IF EXISTS `t_product_func`;

CREATE TABLE `t_product_func` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `product_code` varchar(60) NOT NULL DEFAULT '' COMMENT '关联的产品编码',
  `func_name` varchar(60) NOT NULL DEFAULT '' COMMENT '功能名称',
  `identifier` varchar(60) NOT NULL DEFAULT '' COMMENT '标识符',
  `func_type` varchar(20) NOT NULL DEFAULT '' COMMENT '功能类型 服务 属性 事件',
  `data_type` varchar(20) NOT NULL DEFAULT '' COMMENT '数据类型',
  `data_define` varchar(200) NOT NULL DEFAULT '' COMMENT '数据定义，JSON格式，数字显示取值范围，文本显示长度',
  `wr_type` int(11) NOT NULL DEFAULT '0' COMMENT '0:只读 1:读写',
  `func_status` int(11) NOT NULL DEFAULT '0' COMMENT '0:草稿 1:启用',
  `del_flag` int(11) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` bigint(20) NOT NULL DEFAULT '0',
  `func_desc` varchar(200) NOT NULL DEFAULT '' COMMENT '描述',
  `unit` varchar(20) NOT NULL DEFAULT '' COMMENT '计量单位(CODE)',
  `unit_name` varchar(20) NOT NULL DEFAULT '' COMMENT '计量单位名称',
  `attr` varchar(5120) NOT NULL DEFAULT '' COMMENT '属性',
  `event_type` varchar(20) NOT NULL DEFAULT '' COMMENT '事件类型',
  `async` int(11) NOT NULL DEFAULT '1' COMMENT '默认异步(服务调用通知结果方式)',
  `input_param` varchar(5120) NOT NULL DEFAULT '' COMMENT '服务输入参数',
  `output_param` varchar(5120) NOT NULL DEFAULT '' COMMENT '服务输出参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_PR_ID` (`product_code`,`identifier`,`func_type`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COMMENT='产品物模型';

/*Table structure for table `t_protocol` */

DROP TABLE IF EXISTS `t_protocol`;

CREATE TABLE `t_protocol` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `protocol_code` varchar(60) NOT NULL DEFAULT '' COMMENT '协议编码',
  `protocol_name` varchar(60) NOT NULL DEFAULT '' COMMENT '协议名称',
  `protocol_file_url` varchar(500) NOT NULL DEFAULT '' COMMENT '协议jar包上传地址',
  `protocol_type` int(11) NOT NULL DEFAULT '0' COMMENT '协议类型 0:未知 1:jar',
  `jar_sign` varchar(100) NOT NULL DEFAULT '' COMMENT '协议文件摘要(文件的md5)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `protocol_status` int(11) NOT NULL DEFAULT '0' COMMENT '0:草稿 1:启用 2:停用',
  `del_flag` int(11) NOT NULL DEFAULT '0' COMMENT '0:正常 1:删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_CODE` (`protocol_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='协议表';

/*Table structure for table `t_resource` */

DROP TABLE IF EXISTS `t_resource`;

CREATE TABLE `t_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource_code` varchar(60) NOT NULL DEFAULT '' COMMENT '产品编码，唯一标志',
  `resource_name` varchar(40) NOT NULL DEFAULT '' COMMENT '产品名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `resource_status` int(11) NOT NULL DEFAULT '0' COMMENT '0:草稿 1:发布 2:停用',
  `del_flag` int(11) NOT NULL DEFAULT '0' COMMENT '0:正常 1:删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_CODE` (`resource_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源管理表';

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `role_code` varchar(40) NOT NULL DEFAULT '',
  `role_name` varchar(50) NOT NULL DEFAULT '',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `op_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNI_ROLE_CODE` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_role_permission` */

DROP TABLE IF EXISTS `t_role_permission`;

CREATE TABLE `t_role_permission` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `role_code` varchar(50) NOT NULL DEFAULT '' COMMENT '角色编码',
  `permission_code` varchar(50) NOT NULL DEFAULT '' COMMENT '权限编码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `op_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '操作人ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COMMENT='角色权限表';

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '无意义自增长主键',
  `user_name` varchar(20) NOT NULL DEFAULT '' COMMENT '登录名,用户编码',
  `user_nick` varchar(20) NOT NULL DEFAULT '' COMMENT '昵称',
  `passwd` varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'true or false',
  `role_type` int(11) NOT NULL DEFAULT '3' COMMENT '内置角色 1:超级管理员 2:管理员 3:演示用户',
  `user_status` int(11) NOT NULL DEFAULT '1' COMMENT '1:正常 2:禁用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `role_code` varchar(60) NOT NULL DEFAULT '' COMMENT '角色编码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNI_UNAME` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
insert  into `t_role`(`id`,`role_code`,`role_name`,`create_time`,`update_time`,`op_id`) values (1,'test','测试','2021-02-23 18:55:27','2021-02-23 18:55:27',0),(2,'admin','管理员','2021-02-23 19:23:11','2021-02-23 19:23:11',0);

/*Data for the table `t_role_permission` */

insert  into `t_role_permission`(`id`,`user_id`,`role_code`,`permission_code`,`create_time`,`update_time`,`op_id`) values (1,0,'test','system:user:add','2021-02-23 18:56:54','2021-02-23 18:56:54',0),(2,0,'admin','sys:product:create','2021-02-23 19:23:41','2021-02-23 19:23:41',0),(3,0,'admin','sys:product:update','2021-02-23 19:23:41','2021-02-23 19:23:41',0),(4,0,'admin','sys:product:delete','2021-02-23 19:23:42','2021-02-23 19:23:42',0),(5,0,'admin','sys:model:release','2021-02-23 19:23:44','2021-02-23 19:23:44',0),(6,0,'admin','sys:model:add','2021-02-23 19:23:44','2021-02-23 19:23:44',0),(7,0,'admin','sys:model:update','2021-02-23 19:23:46','2021-02-23 19:23:46',0),(8,0,'admin','sys:model:delete','2021-02-23 19:23:46','2021-02-23 19:23:46',0),(9,0,'admin','system:device:batchadd','2021-02-23 19:23:47','2021-02-23 19:23:47',0),(10,0,'admin','system:device:batchadd','2021-02-23 19:23:48','2021-02-23 19:23:48',0),(11,0,'admin','system:device:add','2021-02-23 19:23:56','2021-02-23 19:23:56',0),(12,0,'admin','system:device:update','2021-02-23 19:24:40','2021-02-23 19:24:40',0),(13,0,'admin','system:device:gw:map','2021-02-23 19:24:41','2021-02-23 19:24:41',0),(14,0,'admin','system:device:status','2021-02-23 19:24:46','2021-02-23 19:24:46',0),(15,0,'admin','system:open:add','2021-02-23 19:59:03','2021-02-23 19:59:03',0),(16,0,'admin','system:open:update','2021-02-23 19:59:05','2021-02-23 19:59:05',0),(17,0,'admin','system:open:delete','2021-02-23 19:59:06','2021-02-23 19:59:06',0),(18,0,'admin','system:open:secret','2021-02-23 19:59:18','2021-02-23 19:59:18',0);
insert into `t_role_permission` ( `user_id`, `role_code`, `permission_code`, `create_time`, `update_time`, `op_id`) values('0','admin','system:device:import','2021-03-16 06:45:16','2021-03-16 06:45:16','0');

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`user_name`,`user_nick`,`passwd`,`del_flag`,`role_type`,`user_status`,`create_time`,`update_time`,`role_code`) values (1,'admin','超级管理员','f59bd65f7edafb087a81d4dca06c4910',0,1,1,'2020-12-14 16:47:56','2021-02-23 19:25:30','admin'),(2,'demo','演示用户','f59bd65f7edafb087a81d4dca06c4910',0,3,1,'2021-02-23 19:25:41','2021-02-23 19:25:46','');
