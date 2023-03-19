package com.fmbank.welfarelottery.util;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class BallRandomUtil {
    /**
     * 获取N注双色球号码:只为红球
     */
    public static ArrayList<String> getDoubleColorBallNumber(int num) {
        log.info("随机生成" + num + "注双色球号码为：");
        ArrayList<String> reds = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            if (reds.contains(getRedBall())) {
                i = i - 1;
            } else {
                reds.add(getRedBall());
            }
        }
        return reds;
    }

    /**
     * 获取单注双色球号码
     */
    public static String getRedBall() {
        ArrayList<String> reds = new ArrayList<>();
        List<String> redBalls = getRedBalls();
        for (int i = 0; i < 6; i++) {
            if (ObjectUtils.isEmpty(redBalls)) break;
            String selected = RandomUtil.randomEle(redBalls);
            reds.add(selected);
            redBalls.remove(selected);
        }
        Collections.sort(reds);
        return StringUtils.join(reds, ",");
    }
    /**
     * 获取单注双色球号码
     */
    public static String getBlueBall() {
        return RandomUtil.randomEle(getBlueBalls());
    }
    /**
     * 获取红球球号集合
     */
    public static List<String> getRedBalls() {
        return getBalls(33);
    }

    /**
     * 获取蓝球球号集合
     */
    public static List<String> getBlueBalls() {
        return getBalls(16);
    }

    /**
     * 获取球号集合
     */
    public static List<String> getBalls(int num) {
        List<String> redBalls = Lists.newArrayList();
        for (int i = 1; i <= num; i++) {
            int length = String.valueOf(num).length();
            String str = String.format("%0" + length + "d", i);
            redBalls.add(str);
        }
        return redBalls;
    }
}
