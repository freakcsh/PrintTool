package com.freak.printtool.hardware.app;

/**
 * @author Freak
 * @date 2019/8/13.
 */

public class Constants {
    public static final String WHETHER_PRINT = "whether_print";
    public static String DEVICE_NAME = "deviceName";
    public static boolean ISCONNECTED;
    public static final String PRINT_DIRECTION = "print_direction";

    /**
     * 以下是设备的标记
     */

    //8位商品设置
    public static final String OTHER_NORMAL = "20";

    //自编码商品设置
    public static final String OWN_CODING = "29";
    public static final String OWN_CODING_NUMBER = "9";

    //电子秤设置  品牌：    型号：
    public static final String SCALE_IP = "scale_ip";
    public static final int TMA_SCALE_PORT = 4001;
    public static final String SCALE_DAHUA = "22";

    //标签打印机 品牌：SPRT 思普瑞特  型号：SP-TL51 台式标签打印机
    public static final int WENDOR_ID_SPRT = 1155;
    public static final int PRODUCT_ID_SPRT = 22304;

    //小票打印机 品牌：标准（暂时使用）  型号：58热敏式票据打印机
    public static final int WENDOR_ID_BZ = 1155;
    public static final int PRODUCT_ID_BZ = 1803;

    //小票打印机：热敏票据打印机 品牌：资江  型号：ZJ-5890D
    public static final int WENDOR_ID_ZJ = 1046;
    public static final int PRODUCT_ID_ZJ = 20497;
    public static final String CONNECTSTATE = "connectState";
}
