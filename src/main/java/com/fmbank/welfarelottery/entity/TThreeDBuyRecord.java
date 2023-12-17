package com.fmbank.welfarelottery.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 3购买记录表
 * </p>
 *
 * @author jianchun
 * @since 2023-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_three_d_buy_record")
public class TThreeDBuyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 期号
     */
    private String code;

    /**
     * 日期
     */
    private String date;

    /**
     * 开奖号码
     */
    private String lotteryNumber;

    /**
     * 下期最大未开奖期数前十个号（用于购买）
     */
    private String buyNumber;

    /**
     * 本期是否中奖0未中奖1已中奖
     */
    private String winStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;


}
