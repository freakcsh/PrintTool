package com.freak.printtool.hardware.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.freak.printtool.hardware.utils.ToastUtil;


/**
 * @author Freak
 * @date 2019/8/13.
 */

public class UsbDeviceConnectChangedReceiver extends BroadcastReceiver {

    /**
     * usb线的广播
     */
    private final static String USB_STATE = "android.hardware.usb.action.USB_STATE";
    /**
     * 外设的广播
     */
    public static final String USB_DEVICE_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public static final String USB_DEVICE_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    /**
     * 耳机
     */
    private static final String HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //连接的外设
        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        //外设连接状态
        if (USB_DEVICE_ATTACHED.equals(action)) {
            //连接
            Log.d("PayMethod", device.getVendorId() + "");
            Log.d("PayMethod", device.getProductId() + "");
            // device.getVendorId() == 26728 && device.getProductId() == 1536
            if (device.getVendorId() == 26728 && device.getProductId() == 1280 || device.getVendorId() == 26728 && device.getProductId() == 1536 || device.getVendorId() == 4070 && device.getProductId() == 33054 ||
                    device.getVendorId() == 1155 && device.getProductId() == 1803 || device.getVendorId() == 1046 && device.getProductId() == 20497) {
                ToastUtil.shortShow("小票打印机插入");
            }
            if (device.getVendorId() == 1155 && device.getProductId() == 22304) {
                ToastUtil.shortShow("标签打印机插入");
            }
        }
        if (USB_DEVICE_DETACHED.equals(action)) {
            //断开
            if (device.getVendorId() == 26728 && device.getProductId() == 1280 || device.getVendorId() == 26728 && device.getProductId() == 1536 || device.getVendorId() == 4070 && device.getProductId() == 33054 ||
                    device.getVendorId() == 1155 && device.getProductId() == 1803 || device.getVendorId() == 1046 && device.getProductId() == 20497) {
                ToastUtil.shortShow("小票打印机被拔出");
            }
            if (device.getVendorId() == 1155 && device.getProductId() == 22304) {
                ToastUtil.shortShow("标签打印机被拔出");
            }
        }
        //判断存储usb
        if (USB_STATE.equals(action)) {
            boolean connected = intent.getBooleanExtra("connected", false);
            if (connected) {
                //应用刚启动也会触发
//                ToastUtil.shortShow("USB已连接");
            } else {
//                ToastUtil.shortShow("USB已断开");
            }
        }
    }
}