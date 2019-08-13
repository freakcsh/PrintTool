package com.freak.printtool.hardware.utils;

import android.annotation.SuppressLint;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 这是进行BigDecimal的四则运算
 *
 * @author Freak
 * @date 2019/8/13.
 */
public class DecimalUtil {

    /**
     * 金钱加法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    /**
     * 金钱加法 3
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String add(String v1, String v2, String v3) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        BigDecimal b3 = new BigDecimal(v3);
        return ((b1.add(b2)).add(b3)).toString();
    }

    /**
     * 金钱加法 4
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String add(String v1, String v2, String v3, String v4) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        BigDecimal b3 = new BigDecimal(v3);
        BigDecimal b4 = new BigDecimal(v4);
        return (((b1.add(b2)).add(b3)).add(b4)).toString();
    }

    /**
     * 金钱加法 5
     *
     * @param v1
     * @param v2
     * @param v3
     * @param v4
     * @param v5
     * @return
     */
    public static String add(String v1, String v2, String v3, String v4, String v5) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        BigDecimal b3 = new BigDecimal(v3);
        BigDecimal b4 = new BigDecimal(v4);
        BigDecimal b5 = new BigDecimal(v5);
        return (((b1.add(b2)).add(b3)).add(b4).add(b5)).toString();
    }

    /**
     * 金钱减法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String subtract(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }

    /**
     * 金钱乘法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).toString();
    }

    /**
     * 乘法
     *
     * @param v1    乘数
     * @param v2    被乘数
     * @param scale 小数点保留位数
     * @return
     */
    public static String multiplyWithScale(String v1, String v2, int scale) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        BigDecimal result = b1.multiply(b2);
        result = result.setScale(scale, RoundingMode.HALF_EVEN);
        return result.toString();
    }

    /**
     * 金钱除法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String divide(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2).toString();
    }

    /**
     * 如果除不断，产生无限循环小数，那么就会抛出异常，解决方法就是对小数点后的位数做限制
     *
     * @param v1
     * @param v2 小数模式 ，DOWN，表示直接不做四舍五入，参考{@link RoundingMode}
     * @return
     */
    public static String divideWithRoundingDown(String v1, String v2) {
        return divideWithRoundingMode(v1, v2, RoundingMode.DOWN);
    }

    /**
     * 选择小数部分处理方式
     */
    public static String divideWithRoundingMode(String v1, String v2, RoundingMode roundingMode) {
        return divideWithRoundingModeAndScale(v1, v2, RoundingMode.DOWN, 0);
    }

    /**
     * 选择小数点后部分处理方式，以及保留几位小数
     *
     * @param v1           除数
     * @param v2           被除数
     * @param roundingMode 小数处理模式
     * @param scale        保留几位
     * @return
     */
    public static String divideWithRoundingModeAndScale(String v1, String v2,
                                                        RoundingMode roundingMode, int scale) {
        try {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.divide(b2, scale, roundingMode).toString();
        } catch (ArithmeticException e) {
            ToastUtil.shortShow("除数不能为0");
            return "0";
        }
    }


    /**
     * 比较大小
     *
     * @param v1
     * @param v2
     * @return -1：v1<v2 0：v1=v2 1:v1>v2
     */
    public static int compareTo(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.compareTo(b2);
    }

    /**
     * 在整数后面加上小数掉和2个0
     *
     * @param v
     * @return
     */
    public static String toDecimal(String v) {
        Float f = Float.valueOf(v);
        @SuppressLint("DefaultLocale")
        String format = String.format("%.2f", f);
        return format;
    }

}
