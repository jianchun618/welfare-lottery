-- 3d开奖记录表
CREATE TABLE `t_three_d_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '期号',
  `date` varchar(32) DEFAULT NULL COMMENT '日期',
  `week` varchar(32) DEFAULT NULL COMMENT '周几',
  `lottery_number` varchar(32) DEFAULT NULL COMMENT '开奖号码',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_three_d_record_UN` (`date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='3d开奖记录表';


CREATE TABLE `t_three_d_buy_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '期号',
  `date` varchar(32) DEFAULT NULL COMMENT '日期',
  `lottery_number` varchar(32) DEFAULT NULL COMMENT '开奖号码',
  `buy_number` varchar(200) DEFAULT NULL COMMENT '下期最大未开奖期数前十个号（用于购买）',
  `win_status` varchar(32) DEFAULT NULL COMMENT '本期是否中奖0未中奖1已中奖',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_three_d_buy_record_UN` (`date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='3购买记录表';

CREATE TABLE `t_three_d_his_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `calculate_code` varchar(32) DEFAULT NULL COMMENT '计算期数',
  `lottery_number` varchar(32) DEFAULT NULL COMMENT '奖号:001-999所有号码',
  `code` varchar(32) DEFAULT NULL COMMENT '历史开奖期号',
  `date` varchar(32) DEFAULT NULL COMMENT '历史开奖日期',
  `period` varchar(32) DEFAULT NULL COMMENT '历史未开奖的期数（现在的期号减去历史开奖的期号）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_three_d_his_summary_UN` (`lottery_number`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='奖号历史未开奖的期数统计';
