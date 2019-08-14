package com.freak.printtool.hardware.module.wifi.printerutil;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import com.freak.printtool.hardware.utils.LogUtil;
import com.freak.printtool.hardware.utils.ToastUtil;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.service.GpPrintService;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

/**
 * @author Freak
 * @date 2019/8/13.
 */

public class PrinterUtil {
    private PrinterServiceConnection conn;
    private BroadCastReceiver mBroadcastReceiver;
    private GpService mGpService;
    private Context context;
    private boolean isBind;

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "onServiceDisconnected() called");
            mGpService = null;
        }
    }

    public PrinterUtil(Context context) {
        this.context = context;
        mBroadcastReceiver = new BroadCastReceiver();
        connection();
        doRegisterReceiver();
    }

    /**
     * 连接 开启服务
     *
     * @param
     */
    private void connection() {
        LogUtil.e("开启打印服务");
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(context, GpPrintService.class);
        // bindService
        isBind = context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * 注册广播
     *
     * @param
     */
    public void doRegisterReceiver() {
        // 注册实时状态查询广播
        context.registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_DEVICE_REAL_STATUS));
        /**
         * 票据模式下，可注册该广播，在需要打印内容的最后加入addQueryPrinterStatus()，在打印完成后会接收到
         * action为GpCom.ACTION_DEVICE_STATUS的广播，特别用于连续打印，
         * 可参照该sample中的sendReceiptWithResponse方法与广播中的处理
         **/
        context.registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_RECEIPT_RESPONSE));
        /**
         * 标签模式下，可注册该广播，在需要打印内容的最后加入addQueryPrinterStatus(RESPONSE_MODE mode)
         * ，在打印完成后会接收到，action为GpCom.ACTION_LABEL_RESPONSE的广播，特别用于连续打印，
         * 可参照该sample中的sendLabelWithResponse方法与广播中的处理
         **/
        context.registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_LABEL_RESPONSE));
    }

    /**
     * 连接打印机
     */
    public void connectToDevice(String ip, int port) {
        int rel = 0;
        try {
            rel = mGpService.openPort(0, 3, ip, port);
            LogUtil.e("开始连接");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];

        if (r == GpCom.ERROR_CODE.SUCCESS) {
            LogUtil.e("打印机连接成功");
        } else {
            LogUtil.e("连接失败原因--》" + GpCom.getErrorText(r));
        }
        LogUtil.e("打印机连接状态--》" + GpCom.getErrorText(r));

    }

    /**
     * 断开连接、关闭端口
     */
    public void disConnectToDevice() {
        try {
            mGpService.closePort(0);
            LogUtil.e("关闭端口");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /***
     * 查询打印机连接状态
     */
    public void checkPrintLinStatus() {
        try {
            int printStatus = mGpService.getPrinterConnectStatus(0);
            switch (printStatus) {
                case 0:
                    LogUtil.e("连接断开");
                    break;
                case 1:
                    LogUtil.e("监听状态");
                    break;
                case 2:
                    LogUtil.e("正在连接");
                    break;
                case 3:
                    LogUtil.e("已连接");
                    break;
                default:
                    break;
            }
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * 后台控制打印格式方式打印测试
     *
     * @param arg
     */
    public void printFormat(String arg) {
        Map<String, Object> styleMap = new HashMap<>();
        styleMap.put("<center>", "CENTER");
        styleMap.put("<center_bold>", "CENTER_BOLD");
        styleMap.put("<title>", "TITLE");
        styleMap.put("<left>", "LEFT");
        styleMap.put("<left_bold>", "LEFT_BOLD");
        styleMap.put("<right>", "RIGHT");
        styleMap.put("<right_bold>", "RIGHT_BOLD");
        styleMap.put("<bold>", "BOLD");

        Stack stack = new Stack<String>();
        StringBuffer sb = new StringBuffer();
        int pos = 0;
        boolean flag;
        int start = 0;
        int end = 0;
        String type = "";
        List<String> typeList = new ArrayList<>();
        List<String> printTextList = new ArrayList<>();
        while (arg.length() > pos) {
            flag = true;
            if (arg.charAt(pos) == '<') {
                for (Object key : styleMap.keySet()) {
                    if (arg.length() > (pos + key.toString().length()) && arg.substring(pos, pos + key.toString().length()).equals(key)) {
                        type = styleMap.get(key).toString();
                        stack.push(styleMap.get(key));
                        pos += key.toString().length();
                        start = pos;
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    pos = arg.indexOf('>', pos) + 1;
                    if (!stack.isEmpty()) {
                        int length = stack.lastElement().toString().length();
                        end = pos - length - 4;
                        typeList.add(type);
                        printTextList.add(arg.substring(start, end + 1));
                        stack.pop();
                    }
                }
            } else {
                sb.append(arg.charAt(pos));
                pos++;
            }
        }
        Logger.e(sb.toString());
        typeList.add("end");
        printTextList.add("-------------------------------");
        startPrint(typeList, printTextList);

    }


    public void startPrint(List<String> type, List<String> printText) {
        EscCommand escCommand = new EscCommand();
        escCommand.addInitializePrinter();
        for (int i = 0; i < type.size(); i++) {
            if ("CENTER_BOLD".equals(type.get(i))) {
                //设置打印居中
                escCommand.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
                //设置加粗
                escCommand.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                //是否加重
                escCommand.addTurnDoubleStrikeOnOrOff(EscCommand.ENABLE.ON);
                escCommand.addText(printText.get(i));
                escCommand.addPrintAndFeedLines((byte) 1);
            } else if ("TITLE".equals(type.get(i))) {
                escCommand.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
                //设置不加粗
                escCommand.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                escCommand.addText(printText.get(i));
                escCommand.addPrintAndFeedLines((byte) 2);
            } else if ("CENTER".equals(type.get(i))) {
                escCommand.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
                //设置不加粗
                escCommand.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                escCommand.addText(printText.get(i));
                escCommand.addPrintAndFeedLines((byte) 1);
            } else if ("LEFT".equals(type.get(i))) {
                escCommand.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
                //设置不加粗
                escCommand.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                escCommand.addText(printText.get(i));
                escCommand.addPrintAndFeedLines((byte) 1);
            } else if ("LEFT_BOLD".equals(type.get(i))) {
                escCommand.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
                //设置不加粗
                escCommand.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                //是否加重
                escCommand.addTurnDoubleStrikeOnOrOff(EscCommand.ENABLE.ON);
                escCommand.addText(printText.get(i));
                escCommand.addPrintAndFeedLines((byte) 1);
            } else if ("RIGHT".equals(type.get(i))) {
                escCommand.addSelectJustification(EscCommand.JUSTIFICATION.RIGHT);
                escCommand.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                escCommand.addText(printText.get(i));
                escCommand.addPrintAndFeedLines((byte) 1);
            } else if ("RIGHT_BOLD".equals(type.get(i))) {
                escCommand.addSelectJustification(EscCommand.JUSTIFICATION.RIGHT);
                escCommand.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                escCommand.addTurnDoubleStrikeOnOrOff(EscCommand.ENABLE.ON);
                escCommand.addText(printText.get(i));
                escCommand.addPrintAndFeedLines((byte) 1);
            } else if ("END".equals(type.get(i))) {
                escCommand.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
                escCommand.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                escCommand.addText(printText.get(i));
                escCommand.addPrintAndFeedLines((byte) 2);
            } else {
                escCommand.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
                escCommand.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                escCommand.addText(printText.get(i));
                escCommand.addPrintAndFeedLines((byte) 1);
            }
        }
        escCommand.addPrintAndLineFeed();
        // 加入查询打印机状态，打印完成后，此时会接收到GpCom.ACTION_DEVICE_STATUS广播
        escCommand.addQueryPrinterStatus();
        // 发送数据
        Vector<Byte> data = escCommand.getCommand();
        byte[] bytes = GpUtils.ByteTo_byte(data);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(0, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                disConnectToDevice();
            } else {
                disConnectToDevice();
                mHandler.sendEmptyMessage(0);
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void printerOnDestroy() {
        if (conn != null) {
            // unBindService
            //解决14254 java.lang.IllegalArgumentException Service not registered错误 此错误在执行解绑成功之后在进行解绑导致的错误
            if (isBind) {
                context.unbindService(conn);
                context.unregisterReceiver(mBroadcastReceiver);
                isBind = false;
                LogUtil.e("解绑服务");
            }
        }

    }

    /**
     * 查询打印机状态并返回
     *
     * @return
     */
    public int isLin() {
        int printStatus = 0;
        try {
            printStatus = mGpService.getPrinterConnectStatus(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return printStatus;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    LogUtil.e("打印成功");
                    ToastUtil.show("打印成功");
                    break;
                default:
                    break;
            }
        }
    };
}
