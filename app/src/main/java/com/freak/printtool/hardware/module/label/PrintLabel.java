package com.freak.printtool.hardware.module.label;

import android.content.Context;
import android.text.TextUtils;


import com.freak.printtool.hardware.app.Constants;
import com.freak.printtool.hardware.print.bean.ProductLabelBean;
import com.freak.printtool.hardware.utils.ACache;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PBarcodeType;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.exception.ParameterErrorException;
import com.printer.sdk.exception.PrinterPortNullException;
import com.printer.sdk.exception.WriteException;

import java.util.List;


/**
 * 标签打印模版
 *
 * @author Freak
 * @date 2019/8/13.
 */
public class PrintLabel {

    /**
     * 多个打印
     *
     * @param iPrinter
     * @param mContext
     * @param productLabelBeanList
     */
    public void doPrintTSPL(final PrinterInstance iPrinter, final Context mContext, final List<ProductLabelBean> productLabelBeanList) {
        final boolean printerDirection = (boolean) ACache.get(mContext.getApplicationContext()).getAsObject(Constants.PRINT_DIRECTION);
        android.util.Log.d("cai", "是否打印" + printerDirection);

        new Thread() {
            @Override
            public void run() {

                for (ProductLabelBean p : productLabelBeanList) {

                    int left = PrefUtils.getInt(mContext, "leftmargin", 0);
                    int top = PrefUtils.getInt(mContext, "topmargin", 0);
                    int numbers = PrefUtils.getInt(mContext, "printnumbers", 1);
                    int isBeep = PrefUtils.getInt(mContext, "isBeep", 0);

                    try {
                        // 设置标签纸大小 型号SIZE_58mm 尺寸56 * 8：45 * 8
                        //45
                        iPrinter.pageSetupTSPL(PrinterConstants.SIZE_80mm, 80 * 8, 50 * 8);

                        // 清除缓存区内容
                        iPrinter.printText("CLS\r\n");

                        // 设置标签的参考坐标原点
                        if (left == 0 || top == 0) {
                            // 不做设置，默认
                        } else {
                            iPrinter.sendStrToPrinterTSPL("REFERENCE " + left * 8 + "," + top * 8 + "\r\n");
                        }

                        /**
                         * x 是代表横的位置 ；y是行数，从0开始
                         * 前4 表明开始的位置 xy，开始和结束的位置
                         * 56 表明放置的位置
                         * 7 表明是简体中文
                         * 89 表明是xy的倍数：表明字体的大小
                         * 10 表明是是否旋转
                         * 11 表明是需要打印的字体
                         * */
                        /**
                         * 初始版
                         */

                        if (printerDirection) {

                            /**
                             * 以下为最终版
                             * 旋转180度
                             */
                            //名称
                            if (TextUtils.isEmpty(p.getName())) {
                                iPrinter.drawTextTSPL(50 * 8, 40 * 8 + 10, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, " ");
                            } else {
                                iPrinter.drawTextTSPL(50 * 8, 40 * 8 + 10, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getName());
                            }
                            // 横线上方区域右侧的文字
                            //零售价
                            if (TextUtils.isEmpty(p.getPrice())) {
                                iPrinter.drawTextTSPL(33 * 8, 28 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 2, 2, PrinterConstants.PRotate.Rotate_180, " ");
                            } else {
                                iPrinter.drawTextTSPL(33 * 8, 28 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 2, 2, PrinterConstants.PRotate.Rotate_180, p.getPrice());
                            }
                            // 横线上方区域右侧的文字 价格和E币，空格间隔
                            //会员价和e币
//                        iPrinter.drawTextTSPL(25 * 8, 11 * 8, 40 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getCashAndEPrice());
                            //会员价 TODO 已取消
//                            iPrinter.drawTextTSPL(33 * 8, 14 * 8 + 2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getCash());
                            //e币   最大数据个数限制为5位  例如：99.99
                            if (TextUtils.isEmpty(p.getEprice())) {
                                iPrinter.drawTextTSPL(17 * 8, 14 * 8 + 2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, " ");
                            } else {
                                iPrinter.drawTextTSPL(17 * 8, 14 * 8 + 2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getEprice());
                            }
                            //计量单位
                            if (TextUtils.isEmpty(p.getUnit())) {
                                iPrinter.drawTextTSPL(60 * 8, 20 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");
                            } else {
                                iPrinter.drawTextTSPL(60 * 8, 20 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getUnit());
                            }
                            // 二维码下方的一维条码 13位
                            // 14，15，16，17，18 no 12
                            //sn
                            if (TextUtils.isEmpty(p.getSn())) {
                                iPrinter.drawTextTSPL(60 * 8, 14 * 8 + 2, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");
                            } else {
                                iPrinter.drawTextTSPL(60 * 8, 14 * 8 + 2, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getSn());
                            }
                            //int start_x, int start_y, PBarcodeType type, int height, boolean isReadable, PRotate rotate, int narrowWidth, int wideWidth, String content
//                            iPrinter.drawBarCodeTSPL(75 * 8, 10 * 8 + 4, PBarcodeType.JAN3_EAN13, 5 * 8, false, null, 1, 1, p.getSn());
                            iPrinter.drawBarCodeTSPL(75 * 8, 10 * 8 + 4, PBarcodeType.JAN3_EAN13, 5 * 8, false, PrinterConstants.PRotate.Rotate_180, 1, 1, p.getSn());

                            //以下三条数据是不打印的，只是打印空格
                            //规格
                            if (TextUtils.isEmpty(p.getSpecification())) {
                                iPrinter.drawTextTSPL(60 * 8, 30 * 8 + 4, 70 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");
                            } else {
                                iPrinter.drawTextTSPL(60 * 8, 30 * 8 + 4, 70 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getSpecification());
                            }
                            // 横线上方区域左下方的文字 （(0,0)(40*8,7*8)）
                            //产地
                            if (TextUtils.isEmpty(p.getProducer())) {
                                iPrinter.drawTextTSPL(65 * 8, 25 * 8, 75 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");
                            } else {
                                iPrinter.drawTextTSPL(65 * 8, 25 * 8, 75 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getProducer());
                            }
                            //等级
                            if (TextUtils.isEmpty(p.getRank())) {
                                iPrinter.drawTextTSPL(48 * 8, 25 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");
                            } else {
                                iPrinter.drawTextTSPL(48 * 8, 25 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getRank());
                            }
                            //物价员
                            if (TextUtils.isEmpty(p.getSupervisor())) {
                                iPrinter.drawTextTSPL(14 * 8, 5 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");

                            } else {
                                iPrinter.drawTextTSPL(14 * 8, 5 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getSupervisor());
                            }

                        } else {
                            //名称
                            if (TextUtils.isEmpty(p.getName())) {
                                iPrinter.drawTextTSPL(30 * 8, 11 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, null, " ");
                            } else {
                                iPrinter.drawTextTSPL(30 * 8, 11 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, null, p.getName());
                            }
                            // 横线上方区域右侧的文字
                            //零售价
                            if (TextUtils.isEmpty(p.getPrice())) {
                                iPrinter.drawTextTSPL(48 * 8, 23 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 2, 2, null, " ");
                            } else {
                                iPrinter.drawTextTSPL(48 * 8, 23 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 2, 2, null, p.getPrice());
                            }
                            // 横线上方区域右侧的文字 价格和E币，空格间隔
                            //会员价和e币
//                        iPrinter.drawTextTSPL(49 * 8, 38 * 8, 70 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 2, null, p.getCashAndEPrice());
                            //会员价 TODO 已取消
//                            iPrinter.drawTextTSPL(47 * 8, 38 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, null, p.getCash());
                            //打印积分
                            if (TextUtils.isEmpty(p.getEprice())) {
                                iPrinter.drawTextTSPL(63 * 8, 38 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, null, " ");

                            } else {
                                iPrinter.drawTextTSPL(63 * 8, 38 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, null, p.getEprice());

                            }

                            //计量单位
                            if (TextUtils.isEmpty(p.getUnit())) {
                                iPrinter.drawTextTSPL(18 * 8, 32 * 8, 40 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, " ");
                            } else {
                                iPrinter.drawTextTSPL(18 * 8, 32 * 8, 40 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, p.getUnit());
                            }
                            // 二维码下方的一维条码 13位
                            // 14，15，16，17，18 no 12
                            //sn
                            if (TextUtils.isEmpty(p.getSn())) {
                                iPrinter.drawTextTSPL(12 * 8, 37 * 8 + 2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, " ");
                            } else {
                                iPrinter.drawTextTSPL(12 * 8, 37 * 8 + 2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, p.getSn());
                            }

                            //int start_x, int start_y, PBarcodeType type, int height, boolean isReadable, PRotate rotate, int narrowWidth, int wideWidth, String content
                            iPrinter.drawBarCodeTSPL(40, 40 * 8 + 4, PBarcodeType.JAN3_EAN13, 5 * 8, false, null, 1, 1, p.getSn());

                            //物价员
                            if (TextUtils.isEmpty(p.getSupervisor())) {
                                iPrinter.drawTextTSPL(65 * 8, 47 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, " ");
                            } else {
                                iPrinter.drawTextTSPL(65 * 8, 47 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, p.getSupervisor());
                            }

                        }
//                        //名称
//                        iPrinter.drawTextTSPL(14 * 8, 11 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, null, p.getName());
//
//                        // 横线上方区域右侧的文字
//                        //零售价
//                        iPrinter.drawTextTSPL(48 * 8, 23 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 2, 2, null, p.getPrice());
//
//                        // 横线上方区域右侧的文字 价格和E币，空格间隔
//                        //会员价和e币
//                        iPrinter.drawTextTSPL(49 * 8, 38 * 8, 70 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 2, null, p.getCashAndEPrice());
//
//                        //计量单位
//                        iPrinter.drawTextTSPL(18 * 8, 32 * 8, 40 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, p.getUnit());
//
//                        // 二维码下方的一维条码 13位
//                        // 14，15，16，17，18 no 12
//                        //sn
//                        iPrinter.drawTextTSPL(12 * 8, 37 * 8 + 2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, p.getSn());
//
//                        //int start_x, int start_y, PBarcodeType type, int height, boolean isReadable, PRotate rotate, int narrowWidth, int wideWidth, String content
//                        iPrinter.drawBarCodeTSPL(40, 40 * 8 + 4, PBarcodeType.JAN3_EAN13, 5 * 8, false, null, 1, 1, p.getSn());


//                        /**
//                         * 以下为最终版
//                         */
//                        //名称
//                        iPrinter.drawTextTSPL(50 * 8, 40 * 8+10, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getName());
//
//                        // 横线上方区域右侧的文字
//                        //零售价
//                        iPrinter.drawTextTSPL(33 * 8, 28 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 2, 2, PrinterConstants.PRotate.Rotate_180, p.getPrice());
//
//                        // 横线上方区域右侧的文字 价格和E币，空格间隔
//                        //会员价和e币
////                        iPrinter.drawTextTSPL(25 * 8, 11 * 8, 40 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getCashAndEPrice());
//                       //会员价
//                        iPrinter.drawTextTSPL(33 * 8, 14 * 8+2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getCash());
//                        //e币   最大数据个数限制为5位  例如：99.99
//                        iPrinter.drawTextTSPL(17 * 8, 14 * 8+2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getEprice());
//
//                        //计量单位
//                        iPrinter.drawTextTSPL(60 * 8, 20 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getUnit());
//
//                        // 二维码下方的一维条码 13位
//                        // 14，15，16，17，18 no 12
//                        //sn
//                        iPrinter.drawTextTSPL(60 * 8, 14 * 8 + 2, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getSn());
//
//                        //int start_x, int start_y, PBarcodeType type, int height, boolean isReadable, PRotate rotate, int narrowWidth, int wideWidth, String content
//                        iPrinter.drawBarCodeTSPL(75*8, 10 * 8+4  , PBarcodeType.JAN3_EAN13, 5 * 8, false, null, 1, 1, p.getSn());
//                        //以下三条数据是不打印的，只是打印空格
//                        //规格
//                        iPrinter.drawTextTSPL(60 * 8, 30 * 8 + 4, 70 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");
//                        // 横线上方区域左下方的文字 （(0,0)(40*8,7*8)）
//                        //产地
//                        iPrinter.drawTextTSPL(65 * 8 , 25 * 8, 75 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180," ");
//
//                        //等级
//                        iPrinter.drawTextTSPL(48 * 8, 25 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");


                        // 判断是否响应蜂鸣器
                        if (isBeep == 1) {
                            // 打印前响
                            iPrinter.beepTSPL(1, 1000);
                            Thread.sleep(3000);
                            // 打印
                            iPrinter.printTSPL(numbers, 1);
                        } else if (isBeep == 2) {
                            // 打印
                            iPrinter.printTSPL(numbers, 1);
                            // 打印后响
                            iPrinter.beepTSPL(1, 1000);
                        } else {
                            // 打印
                            iPrinter.printTSPL(numbers, 1);
                        }

                    } catch (WriteException e) {
                        e.printStackTrace();
                    } catch (PrinterPortNullException e) {
                        e.printStackTrace();
                    } catch (ParameterErrorException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }.start();
    }


//    //多个打印
//    public void doPrintTSPL(final PrinterInstance iPrinter, final Context mContext, final List<ProductLabelBean> productLabelBeanList) {
//        new Thread() {
//            @Override
//            public void run() {
//
//                for (ProductLabelBean p : productLabelBeanList) {
//
//                    int left = PrefUtils.getInt(mContext, "leftmargin", 0);
//                    int top = PrefUtils.getInt(mContext, "topmargin", 0);
//                    int numbers = PrefUtils.getInt(mContext, "printnumbers", 1);
//                    int isBeep = PrefUtils.getInt(mContext, "isBeep", 0);
//
//                    try {
//                        // 设置标签纸大小 型号SIZE_58mm 尺寸56 * 8：45 * 8
//                        //45
//                        iPrinter.pageSetupTSPL(PrinterConstants.SIZE_80mm, 80 * 8, 50 * 8);
//
//                        // 清除缓存区内容
//                        iPrinter.printText("CLS\r\n");
//
//                        // 设置标签的参考坐标原点
//                        if (left == 0 || top == 0) {
//                            // 不做设置，默认
//                        } else {
//                            iPrinter.sendStrToPrinterTSPL("REFERENCE " + left * 8 + "," + top * 8 + "\r\n");
//                        }
//
//                        /**
//                         * x 是代表横的位置 ；y是行数，从0开始
//                         * 前4 表明开始的位置 xy，开始和结束的位置
//                         * 56 表明放置的位置
//                         * 7 表明是简体中文
//                         * 89 表明是xy的倍数：表明字体的大小
//                         * 10 表明是是否旋转
//                         * 11 表明是需要打印的字体
//                         * */
//
//                        //名称
//                        iPrinter.drawTextTSPL(14 * 8, 11 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, null, p.getName());
//
//                        // 横线上方区域右侧的文字
//                        //零售价
//                        iPrinter.drawTextTSPL(48 * 8, 23 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 2, 2, null, p.getPrice());
//
//                        // 横线上方区域右侧的文字 价格和积分，空格间隔
//                        //会员价和积分
//                        iPrinter.drawTextTSPL(49 * 8, 38 * 8, 70 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 2, null, p.getCashAndEPrice());
//
//                        //计量单位
//                        iPrinter.drawTextTSPL(18 * 8, 32 * 8, 40 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, p.getUnit());
//
//                        // 二维码下方的一维条码 13位
//                        // 14，15，16，17，18 no 12
//                        //sn
//                        iPrinter.drawTextTSPL(12 * 8, 37 * 8 + 2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, p.getSn());
//
//                        //int start_x, int start_y, PBarcodeType type, int height, boolean isReadable, PRotate rotate, int narrowWidth, int wideWidth, String content
//                        iPrinter.drawBarCodeTSPL(40, 40 * 8 + 4, PBarcodeType.JAN3_EAN13, 5 * 8, false, null, 1, 1, p.getSn());
//
//                        // 判断是否响应蜂鸣器
//                        if (isBeep == 1) {
//                            // 打印前响
//                            iPrinter.beepTSPL(1, 1000);
//                            Thread.sleep(3000);
//                            // 打印
//                            iPrinter.printTSPL(numbers, 1);
//                        } else if (isBeep == 2) {
//                            // 打印
//                            iPrinter.printTSPL(numbers, 1);
//                            // 打印后响
//                            iPrinter.beepTSPL(1, 1000);
//                        } else {
//                            // 打印
//                            iPrinter.printTSPL(numbers, 1);
//                        }
//
//                    } catch (WriteException e) {
//                        e.printStackTrace();
//                    } catch (PrinterPortNullException e) {
//                        e.printStackTrace();
//                    } catch (ParameterErrorException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//
//        }.start();
//    }

//    //打印测试
//    public void doPrintTSPL(final PrinterInstance iPrinter, final Context mContext, final ProductLabelBean p) {
//        new Thread() {
//            @Override
//            public void run() {
//
//                int left = PrefUtils.getInt(mContext, "leftmargin", 0);
//                int top = PrefUtils.getInt(mContext, "topmargin", 0);
//                int numbers = PrefUtils.getInt(mContext, "printnumbers", 1);
//                int isBeep = PrefUtils.getInt(mContext, "isBeep", 0);
//
//                try {
//                    // 设置标签纸大小 型号SIZE_58mm 尺寸56 * 8：45 * 8
//                    iPrinter.pageSetupTSPL(PrinterConstants.SIZE_80mm, 80 * 8, 50 * 8);
//
//                    // 清除缓存区内容
//                    iPrinter.printText("CLS\r\n");
//
//                    // 设置标签的参考坐标原点
//                    if (left == 0 || top == 0) {
//                        // 不做设置，默认
//                    } else {
//                        iPrinter.sendStrToPrinterTSPL("REFERENCE " + left * 8 + "," + top * 8 + "\r\n");
//                    }
//
//                    /**
//                     * x 是代表横的位置 ；y是行数，从0开始
//                     * 前4 表明开始的位置 xy，开始和结束的位置
//                     * 56 表明放置的位置
//                     * 7 表明是简体中文
//                     * 89 表明是xy的倍数：表明字体的大小
//                     * 10 表明是是否旋转
//                     * 11 表明是需要打印的字体
//                     * */
//
//                    //名称
//                    iPrinter.drawTextTSPL(14 * 8, 11 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, null, p.getName());
//
//                    // 横线上方区域
//                    //规格
//                    iPrinter.drawTextTSPL(12 * 8, 19 * 8 + 4, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, "规格1");
//                    // 横线上方区域左下方的文字 （(0,0)(40*8,7*8)）
//                    //产地
//                    iPrinter.drawTextTSPL(11 * 8 + 4, 26 * 8, 25 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, p.getProducer());
//
//                    //等级
//                    iPrinter.drawTextTSPL(31 * 8, 26 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, "一级品");
//
//                    // 横线上方区域右侧的文字
//                    //零售价
//                    iPrinter.drawTextTSPL(48 * 8, 24 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 2, 2, null, p.getPrice());
//
//                    // 横线上方区域右侧的文字 价格和积分，空格间隔
//                    //会员价和积分
//                    iPrinter.drawTextTSPL(49 * 8, 38 * 8, 70 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 2, null, p.getCashAndEPrice());
//
//                    //计量单位
//                    iPrinter.drawTextTSPL(18 * 8, 32 * 8, 40 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, p.getUnit());
//
//                    // 二维码下方的一维条码 13位
//                    //sn
//                    iPrinter.drawTextTSPL(12 * 8, 37 * 8 + 2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, p.getSn());
//
//                    //int start_x, int start_y, PBarcodeType type, int height, boolean isReadable, PRotate rotate, int narrowWidth, int wideWidth, String content
//                    iPrinter.drawBarCodeTSPL(40, 40 * 8 + 4, PBarcodeType.JAN3_EAN13, 5 * 8, false, null, 1, 1, p.getSn());
//
//                    // 判断是否响应蜂鸣器
//                    if (isBeep == 1) {
//                        // 打印前响
//                        iPrinter.beepTSPL(1, 1000);
//                        Thread.sleep(3000);
//                        // 打印
//                        iPrinter.printTSPL(numbers, 1);
//                    } else if (isBeep == 2) {
//                        // 打印
//                        iPrinter.printTSPL(numbers, 1);
//                        // 打印后响
//                        iPrinter.beepTSPL(1, 1000);
//                    } else {
//                        // 打印
//                        iPrinter.printTSPL(numbers, 1);
//                    }
//
//                } catch (WriteException e) {
//                    e.printStackTrace();
//                } catch (PrinterPortNullException e) {
//                    e.printStackTrace();
//                } catch (ParameterErrorException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }.start();
//    }


    /**
     * 打印测试
     *
     * @param iPrinter
     * @param mContext
     * @param p
     */
    public void doPrintTSPL(final PrinterInstance iPrinter, final Context mContext, final ProductLabelBean p) {
        final boolean printerDirection = (boolean) ACache.get(mContext.getApplicationContext()).getAsObject(Constants.PRINT_DIRECTION);

        new Thread() {
            @Override
            public void run() {
                int left = PrefUtils.getInt(mContext, "leftmargin", 0);
                int top = PrefUtils.getInt(mContext, "topmargin", 0);
                int numbers = PrefUtils.getInt(mContext, "printnumbers", 1);
                int isBeep = PrefUtils.getInt(mContext, "isBeep", 0);
                int printlevel = PrefUtils.getInt(mContext, "printlevel", 7);

                try {
                    // 设置标签纸大小 型号SIZE_58mm 尺寸56 * 8：45 * 8
                    iPrinter.pageSetupTSPL(PrinterConstants.SIZE_80mm, 80 * 8, 50 * 8);

                    // 清除缓存区内容
                    iPrinter.printText("CLS\r\n");

                    // 设置标签的参考坐标原点
                    if (left == 0 || top == 0) {
                        // 不做设置，默认
                    } else {
                        iPrinter.sendStrToPrinterTSPL("REFERENCE " + left * 8 + "," + top * 8 + "\r\n");
                    }

                    /**
                     * x 是代表横的位置 ；y是行数，从0开始
                     * 前4 表明开始的位置 xy，开始和结束的位置
                     * 56 表明放置的位置
                     * 7 表明是简体中文
                     * 89 表明是xy的倍数：表明字体的大小
                     * 10 表明是是否旋转
                     * 11 表明是需要打印的字体
                     * */


//                    //名称
//                    iPrinter.drawTextTSPL(50 * 8, 40 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getName());
//
//                    // 横线上方区域
//                    //规格
//                    iPrinter.drawTextTSPL(60 * 8, 30 * 8 + 4, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, "规格1");
//                    // 横线上方区域左下方的文字 （(0,0)(40*8,7*8)）
//                    //产地
//                    iPrinter.drawTextTSPL(65 * 8 , 25 * 8, 75 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getProducer());
//
//                    //等级
//                    iPrinter.drawTextTSPL(48 * 8, 25 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, "一级品");
//
//                    // 横线上方区域右侧的文字
//                    //零售价
//                    iPrinter.drawTextTSPL(30 * 8, 28 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 2, 2, PrinterConstants.PRotate.Rotate_180, p.getPrice());
//
//                    // 横线上方区域右侧的文字 价格和E币，空格间隔
//                    //会员价和e币
//                    iPrinter.drawTextTSPL(25 * 8, 11 * 8, 40 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getCashAndEPrice());
//
//                    //计量单位
//                    iPrinter.drawTextTSPL(60 * 8, 16 * 8, 40 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getUnit());
//
//                    // 二维码下方的一维条码 13位
//                    //sn
//                    iPrinter.drawTextTSPL(60 * 8, 14 * 8 + 2, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getSn());
//
//                    //int start_x, int start_y, PBarcodeType type, int height, boolean isReadable, PRotate rotate, int narrowWidth, int wideWidth, String content
//                    iPrinter.drawBarCodeTSPL(75*8, 10 * 8 , PBarcodeType.JAN3_EAN13, 5 * 8, false, null, 1, 1, p.getSn());


                    /**
                     * 1、对应开始位置的x坐标
                     * 2、对应开始位置的y坐标
                     * 3、对应结束位置的x坐标
                     * 4、对应结束位置的y坐标
                     * 5、x坐标的对齐方式
                     * 6、y坐标的对齐方式
                     * 7、是否是简体中文
                     * 8、x字体大小
                     * 9、y字体大小
                     * 10、旋转方式
                     * 11、打印的数据
                     */
                    /***
                     * 第二版最、最终版
                     */
//                    //名称 最大数据个数限制为10位
//                    iPrinter.drawTextTSPL(50 * 8, 40 * 8+10, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getName());
//                    //零售价  最大数据个数限制为10位  例如：3000000.01
//
//                    iPrinter.drawTextTSPL(33 * 8, 28 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 2, 2, PrinterConstants.PRotate.Rotate_180, p.getPrice());
//                    // 横线上方区域右侧的文字 价格和E币，空格间隔
//                    //会员价  最大数据个数限制为8位  例如：200000.01
//
//                    iPrinter.drawTextTSPL(33 * 8, 14 * 8+2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getCash());
//                    //e币   最大数据个数限制为5位  例如：99.99
//                    iPrinter.drawTextTSPL(17 * 8, 14 * 8+2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getEprice());
//
//                    //计量单位 字数最大限制为6位
//                    iPrinter.drawTextTSPL(60 * 8, 20 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, "包");
//
//                    // 二维码下方的一维条码 13位
//                    //sn
//                    /***
//                     * 上面有
//                     */
//                    iPrinter.drawTextTSPL(60 * 8, 14 * 8 + 2, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getSn());
//                    /***
//                     * 上面有
//                     */
//                    //int start_x, int start_y, PBarcodeType type, int height, boolean isReadable, PRotate rotate, int narrowWidth, int wideWidth, String content
//                    iPrinter.drawBarCodeTSPL(75*8, 10 * 8+4 , PBarcodeType.JAN3_EAN13, 5 * 8, false, null, 1, 1, p.getSn());
//                    // 横线上方区域
//                    //规格
//                    iPrinter.drawTextTSPL(60 * 8, 30 * 8 + 4, 70 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");
//                    // 横线上方区域左下方的文字 （(0,0)(40*8,7*8)）
//                    //产地
//                    iPrinter.drawTextTSPL(65 * 8 , 25 * 8, 75 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180," ");
//
//                    //等级
//                    iPrinter.drawTextTSPL(48 * 8, 25 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");

//                    // 横线上方区域右侧的文字

                    /**
                     * 测试一、二版结合选择
                     */

                    if (printerDirection) {

                        /**
                         * 以下为最终版
                         * 旋转版
                         */
                        //名称
                        iPrinter.drawTextTSPL(50 * 8, 40 * 8 + 10, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getName());

                        // 横线上方区域右侧的文字
                        //零售价
                        iPrinter.drawTextTSPL(33 * 8, 28 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 2, 2, PrinterConstants.PRotate.Rotate_180, p.getPrice());

                        // 横线上方区域右侧的文字 价格和E币，空格间隔
                        //会员价和e币
//                        iPrinter.drawTextTSPL(25 * 8, 11 * 8, 40 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getCashAndEPrice());
                        //会员价
                        iPrinter.drawTextTSPL(33 * 8, 14 * 8 + 2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getCash());
                        //e币   最大数据个数限制为5位  例如：99.99
                        iPrinter.drawTextTSPL(17 * 8, 14 * 8 + 2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, PrinterConstants.PRotate.Rotate_180, p.getEprice());

                        //计量单位
                        iPrinter.drawTextTSPL(60 * 8, 20 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getUnit());

                        // 二维码下方的一维条码 13位
                        // 14，15，16，17，18 no 12
                        //sn
                        iPrinter.drawTextTSPL(60 * 8, 14 * 8 + 2, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, p.getSn());

                        //int start_x, int start_y, PBarcodeType type, int height, boolean isReadable, PRotate rotate, int narrowWidth, int wideWidth, String content
                        iPrinter.drawBarCodeTSPL(75 * 8, 10 * 8 + 4, PBarcodeType.JAN3_EAN13, 5 * 8, false, PrinterConstants.PRotate.Rotate_180, 1, 1, p.getSn());
                        //以下三条数据是不打印的，只是打印空格
                        //规格
                        iPrinter.drawTextTSPL(60 * 8, 30 * 8 + 4, 70 * 8, 7 * 8, PAlign.CENTER, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");
                        // 横线上方区域左下方的文字 （(0,0)(40*8,7*8)）
                        //产地
                        iPrinter.drawTextTSPL(65 * 8, 25 * 8, 75 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");

                        //等级
                        iPrinter.drawTextTSPL(48 * 8, 25 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, " ");
                        //TODO：2018-02-10 需要调试位置
                        //物价员
                        iPrinter.drawTextTSPL(14 * 8, 5 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, PrinterConstants.PRotate.Rotate_180, "物价员");
                    } else {
                        /**
                         * 不旋转版
                         */
                        //名称
                        iPrinter.drawTextTSPL(17 * 8, 11 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, null, p.getName());

                        // 横线上方区域右侧的文字
                        //零售价
                        iPrinter.drawTextTSPL(48 * 8, 23 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 2, 2, null, p.getPrice());

                        // 横线上方区域右侧的文字 价格和E币，空格间隔
                        //会员价和e币
                        iPrinter.drawTextTSPL(47 * 8, 38 * 8, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, null, p.getCash());
                        //打印e币
                        iPrinter.drawTextTSPL(63 * 8, 38 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 2, null, p.getEprice());

                        //计量单位
                        iPrinter.drawTextTSPL(18 * 8, 32 * 8, 40 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, p.getUnit());

                        // 二维码下方的一维条码 13位
                        // 14，15，16，17，18 no 12
                        //sn
                        iPrinter.drawTextTSPL(12 * 8, 37 * 8 + 2, 70 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, p.getSn());

                        //int start_x, int start_y, PBarcodeType type, int height, boolean isReadable, PRotate rotate, int narrowWidth, int wideWidth, String content
                        iPrinter.drawBarCodeTSPL(40, 40 * 8 + 4, PBarcodeType.JAN3_EAN13, 5 * 8, false, null, 1, 1, p.getSn());
                        //物价员
                        iPrinter.drawTextTSPL(65 * 8, 47 * 8, 80 * 8, 7 * 8, PAlign.START, PAlign.START, true, 1, 1, null, "物价员");
                    }
                    // 判断是否响应蜂鸣器
                    if (isBeep == 1) {
                        // 打印前响
                        iPrinter.beepTSPL(1, 1000);
                        Thread.sleep(3000);
                        // 打印
                        iPrinter.printTSPL(numbers, 1);
                    } else if (isBeep == 2) {
                        // 打印
                        iPrinter.printTSPL(numbers, 1);
                        // 打印后响
                        iPrinter.beepTSPL(1, 1000);
                    } else {
                        // 打印
                        iPrinter.printTSPL(numbers, 1);
                    }

                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (PrinterPortNullException e) {
                    e.printStackTrace();
                } catch (ParameterErrorException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }
}
