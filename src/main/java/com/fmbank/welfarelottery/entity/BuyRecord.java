package com.fmbank.welfarelottery.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 购买记录表
 * </p>
 *
 * @author jianchun
 * @since 2023-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_buy_record")
@ToString
public class BuyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 购买日期
     */
    private String date;

    /**
     * 红球
     */
    private String red;
    /**
     * 当期开奖红球结果
     */
    private String result;

    /**
     * 红球命中个数
     */
    private Integer redHitTotal;

    /**
     * 篮球
     */
    private String blue;

    /**
     * 蓝球命中个数
     */
    private Integer blueHitTotal;

    /**
     * 中奖金额
     */
    private double winningAmount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;


}
