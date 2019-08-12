package com.freak.printtool.hardware.app;

/**
 * Created by hboxs006 on 2017/2/18.
 */

public class Constants {

    /*
     1、线上服务器路径：http://120.79.82.252/asl-merchant/merchant/login.htm
            2、测试推送：http://120.79.82.252:9999/index.html
     */


//    public static final String BASE_URL = "http://192.168.50.77:8089/";//泽桃
    public static final String BASE_URL = "http://120.79.82.252";//远程
//    public static final String BASE_URL = "http://192.168.50.88:8083/";//总监
//      public static final String BASE_URL = "http://192.168.50.125:8088/";//卓航
//    public static final String BASE_URL = "http://192.168.50.98:8080/";//城才
//    public static final String BASE_URL = "http://egou.gm315.com/";//域名

    //ip
//    public static final String BASE_URL = "http://120.25.240.114:8081/";
//    public static final String BASE_URL = "http://www.jiuchasheng.cn/";

    //测服
//    public static final String BASE_URL = "http://120.24.165.220:8080/";

    //标准商品
    public static final String NORMAL = "normal";
    //散称商品
    public static final String WEIGHT = "weight";
    //自编码商品
    public static final String CUSTOM = "custom";


    /**
     * @anthor dmin
     * created at 2017/11/15 16:56
     */

    //这是商家登录
    public static final String TOKEN = "token";
    //这是省区的开发
    public static final String GAO_MI_APP_ID = "gao_mi_app_id";
    public static final String TOKEN_EXPIRE = "token_expire";
    public static final String TOKEN_ABLE = "token_able";

    /* 这是收银员登录 */
    //商家信息是在收银员登录的时候更新 8

    //这是商家的id
    public static final String MERCHANT_ID = "merchantId";
    //这是商家的地址
    public static final String MERCHANT_ADDRESS = "merchantAddress";
    //这是商家是否启用会员卡 able-启用；disable-不启用
    public static final String MERCHANT_CARD_AVAILABLE = "merchantCardAvailable";
    //这是商家的配送费
    public static final String MERCHANT_DELIVERY_FEE = "merchantDeliveryFee";
    //这是商家的最小起送价
    public static final String MERCHANT_MIN_DELIVERY_PRICE = "merchantMinDeliveryPrice";
    //这是商家的名称
    public static final String MERCHANT_NAME = "merchantName";
    //这是商家的联系电话
    public static final String MERCHANT_PHONE = "merchantPhone";
    //这是商家的账号
    public static final String MERCHANT_USERNAME = "merchantUsername";

    //这是收银员信息 3
    //这是收银员的id
    public static final String CASHIER_ID = "cashierId";
    //这是收银员的名字
    public static final String CASHIER_NAME = "cashierName";
    //这是收银员的工号
    public static final String CASHIER_JOB_NUMBER = "cashierJobNumber";
    //这是收银员的权限 manager-店长；salesclerk-销售员；other-其他；null也是店长
    public static final String CASHIER_POSITION = "cashierPosition";

    //这是总后台的限制信息
    //这是总后台设置的单店的会员人数上限 ，没有就默认是10000
    public static final String PLATFORM_COUNT = "platformCount";
    //这是总后台设置的单笔积分消费上限，没有就默认是100
    public static final String PLATFORM_ECOIN = "platformEcoin";


    //这是和退出登录有关
    //这是收银员的登录时间
    public static final String CASHIER_LOGIN_TIME = "cashier_login_time";
    //这是收银员的登录的备用金
    public static final String CASHIER_STANDBY_CASH = "cashier_standby_cash";

    //   RxBus
    public static final String RENOVATE = "renovate";

    //公用对话框的文字
    public static final String COMMON_TEXT = "commonText";

    //设置状态对话框的文字
    public static final String DEVICE_STATE = "deviceState";

