package com.freak.printtool.hardware.module.nosdkprint;

import android.text.TextUtils;
import android.util.Log;

import com.freak.printtool.hardware.app.App;
import com.freak.printtool.hardware.app.Constants;
import com.freak.printtool.hardware.print.bean.CollectionPrintBean;
import com.freak.printtool.hardware.print.bean.MemberRechargePrintBean;
import com.freak.printtool.hardware.print.bean.PrintfBean;
import com.freak.printtool.hardware.print.bean.StockFlowPrintBean;
import com.freak.printtool.hardware.print.bean.TransferPrintBean;
import com.freak.printtool.hardware.print.bean.WebOrderPrintBean;
import com.freak.printtool.hardware.utils.ACache;
import com.freak.printtool.hardware.utils.CommonUtils;
import com.freak.printtool.hardware.utils.DateUtil;
import com.freak.printtool.hardware.utils.DecimalUtil;

import java.io.UnsupportedEncodingException;

/**
 * 调用硬件工具类
 *
 * @author Freak
 * @date 2019/8/13.
 */

public class HardwareUtil {

    public static final byte[] PRINTF_CUT = {0x0a, 0x0a, 0x1d, 0x56, 0x01};
    public static final byte[] PUSH_CASH = {0x1b, 0x70, 0x00, 0x1e, (byte) 0xff, 0x00};

    public static final int TYPE_COLLECTION = 0;
    public static final int TYPE_WEB_ORDER = 1;
    public static final int TYPE_VIP_CARD = 2;
    public static final int TYPE_TRANSFER = 3;

