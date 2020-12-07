
CREATE TABLE `sys_article` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL COMMENT '权限名称',
  `create_by` int(11) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `parent_id` int(20) DEFAULT NULL COMMENT '父id',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '是否禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';




id

data_id  对应的词频

title_id  标签id

topic_id  话题id

content_id  内容id

emotion_name  情感名

emotion_value  情感值

insert_time 时间

media_type 媒体类型

post_man 发布人

post_man_id 发布人id


