-- 购买记录表
CREATE TABLE `t_buy_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `date` varchar(50) NOT NULL COMMENT '购买日期',
  `result` varchar(50) DEFAULT NULL COMMENT '当期开奖red',
  `red` varchar(50) DEFAULT NULL COMMENT '红球',
  `red_hit_total` int(10) DEFAULT NULL COMMENT '红球命中个数',
  `hit_number` varchar(50) DEFAULT NULL COMMENT '中奖号码',
  `blue` varchar(10) DEFAULT NULL COMMENT '篮球',
  `blue_hit_total` int(10) DEFAULT NULL COMMENT '蓝球命中个数',
  `winning_amount` double DEFAULT NULL COMMENT '中奖金额',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_buy_record_UN` (`date`,`red`)
) ENGINE=InnoDB AUTO_INCREMENT=161 DEFAULT CHARSET=utf8 COMMENT='购买记录表';

-- 开奖记录表
CREATE TABLE `t_lottery_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(20) NOT NULL COMMENT '期数',
  `date` varchar(50) NOT NULL COMMENT '开奖日期',
  `week` varchar(20) DEFAULT NULL COMMENT '星期几',
  `red` varchar(50) DEFAULT NULL COMMENT '红球',
  `blue` varchar(20) DEFAULT NULL COMMENT '篮球',
  `content` varchar(500) DEFAULT NULL COMMENT '中奖情况',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_lottery_record_UN` (`code`,`date`),
  KEY `t_lottery_record_red_IDX` (`red`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1597 DEFAULT CHARSET=utf8 COMMENT='开奖记录表';


-- 购买原始数据
CREATE TABLE `t_buy_record_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `date` varchar(50) NOT NULL COMMENT '购买日期',
  `red` varchar(50) DEFAULT NULL COMMENT '红球',
  `red_hit_total` int(10) DEFAULT NULL COMMENT '红球命中个数',
  `blue` varchar(20) DEFAULT NULL COMMENT '篮球',
  `blue_hit_total` int(10) DEFAULT NULL COMMENT '蓝球命中个数',
  `winning_amount` double DEFAULT NULL COMMENT '中奖金额',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_buy_record_UN` (`date`,`blue`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='购买记录表';

