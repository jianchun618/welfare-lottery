package com.fmbank.welfarelottery.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: jv
 * @create: 2022/05/25 15:54
 **/
@Slf4j
public class AbstractConvert {
    public static <P, V> V convert(P p, Class<V> vClass) {
        if (p == null) {
            return null;
        }
        try {
            V v = vClass.getConstructor().newInstance();
            BeanUtils.copyProperties(p, v);
            return v;
        } catch (Exception ex) {
            log.error("[{}] convert to [{}] error", p, vClass, ex);
        }
        return null;
    }

    public static <P, V> List<V> convert(List<P> list, Class<V> vClass) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }
        return list.stream().map(p -> convert(p, vClass)).collect(Collectors.toList());
    }

}