    /**
     * 设备是否连接
     */
    public static boolean isConnected(UsbAdmin mUsbAdmin) {
        mUsbAdmin.openUsb();

        if (!mUsbAdmin.getUsbStatus()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否能打印
     */
    public static boolean isPrintfData(PrintfBean content, UsbAdmin mUsbAdmin, int type) {
        Boolean print = (Boolean) ACache.get(App.getInstance().getApplicationContext()).getAsObject(Constants.WHETHER_PRINT);
        Log.d("HardwareUtil", "是否打印" + print);
        if (print != null && !print) {
            return false;
        }

        byte[] data = new byte[1024];
        String cmd = "";
        try {

            switch (type) {
                case TYPE_COLLECTION:
                    cmd = productCollectFormat((CollectionPrintBean) content);
                    break;
                case TYPE_WEB_ORDER:
                    cmd = webOrderFormat((WebOrderPrintBean) content);
                    break;
                case TYPE_VIP_CARD:
                    cmd = buyVipCardFormat((CollectionPrintBean) content);
                    break;
                case TYPE_TRANSFER:
                    cmd = transferFormat((TransferPrintBean) content);
                    break;

                default:
                    break;
            }

            data = cmd.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (mUsbAdmin.sendCommand(data) && mUsbAdmin.sendCommand(PRINTF_CUT)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否能打印
     */
    public static boolean isPrintfData(PrintfBean content, UsbAdmin mUsbAdmin) {

        boolean print = (boolean) ACache.get(App.getInstance().getApplicationContext()).getAsObject(Constants.WHETHER_PRINT);
        Log.d("HardwareUtil", "是否打印" + print);
        if (!print) {
            return false;
        }
        byte[] data = new byte[1024];
        String cmd = "";
        try {
            cmd = stockFlowFormat((StockFlowPrintBean) content);
            data = cmd.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
     /*   if (mUsbAdmin.sendCommand(data) && mUsbAdmin.sendCommand(PRINTF_CUT)) {
            return true;
        } else {
            return false;
        }*/
        return mUsbAdmin.sendCommand(data) && mUsbAdmin.sendCommand(PRINTF_CUT);
    }

    /**
     * 是否能打印
     */
    public static boolean isPrintfData(String content, UsbAdmin mUsbAdmin) {

        boolean print = (boolean) ACache.get(App.getInstance().getApplicationContext()).getAsObject(Constants.WHETHER_PRINT);
        Log.d("HardwareUtil", "是否打印" + print);
        if (!print) {
            return false;
        }
        byte[] data = new byte[1024];
        try {
            String cmd = testFormat();
            data = cmd.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (mUsbAdmin.sendCommand(data) && mUsbAdmin.sendCommand(PRINTF_CUT)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 打开钱箱
     *
     * @param mUsbAdmin
     * @return
     */
    public static boolean pushCash(UsbAdmin mUsbAdmin) {
        boolean canPush = false;
        mUsbAdmin.openUsb();
        mUsbAdmin.getUsbStatus();
        if (mUsbAdmin.sendCommand(PUSH_CASH)) {
            canPush = true;
        } else {
            canPush = false;
        }
        return canPush;
    }

    /**
     * 商品结算打印格式
     */
    public static String productCollectFormat(CollectionPrintBean content) throws UnsupportedEncodingException {
        String dataStr = null;
        String productListStr = "";
        String productName = "";
        String price = "";
        String num = "";
        String promotionPrice = "";
        /**
         * 这是打印
         * */
        for (int i = 0; i < content.getProductItemses().size(); i++) {
            //单个商品的优惠价格
            promotionPrice = content.getProductItemses().get(i).getPromotionPrice();
//            productName = CommonUtils.formatStr(content.getProductItemses().get(i).getName(), 30);
            productName = content.getProductItemses().get(i).getName();

            num = CommonUtils.formatStr(content.getProductItemses().get(i).getNum(), 7);
            //原价
            price = CommonUtils.formatStr(content.getProductItemses().get(i).getMomentPrice(), 8);
            if (Double.valueOf(promotionPrice) == 0) {
                productListStr += productName + cmdEnter()
                        + "\t" + price
                        + num
                        + content.getProductItemses().get(i).getTotal() + cmdEnter();
            } else {
                productListStr += productName + cmdEnter()
                        + "\t" + price
                        + num
                        + content.getProductItemses().get(i).getTotal() + cmdEnter() + "                    优惠:" + promotionPrice + cmdEnter();
            }

        }

        dataStr = "      云收银销售单据\n" +
                cmdEnter() +
                "门  店:" + content.getShopName() +
                cmdEnter() +
                "单据号:" + content.getSn() +
                cmdEnter() +
                "日  期:" + content.getTime() +
                cmdEnter() +
                "收银员:" + content.getCashierId() +
                cmdEnter() +
                "商  品\t 单价    数量   小计" +
                cmdEnter() +
                "--------------------------------\n" +
                productListStr +
                cmdEnter() +
                "\n--------------------------------" +
                "合计数量:" + content.getQuality() +
                cmdEnter() +
                "总额:" + content.getTotalCash() + "    \t实收:" + content.getTotalReceipts() +
                cmdEnter() +
                "总优惠:" + content.getDiscount() + "    \t积分:" + content.getTotalEcoin() +
                cmdEnter() +
                "支付方式:" + content.getTotalWeixin() +
                cmdEnter() +
                "会员卡号:" + content.getMemberCard() +
                cmdEnter() +
                "门店热线:" + content.getPhone() +
                cmdEnter() +
                "门店地址:" + content.getAddress() + "\n" +
                cmdEnter() +
                "谢谢惠顾\n" +
                cmdEnter();
        return dataStr;
    }

    /**
     * 打印收货单据
     *
     * @param stockFlowPrintBean
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String stockFlowFormat(StockFlowPrintBean stockFlowPrintBean) throws UnsupportedEncodingException {
        String printString = "";

        String stockFlowListString = "";

        String productName = "";

        String applyCount = "";

        String realCount = "";

        String unit = "";

        String realTotal = "0";

        for (StockFlowPrintBean.StockFlowToPrint stockFlowToPrint : stockFlowPrintBean.getStockFlowToPrintList()) {

            productName = CommonUtils.formatStr(stockFlowToPrint.getName(), 13);

            applyCount = CommonUtils.formatStr(stockFlowToPrint.getApplyCount(), 8);

            if (stockFlowToPrint.isReject()) {
                realCount = CommonUtils.formatStr("0", 8);
            } else {
                realCount = CommonUtils.formatStr(stockFlowToPrint.getRealCount(), 8);
            }

            unit = stockFlowToPrint.getUnit();

            stockFlowListString += productName
                    + applyCount
                    + realCount
                    + unit + cmdEnter();


            if (TextUtils.isEmpty(stockFlowToPrint.getRealCount()) || "0.0".equals(stockFlowToPrint.getRealCount())) {
                /*if (DecimalUtil.camporeTo(realCount,"0") == 1) //大于0
                realTotal = DecimalUtil.subtract(realTotal,"1");*/

                realTotal = DecimalUtil.add(realTotal, "0");
            } else {
                if (stockFlowToPrint.isWeighting()) {
                    if (stockFlowToPrint.isReject()) {
                        realTotal = DecimalUtil.add(realTotal, "0");
                    } else {
                        realTotal = DecimalUtil.add(realTotal, "1");
                    }
                } else {
                    if (stockFlowToPrint.isReject()) {
                        realTotal = DecimalUtil.add(realTotal, "0");
                    } else {
                        realTotal = DecimalUtil.add(realTotal, stockFlowToPrint.getRealCount());
                    }
                }
            }
        }
        String remark = TextUtils.isEmpty(stockFlowPrintBean.getRemark()) ? "散秤商品默认数量为一件" : stockFlowPrintBean.getRemark();
        printString = "\t云收银收货单据\n" +
                cmdEnter() +
                "门  店:" + stockFlowPrintBean.getShopName() +
                cmdEnter() +
                "单  号:" + stockFlowPrintBean.getSn() +
                cmdEnter() +
                "日  期:" + stockFlowPrintBean.getDate() +
                cmdEnter() +
                "收货员:" + stockFlowPrintBean.getCashierId() +
                cmdEnter() +
                "商  品\t     申请    实收   单位" +
                cmdEnter() +
                "--------------------------------\n" +
                stockFlowListString +
                cmdEnter() +
                "\n--------------------------------" +
                "申请总数:" + stockFlowPrintBean.getApplyTotal() +
                cmdEnter() +
                "实收总数:" + realTotal +
                cmdEnter() +
                "备注:" + remark +
                cmdEnter() +
                "门店热线:" + stockFlowPrintBean.getPhone() +
                cmdEnter() +
                "门店地址:" + stockFlowPrintBean.getAddress() + "\n" +
                cmdEnter();
        return printString;
    }


    /**
     * 交接班
     * 已完成
     */
    public static String transferFormat(TransferPrintBean content) {
        String dataStr = null;
        String productListStr = "";
        String productName = "";
        dataStr = "    云收银交接班单据\n" +
                cmdEnter() +
                "编号:" + content.getSn() + "\n" +
                cmdEnter() +
                "门店:" + content.getShopName() +
                cmdEnter() +
                "收银员:" + content.getCashierJobNumber() +
                cmdEnter() +
                "上班时间:" + content.getBeginTime() +
                cmdEnter() +
                "下班时间:" + content.getEndTime() +
                cmdEnter() +
                "--------------------------------\n" +
                "销售总单据数:" + content.getAllBill() +
                cmdEnter() +
                "门店单据:" + content.getShopBill() +
                cmdEnter() +
                "门店退货:" + content.getShopReturn() +
                cmdEnter() +
                "网店单据:" + content.getNetBill() +
                cmdEnter() +
                "网店退货:" + content.getNetReturn() +
                "\n--------------------------------" +
                cmdEnter() +
                "--------------------------------\n" +
                "总销售额:" + content.getAllMoney() +
                cmdEnter() +
                "现金支付:" + content.getCash() +
                cmdEnter() +
                "余额支付:" + content.getBalanceMoney() +
                cmdEnter() +
                "微信支付:" + content.getWeixin() +
                cmdEnter() +
                "刷卡支付:" + content.getPos() +
                cmdEnter() +
                "网店支付:" + content.getNetPay() +
                cmdEnter() +
                "门店退款:" + content.getShopReturnMoney() +
                cmdEnter() +
                "网店退款:" + content.getNetShopReturnMoney() +
                "\n--------------------------------" +
                cmdEnter() +
                "--------------------------------\n" +
                "线下总现金:" + content.getAllCash() +
                cmdEnter() +
                "商品现金收款:" + content.getMerchandiseCashMoney() +
                cmdEnter() +
                "会员卡现金充值:" + content.getBuyCardCash() +
                cmdEnter() +
                "备用金:" + content.getStandbyCash() +
                "\n--------------------------------" +
                cmdEnter() +
                "--------------------------------\n" +
                "会员卡充值总单据数:" + content.getAllCard() +
                cmdEnter() +
                "会员卡充值总金额:" + content.getAllPayCardMoney() +
                "\n--------------------------------" +
                cmdEnter() +
                "备注:" + content.getRemark() +
                cmdEnter() +
                "当前钱箱的现有金额:" + content.getContent() +
                cmdEnter();

        return dataStr;
    }

    /**
     * 购买会员卡打印格式
     */
    public static String buyVipCardFormat(CollectionPrintBean content) {
        String dataStr = null;
        String productListStr = "";
        String productName = "";

        for (int i = 0; i < content.getProductItemses().size(); i++) {
            if (content.getProductItemses().get(i).getName().length() > 8) {
                productName = content.getProductItemses().get(i).getName().substring(0, 8) + ".. ";
            } else {
                productName = content.getProductItemses().get(i).getName() + "  ";
            }
            productListStr += productName
                    + content.getProductItemses().get(i).getPrice() + "  ";
//                    + content.getProductItemses().get(i).getNum() + "  "
//                    + content.getProductItemses().get(i).getTotal() + cmdEnter();
        }

        dataStr = "\tVIP卡购买单据\n" +
                cmdEnter() +
                "门  店:" + content.getShopName() +
                cmdEnter() +
                "单据号:" + content.getSn() +
                cmdEnter() +
                "日 期:" + content.getTime() +
                cmdEnter() +
                "收银员:" + content.getCashierId() +
                cmdEnter() +
                "商  品\t      单价   数量   小计" +
                cmdEnter() +
                "--------------------------------\n" +
                productListStr +
                cmdEnter() +
                "\n--------------------------------" +
                "合计数量:" + content.getQuality() +
                cmdEnter() +
                "金额:" + content.getTotalCash() + "    " + "支付方式:" + content.getTotalWeixin() +
                cmdEnter() +
                "会员卡号:" + content.getMemberCard() +
                cmdEnter() +
                "门店热线:" + content.getPhone() +
                cmdEnter() +
                "门店地址:" + content.getAddress() + "\n" +
                cmdEnter() +
                "谢谢惠顾\n" +
                cmdEnter();

        return dataStr;
    }

    /**
     * 网络单据打印格式
     * 已完成
     */
    public static String webOrderFormat(WebOrderPrintBean content) throws UnsupportedEncodingException {
        String dataStr = null;
        String productListStr = "";
        String productName = "";
        String price = "";
        String num = "";

        for (int i = 0; i < content.getProductItemses().size(); i++) {

            productName = CommonUtils.formatStr(content.getProductItemses().get(i).getName(), 14);
            price = CommonUtils.formatStr(content.getProductItemses().get(i).getPrice(), 8);
            num = CommonUtils.formatStr(content.getProductItemses().get(i).getNum(), 3);
            // total = CommonUtils.formatStr(content.getProductItemses().get(i).getTotal(),5); 不需处理
            productListStr += productName
                    + price
                    + num
                    + content.getProductItemses().get(i).getTotal() + cmdEnter();
        }

        dataStr = "    云收银网络单据\n" +
                cmdEnter() +
                "门  店:" + content.getShopName() +
                cmdEnter() +
                "单据号:" + content.getSn() +
                cmdEnter() +
                "日 期:" + content.getTime() +
                cmdEnter() +
                content.getCashierId() +
                cmdEnter() +
                "商  品\t      单价   数量   小计" +
                cmdEnter() +
                "--------------------------------\n" +
                productListStr +
                cmdEnter() +
                "\n--------------------------------" +
                "合计数量:" + content.getQuality() +
                cmdEnter() +
                "总金额:" + content.getTotalWeixin() +
                cmdEnter() +
//                "优惠券:" + content.getDiscount() + "             " + "积分:" + content.getTotalECoin() +
                "积分:" + content.getTotalEcoin() +
                cmdEnter() +
                "会员卡号:" + content.getMemberCard() +
                cmdEnter() +
                "收货人:" + content.getUserName() +
                cmdEnter() +
                "联系方式:" + content.getUserPhone() +
                cmdEnter() +
                "配送费:" + content.getDeliveryFee() + "   " + "配送方式:" + content.getDeliveryType() +
                cmdEnter() +
                "配送地址:" + content.getUserAddress() +
                cmdEnter() +
                "门店热线:" + content.getPhone() +
                cmdEnter() +
                "门店地址:" + content.getAddress() + "\n" +
                "\n" +
                cmdEnter();

        return dataStr;
    }

    /***
     * 会员充值打印格式
     * @param content
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String memberRechargeFormat(MemberRechargePrintBean content) throws UnsupportedEncodingException {
        String dataStr = null;
        String productListStr = "";
        String productName = "";
        String price = "";
        String num = "";
//        for (int i = 0; i < content.getStockFlowToPrintList().size(); i++) {
//
//            productName = CommonUtils.formatStr(content.getStockFlowToPrintList().get(i).getName(), 14);
//            applyCount = CommonUtils.formatStr(content.getStockFlowToPrintList().get(i).getApplyCount(), 8);
//            realCount = CommonUtils.formatStr(content.getStockFlowToPrintList().get(i).getRealCount(), 3);
//            // total = CommonUtils.formatStr(content.getProductItemses().get(i).getTotal(),5); 不需处理
//            productListStr += productName
//                    + applyCount
//                    + realCount
//                    + "   "+content.getStockFlowToPrintList().get(i).getUnit() + cmdEnter();
//        }
        productName = CommonUtils.formatStr(content.getCommodityName(), 14);
        price = CommonUtils.formatStr(content.getPrice(), 8);
        num = CommonUtils.formatStr(content.getNum(), 5);
        productListStr += productName
                + price
                + num
                + content.getPrice() + cmdEnter();
        dataStr = "      云收银会员充值单据\n" +
                cmdEnter() +
                "门  店:" + content.getShopName() +
                cmdEnter() +
                "单号:" + content.getSn() +
                cmdEnter() +
                "日 期:" + content.getDate() +
                cmdEnter() +
                "收银员:" + content.getCashierId() +
                cmdEnter() +
                "商  品\t      单价   数量   小计" +
                cmdEnter() +
                "--------------------------------\n" +
                productListStr +
                cmdEnter() +
                "\n--------------------------------" +
                "合计:" + content.getPrice() +
                cmdEnter() +
                "数量:" + content.getNum() +
                cmdEnter() +
                "支付方式:" + content.getPaymentMethod() +
                cmdEnter() +
                "会员卡号:" + content.getMemberPhone() +
                cmdEnter() +
                "门店热线:" + content.getShopPhone() +
                cmdEnter() +
                "门店地址:" + content.getAddress() + "\n" +
                "\n" +
                cmdEnter();

        return dataStr;
    }

    public static String strokFlowFormat(StockFlowPrintBean content) throws UnsupportedEncodingException {
        String dataStr = null;
        String productListStr = "";
        String productName = "";
        String applyCount = "";
        String realCount = "";
        for (int i = 0; i < content.getStockFlowToPrintList().size(); i++) {

            productName = CommonUtils.formatStr(content.getStockFlowToPrintList().get(i).getName(), 14);
            applyCount = CommonUtils.formatStr(content.getStockFlowToPrintList().get(i).getApplyCount(), 8);
            realCount += CommonUtils.formatStr(content.getStockFlowToPrintList().get(i).getRealCount(), 8);
            // total = CommonUtils.formatStr(content.getProductItemses().get(i).getTotal(),5); 不需处理
            productListStr += productName
                    + applyCount
                    + realCount
                    + "   " + content.getStockFlowToPrintList().get(i).getUnit() + cmdEnter();
        }

        dataStr = "      云收银收货单据\n" +
                cmdEnter() +
                "门  店:" + content.getShopName() +
                cmdEnter() +
                "单号:" + content.getSn() +
                cmdEnter() +
                "日 期:" + content.getDate() +
                cmdEnter() +
                "收银员:" + content.getCashierId() +
                cmdEnter() +
                "商  品\t      申请   实收   单位" +
                cmdEnter() +
                "--------------------------------\n" +
                productListStr +
                cmdEnter() +
                "\n--------------------------------" +
                "申请总数:" + content.getApplyTotal() +
                cmdEnter() +
                "实收总数:" + realCount +
                cmdEnter() +
                "备注:" + content.getRemark() +
                cmdEnter() +
                "门店热线:" + content.getPhone() +
                cmdEnter() +
                "门店地址:" + content.getAddress() + "\n" +
                "\n" +
                cmdEnter();

        return dataStr;
    }

    /**
     * 测试格式
     */
    public static String testFormat() {
        String data = null;
        String time = DateUtil.getTime();
        data = "测试单据" +
                cmdEnter() +
                "------------------------------\n" +
                cmdEnter() +
                "收银员：1001\n" +
                "测试时间：  " + time + "\n" +
                "------------------------------\n" +
                cmdEnter();

        return data;
    }

    /**
     * 换行
     *
     * @return
     */
    public static String cmdEnter() {
        return new StringBuffer().append((char) 10).toString();
    }
}