    //公用对话框的提示
    public static final String TIPS_TITLE = "tipsTitle";
    public static final String TIPS_TEXT = "tipsText";
    public static final String TIPS_HINT = "tipsHint";
    public static final String TIPS_CANCEL = "tipsCancel";
    public static final String TIPS_SUCCEED = "tipsSucceed";
    public static final String TIPS_RIGHT = "text_right";
    public static final String TIPS_TYPE = "text_type";

    //购物车相关的信息
    public static final String CART_IS_VALIDATE = "cart_is_validate";
    public static final String CART_COUPON_CODE = "cart_coupon_code";
    public static final String CART_MEMBER_ID = "cart_member_id";
    public static final String CART_MEMBER_PHONE = "cart_member_phone";
    public static final String CART_TOTAL_COIN = "cart_total_coin";
    public static final String CART_INFO = "cart_info";
    public static final String CART_POSITION = "cart_position";
    public static final String CART_TOTAL = "cart_total";
    public static final String CART_LIST = "cart_list";
    //这是普通消费
    public static final String CART_ORDINARY_PRICE = "cart_ordinary_price";

    public static final String SELECT_TYPE = "select_type";
    public static final String VIP_TYPE = "vip_type";

    //会员
    public static final String MERCHANT_COUPON = "merchant_coupon";
    public static final String MEMBER_DATA = "member_data";
    public static final String SELECT_DATA = "select_data";
    public static final String CARD_POSITION = "card_position";
    public static final String CARD_LIST = "select_list";
    public static final String MEMBER_BUNDLE = "member_bundle";
    public static final String MEMBER_EDIT = "member_edit";
    public static final String MEMBER_SN = "member_sn";
    public static final String MEMBER_PHONE = "member_phone";

    //订单
    public static final String ORDER_SN = "order_sn";

    //消息中心
    public static final String POSITION = "msg_position";

    //这是三种支付方式 订单项
    public static final String ECOIN = "eCoinWithCash";
    public static final String CASH = "cash";

    public static final String CLIENT_ID = "client_id";
    public static final String DISCOUNT_LIST = "discount_list";
    public static final String STOCK_FLOW_SN = "stockFlowSn";
    public static final String EDIT_TEXT_TYPE = "edit_text_type";
    public static final String PAYMENT_METHOD = "payment_method";
    public static final String MEMBER_PROMOTION = "member_promotion";
    public static final String FIRST_PROMOTION_POSITION = "first_promotion_position";
    public static final String WE_CHAT_CONFIG = "weChatConfig";
    public static final String APKINFO = "apk_info";
    public static final String WHETHER_PRINT = "whether_print";

    //新商品添加
    public static final String MULTI_PRODUCT_SN = "multi_product_sn";
    public static final String UNIT_PRODUCT = "unit_product";
    public static final String UNIT_PRODUCT_NEW = "unit_product_old";
    public static final String UNIT_LIST = "unit_list";

    public static final String RANK_ERROR = "发生了一点小意外";
    public static final String PROMOTION = "promotion";
    public static final String CART_DATA = "cart_data";
    public static final String CART_ORI_TOTAL = "cart_ori_total";
    public static final String SHOW_HIDE_VIDEO = "show_hide_video";
    public static final String RECEIPT_PRINT_DIRECTION = "receipt_print_direction";
    public static final String CATEGORY = "category";
    public static final String ORDER_NUM ="order_num" ;
    public static final String INTENT_TYPE = "intent_type";
    public static final String WIFI_STATE = "ic_network_off";
    public static final String SHOW_HIDE_TIME = "show_hide_time";
    public static final String SHOW_HIDE_WEEK = "show_hide_week";
    public static final String SHOW_HIDE_SYSTEM = "show_hide_system";
    public static final String SHOW_HIDE_SECOND = "show_hide_second";

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
    public static final String PLATFORM_MEMBER = "平台会员";
    public static final String MERCHANT_MEMBER = "商家会员";
    public static final String CONNECTSTATE = "connectState";
}
