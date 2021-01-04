drop table if exists `user_operation_log`;
CREATE TABLE `user_operation_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `login_ip` varchar(30) NOT NULL COMMENT '登录ip',
  `action` varchar(50) NOT NULL COMMENT '操作类型（登录，登出，新增，修改，删除，导出）',
  `user_name` varchar(256) NOT NULL DEFAULT '' COMMENT '操作账户名',
  `creator_id` bigint(20) NOT NULL COMMENT '创建人id',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_login_ip` (`login_ip`) USING BTREE,
  KEY `idx_action` (`action`) USING BTREE,
  KEY `idx_gmt_create` (`gmt_create`) USING BTREE,
  KEY `idx_user_name` (`user_name`(255)) USING BTREE,
  KEY `idx_ip_user_name` (`login_ip`,`user_name`(255)) USING BTREE,
  KEY `idx_user_name_action` (`user_name`(255),`action`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户操作日志';