
CREATE TABLE `sys_article` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `data_id` int(20) DEFAULT NULL COMMENT '数据id(词频会与此关联)',
  `title_id` int(5) DEFAULT NULL COMMENT '标签id',
  `topic_id` varchar(20) DEFAULT NULL COMMENT '话题id,逗号分隔',
  `content_id` int(1) DEFAULT NULL COMMENT '内容id(1:UGC;2:pgc)',
  `content` text COMMENT '内容',
  `emotion_type` int(1) DEFAULT '0' COMMENT '情感类型（0：中性 ; 1:负面；2：中性）',
  `emotion_value` int(5) DEFAULT '0' COMMENT '情感类型（0：中性 ; 1:负面；2：中性）',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '插入时间',
  `media_type` int(1) DEFAULT '0' COMMENT '0：微信；1：微博；2：博客；3：论坛：4：问答；5：新闻',
  `publisher_id` int(10) DEFAULT '0' COMMENT '发布人id',
  `publisher_link_num` int(5) DEFAULT '0' COMMENT '点赞数',
  `publisher_pv` int(5) DEFAULT '0' COMMENT '阅读数',
  `publisher_comment_num` int(5) DEFAULT '0' COMMENT '评论数',
  `publisher_collection_num` int(5) DEFAULT '0' COMMENT '收藏数',
  `publisher_repost_num` int(5) DEFAULT '0' COMMENT '转发数',
  `publisher_site_name` varchar(10) DEFAULT '' COMMENT '站点名',
  `publisher_article_title` varchar(128) DEFAULT '' COMMENT '文章头',
  `publisher_article_unique` varchar(64) DEFAULT '' COMMENT '文章base64',
  `publisher_article_url` varchar(128) DEFAULT '' COMMENT '站点链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

CREATE TABLE `sys_article_detail` (
  `publisher_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '发布人',
  `publisher` varchar(20) DEFAULT '' COMMENT '发布人',
  `publisher_url` varchar(128) DEFAULT '0' COMMENT '发布人id',
  `publisher_user_type` int(1) DEFAULT '0' COMMENT '发布人id',
  `publisher_time` timestamp NULL DEFAULT NULL COMMENT '发布人时间',
  PRIMARY KEY (`publisher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='帖子明细表';
