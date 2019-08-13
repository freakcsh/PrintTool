package com.freak.printtool.hardware.utils;

import android.os.Handler;
import android.view.View;

import com.freak.printtool.hardware.app.Constants;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Freak
 * @date 2019/8/13.
 */

public class CommonUtils {


    public static void disabledView(final View v, long time) {
        v.setEnabled(false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                v.setEnabled(true);
            }
        }, time);
    }

    /**
     * 判断是否包含汉字
     *
     * @param str 字符串
     * @return true?是汉字：不是汉字
     */
    public static boolean isContainChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /**
     * 判断是否全为汉字
     *
     * @param str 字符串
     * @return true?是汉字：不是汉字
     */
    public static boolean isAllChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        boolean flg = str.matches(regEx);
        return flg;
    }

    /**
     * 获取字符串中中文的个数
     *
     * @param str
     * @return
     */
    public static int getChineseCount(String str) {
        String strPattern = "[\u4e00-\u9fa5]";
        int chinaCount = 0;
        Pattern pattern = Pattern.compile(strPattern);
        int length = 0;
        if (str != null) {
            Matcher aMatcher = pattern.matcher(str);
            System.out.println("是否有中文：" + (aMatcher.find() ? "有" : "无"));
            char c[] = str.toCharArray();
            length = c.length;
            for (int i = 0; i < length; i++) {
                Matcher matcher = pattern.matcher(String.valueOf(c[i]));
                if (matcher.matches()) {
                    chinaCount++;
                }
            }
        }
        System.out.println("字符串总个数：" + length);
        System.out.println("其中中文个数：" + chinaCount);
        System.out.println("非中文个数：" + (length - chinaCount));
        return chinaCount;
    }

    /**
     * 用空格补齐至count个
     *
     * @param str
     * @param count
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String formatStr(String str, int count) throws UnsupportedEncodingException {

        char c[] = str.toCharArray();
        int length = c.length;
        int chinaCount = getChineseCount(str);
        //中文占几个空格
        int chineseSpaceCount = 2 * chinaCount;
        //其它字母和数字占几个空格
        int otherSpaceCount = length - chinaCount;
        int total = chineseSpaceCount + otherSpaceCount;
        //全是中文并且总字节数大于需求字节
        if (isAllChinese(str) && total >= count) {
            return str.substring(0, 6) + "  ";
        }
        if (!isAllChinese(str) && total >= 14) {
            return getFormatString(str, 13) + " ";
        }
        if (str == null) {
            str = "";
        }
        int strLen = total;
        if (strLen == count) {
            return str;
        } else if (strLen < count) {
            int temp = count - strLen;
            String tem = "";
            for (int i = 0; i < temp; i++) {
                tem = tem + " ";
            }
            return str + tem;
        } else {
            return getFormatString(str, count);
        }
    }

    //判断字符是否是中文
    public static boolean isChineseChar(char c) throws UnsupportedEncodingException {
        String stringPattern = "[\u4e00-\u9fa5]";
        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(String.valueOf(c));
        return matcher.matches();
    }

    /**
     * 按字节截取字符串
     *
     * @param orignal 原始字符串
     * @param count   截取位数
     * @return 截取后的字符串
     * @throws UnsupportedEncodingException 使用了JAVA不支持的编码格式
     */
    public static String substring(String orignal, int count)
            throws UnsupportedEncodingException {
        // 原始字符不为null，也不是空字符串
        if (orignal != null && !"".equals(orignal)) {
            // 将原始字符串转换为GBK编码格式
            orignal = new String(orignal.getBytes(), "GBK");
            // 要截取的字节数大于0，且小于原始字符串的字节数
            if (count > 0 && count < orignal.getBytes("GBK").length) {
                StringBuffer buff = new StringBuffer();
                char c;
                for (int i = 0; i < count; i++) {
                    c = orignal.charAt(i);
                    buff.append(c);
                    if (isChineseChar(c)) {
                        // 遇到中文汉字，截取字节总数减1
                        --count;
                    }
                }
                return buff.toString();
            }
        }
        return orignal;
    }

    public static String getFormatString(String str, int count) {
        int total = count;
        byte[] bs;
        if (str == null || str.length() == 0) {
            return "";
        }
        StringBuilder rst = new StringBuilder();
        for (char ch : str.toCharArray()) {
            String s = ch + "";
            if (ch > 0x80) {
                // 汉字
                if (total >= 2) {
                    rst.append(s);
                    total = total - 2;
                } else if (total > 0) {
                    rst.append(" ");
                    total = total - 1;
                }
            } else {
                // ascii
                if (total > 0) {
                    rst.append(s);
                    total = total - 1;
                }
            }
        }
        if (total > 0) {
            for (int i = 0; i < total; i++) {
                //不够补空格
                rst.append(" ");
            }
        }
        return rst.toString();
    }

    /**
     * 处理自编码
     */
    public static String handlePluNumber(String pluNumber, String merchantId, boolean isMain) {
        DecimalFormat decimalFormat = new DecimalFormat("00000");
        if (pluNumber.length() == 18 && isMain == false) {
            //18位
            if (pluNumber.startsWith("22")) {
                //以22开头
                pluNumber = pluNumber.substring(2, 7);
            }
        }
        if (pluNumber.length() == 13) {
            //13位
            //这是散称标签
            if (pluNumber.startsWith(Constants.SCALE_DAHUA) && pluNumber.substring(2, 7).equals(decimalFormat.format(Integer.valueOf(merchantId)))) {
                //以22开头,商家id相同
                //例子 22 00001 00002 3
                //首页不能扫码散称商品的标签
                if (isMain) {
                    pluNumber = "-1";
                } else {
                    pluNumber = pluNumber.substring(7, 12);
                }
            }
            //这是自定义标签
            if (pluNumber.startsWith(Constants.OWN_CODING) && pluNumber.substring(2, 7).equals(decimalFormat.format(Integer.valueOf(merchantId)))) {
                //以22开头,商家id相同
                //例子 29 00001 90002 3 从零开始数
                if (pluNumber.substring(7, 8).equals(Constants.OWN_CODING_NUMBER)) {
                    pluNumber = pluNumber.substring(8, 12);
                }
            }
        }
        return pluNumber;
    }

    /**
     * 根据单位转化成g
     *
     * @param unit
     * @param stock
     * @return
     */
    public static String handleStock(String unit, String stock) {
        String s = "";
        switch (unit) {
            case "g":
                s = stock;
                break;
            case "斤":
                s = DecimalUtil.multiply(stock, "500");
                break;
            case "kg":
                s = DecimalUtil.multiply(stock, "1000");
                break;
            default:
                break;
        }
        return s;
    }


    public static String transformNumWithUnit(String number, String unit) {
        String s = "";
        switch (unit) {
            case "g":
                s = number;
                break;
            case "斤":
                s = DecimalUtil.divideWithRoundingModeAndScale(number, "500", RoundingMode.HALF_EVEN, 3);
                break;
            case "kg":
                s = DecimalUtil.divideWithRoundingModeAndScale(number, "1000", RoundingMode.HALF_EVEN, 3);
                break;
            default:
                break;
        }
        return s;
    }

}
