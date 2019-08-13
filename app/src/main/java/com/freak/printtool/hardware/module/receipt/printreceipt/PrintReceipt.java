package com.freak.printtool.hardware.module.receipt.printreceipt;

import android.graphics.Bitmap;


import com.freak.printtool.hardware.app.App;
import com.freak.printtool.hardware.app.Constants;
import com.freak.printtool.hardware.print.bean.PrintfBean;
import com.freak.printtool.hardware.utils.ACache;
import com.freak.printtool.hardware.utils.ToastUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.posin.usbprinter.UsbPrinter;

import java.io.IOException;

/**
 * 小票打印类
 *
 * @author Freak
 * @date 2019/8/13.
 */

public class PrintReceipt {
    /**
     * 打印的数据
     */
    private static String cmd = "";

    public static boolean receiptPrint(final PrintfBean printfBean, int type) {
        Boolean print = (Boolean) ACache.get(App.getInstance().getApplicationContext()).getAsObject(Constants.WHETHER_PRINT);
        final UsbPrinter usbPrinter = App.getInstance().getUsbPrinter();
        final PrintCategory mPrintCategory = App.getInstance().getMpPrintCategory();

        if (usbPrinter == null) {
            ToastUtil.shortShow("小票打印机未连接");
            return false;
        } else {

            if ((print == null || !print)) {
                return false;
            }

//                switch (type) {
//                    case TYPE_COLLECTION:
//                        cmd = productCollectFormat((CollectionPrintBean) printfBean);//收银订单/销售订单
//                        break;
//                    case TYPE_WEB_ORDER:
//                        cmd = webOrderFormat((WebOrderPrintBean) printfBean);//网络订单
//                        break;
//                    case TYPE_VIP_CARD:
//                        cmd = buyVipCardFormat((CollectionPrintBean) printfBean);
//                        break;
//                    case TYPE_TRANSFER:
//                        cmd = transferFormat((TransferPrintBean) printfBean);//交接班
//                        break;
//                    case TYPE_STOCK:
//                        cmd = strokFlowFormat((StockFlowPrintBean) printfBean);//货流通知
//                        break;
//                    case MEMBER_RECHARGE:
//                        cmd = memberRechargeFormat((MemberRechargePrintBean) printfBean);
//                        break;
//
//                    default:
//                        break;
//                }

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        usbPrinter.resetInit();
                        usbPrinter.selectAlignment(UsbPrinter.ALIGNMENT.LEFT);//设置对齐方式
                        Bitmap qrBitMap = null;
                        qrBitMap = mPrintCategory.createCode("http://www.hao123.com", BarcodeFormat.QR_CODE, 256, 256);//设置打印二维码的数据和宽高
                        Bitmap barBitMap = mPrintCategory.createCode("201801050943308072", BarcodeFormat.CODE_128, 350, 40);//设置条形码的数据和宽高
                        usbPrinter.selectFONT(UsbPrinter.FONT.FONT_A);//设置字体
                        usbPrinter.selectBold(false);//设置是否加粗
                        usbPrinter.selectUnderlined(false);//设置是否有下划线
                        usbPrinter.doubleFontSize(false, false);//设置是否放大
                        usbPrinter.setLineSpace(0);//设置间距
                        usbPrinter.printString(cmd);//打印正文
//                            usbPrinter.selectAlignment(UsbPrinter.ALIGNMENT.RIGHT);//设置对齐方式
//                            usbPrinter.printBitmapByLine(barBitMap);//打印条形码
//                            usbPrinter.printBitmapByLine(qrBitMap);//打印二维码
                        usbPrinter.walkPaper(3);//走纸
                        usbPrinter.cutPaper();//切纸
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (WriterException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            return true;
        }
    }


    /**
     * 打印测试
     *
     * @return
     */
    public boolean printReceiptTest() {
        final UsbPrinter usbPrinter = App.getInstance().getUsbPrinter();
        final PrintCategory mPrintCategory = App.getInstance().getMpPrintCategory();
        if (usbPrinter == null) {
            ToastUtil.shortShow("小票打印机未连接");
            return false;
        } else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        usbPrinter.resetInit();
                        usbPrinter.selectAlignment(UsbPrinter.ALIGNMENT.LEFT);//设置对齐方式
                        Bitmap qrBitMap = null;
                        qrBitMap = mPrintCategory.createCode("http://www.hao123.com", BarcodeFormat.QR_CODE, 256, 256);
                        Bitmap barBitMap = mPrintCategory.createCode("201801050943308072", BarcodeFormat.CODE_128, 350, 40);
                        usbPrinter.selectFONT(UsbPrinter.FONT.FONT_A);
                        usbPrinter.selectBold(false);
                        usbPrinter.selectUnderlined(false);
                        usbPrinter.doubleFontSize(false, false);
                        usbPrinter.setLineSpace(0);
                        usbPrinter.printString("小票打印机打印测试\n");
                        usbPrinter.printBitmapByLine(barBitMap);
                        usbPrinter.selectAlignment(UsbPrinter.ALIGNMENT.RIGHT);
                        usbPrinter.printBitmapByLine(qrBitMap);
                        usbPrinter.walkPaper(1);
                        usbPrinter.cutPaper();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (WriterException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            return true;
        }
    }

}
