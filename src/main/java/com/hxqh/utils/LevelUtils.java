package com.hxqh.utils;

import com.hxqh.enums.HealthLevel;
import com.hxqh.enums.PercentageRreactive;

/**
 * 优秀—绿色：80<分数≤100；
 * 良好—黄色：60<分数≤80；
 * 极差—红色：分数≤60
 * <p>
 * Created by Ocean lin on 2020/3/17.
 *
 * @author Ocean lin
 */
public class LevelUtils {

    private static final Integer Sixty = 60;
    private static final Integer Eighty = 80;
    private static final Integer OneHundred = 100;

    private static final Double Eighty_PERCENT = 0.8d;
    private static final Double Ninety_PERCENT = 0.9d;
    private static final Double OneHundred_PERCENT = 1d;

    private static final Double TEN_PERCENT = 0.1d;


    public static String computeLevel(Double score) {
        if (Eighty < score && score <= OneHundred) {
            return HealthLevel.excellent.getCode();
        } else if (Sixty < score && score <= Eighty) {
            return HealthLevel.good.getCode();
        } else {
            return HealthLevel.range.getCode();
        }
    }


    public static String computeLoadFactorLevel(Double score) {
        if (Eighty_PERCENT < score && score <= Ninety_PERCENT) {
            return HealthLevel.good.getCode();
        } else if (Ninety_PERCENT < score && score <= OneHundred_PERCENT) {
            return HealthLevel.range.getCode();
        } else {
            return HealthLevel.excellent.getCode();
        }
    }

    public static String computePercentageRreactive(Double percentage) {
        if (percentage > TEN_PERCENT ) {
            return PercentageRreactive.Concerned.getCode();
        }else {
            return PercentageRreactive.Reasonable.getCode();
        }
    }

}
