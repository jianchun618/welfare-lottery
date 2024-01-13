package com.fmbank.welfarelottery.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 3组六购买记录表
 * </p>
 *
 * @author jianchun
 * @since 2024-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TThreeSixBuyRecord implements Serializable {

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
     * 定值30组，概率25%（用于购买）
     */
    private String buyNumber;

    /**
     * 购买金额
     */
    private Integer buyAmount;

    /**
     * 购买倍数
     */
    private Integer buyDouble;

    /**
     * 本期是否中奖0未中奖1已中奖
     */
    private String winStatus;

    /**
     * 中奖号码
     */
    private String winNumber;

    /**
     * 中奖金额金额
     */
    private Integer winAmount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;


}
