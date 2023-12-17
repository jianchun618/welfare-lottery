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
 * 奖号历史未开奖的期数统计
 * </p>
 *
 * @author jianchun
 * @since 2023-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_three_d_his_summary")
public class TThreeDHisSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 计算期数
     */
    private String calculateCode;

    /**
     * 奖号:001-999所有号码
     */
    private String lotteryNumber;

    /**
     * 历史开奖期号
     */
    private String code;

    /**
     * 历史开奖日期
     */
    private String date;

    /**
     * 历史未开奖的期数（现在的期号减去历史开奖的期号）
     */
    private String period;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;


}
