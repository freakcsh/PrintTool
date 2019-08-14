package com.freak.printtool.hardware.module.nosdkprint;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

/**
 * usb端口测试
 *
 * @author Freak
 * @date 2019/8/13.
 */
public class UsbAdmin {

    private static final String TAG = "UsbAdmin";
    private UsbManager mUsbManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mConnection;
    private UsbEndpoint mEndpointIntr;
    private PendingIntent mPermissionIntent = null;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    @SuppressLint("NewApi")
    private void setDevice(UsbDevice device) {
        if (device != null) {
            UsbInterface intf = null;
            UsbEndpoint ep = null;

            int interfaceCount = device.getInterfaceCount();
            Log.i(TAG, "InterfaceCount:" + interfaceCount);
            int j;

            mDevice = device;
            for (j = 0; j < interfaceCount; j++) {
                int i;

                intf = device.getInterface(j);
                Log.i(TAG, "接口是:" + j + "类是:" + intf.getInterfaceClass());
                if (intf.getInterfaceClass() == 7) {
                    int usbEndpointCount = intf.getEndpointCount();
                    for (i = 0; i < usbEndpointCount; i++) {
                        ep = intf.getEndpoint(i);
                        Log.i(TAG, "端点是:" + i + "方向是:" + ep.getDirection() + "类型是:" + ep.getType());
                        if (ep.getDirection() == 0 && ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                            Log.i(TAG, "接口是:" + j + "端点是:" + i);
                            break;
                        }
                    }
                    if (i != usbEndpointCount) {
                        break;
                    }
                }
            }

            if (j == interfaceCount) {
                Log.i(TAG, "没有打印机接口");
                return;
            }

            mEndpointIntr = ep;

            UsbDeviceConnection connection = mUsbManager.openDevice(device);

            if (connection != null && connection.claimInterface(intf, true)) {
                Log.i(TAG, "打开成功！ ");
                Log.i(TAG, "connection " + connection);
                mConnection = connection;

            } else {
                Log.i(TAG, "打开失败！ ");
                mConnection = null;
            }

        }
    }

    /**
     * 这是测试打开usb
     */
    @SuppressLint("NewApi")
    public void openUsb() {
        Log.i(TAG, "执行到这里1 ");
        if (mDevice != null) {
            Log.i(TAG, "执行到这里2 ");

            Log.i(TAG, "mDevice: " + mDevice.getDeviceId());

            setDevice(mDevice);

            if (mConnection == null) {
                HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
                Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

                while (deviceIterator.hasNext()) {
                    UsbDevice device = deviceIterator.next();
                    mUsbManager.requestPermission(device, mPermissionIntent);
                }
            }

        } else {
            Log.i(TAG, "执行到这里3");
            HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();
                Log.i(TAG, "执行到这里4" + device.getDeviceId());
                mUsbManager.requestPermission(device, mPermissionIntent);
            }
        }
    }

    @SuppressLint("NewApi")
    public void closeUsb() {
        if (mConnection != null) {
            mConnection.close();
            mConnection = null;
        }
    }

    public String getUsbStatus(boolean language) {
        if (mDevice == null) {
            if (language) {
                return "没有Usb设备！";
            } else {
                return "No Usb Device!";
            }
        }
        if (mConnection == null) {
            if (language) {
                return "Usb设备不是打印机！";
            } else {
                return "Usb device is not a printer!";
            }
        }
        if (language) {
            return "Usb打印机打开成功！";
        }
        return "Usb Printer Open success！";
    }

    /**
     * 打印连接的测试回馈
     *
     * @return
     */
    public boolean getUsbStatus() {
        if (mConnection == null) {
            return false;
        }
        return true;
    }

    /**
     * 发送信息 一是打印消息，切纸，打开钱箱等
     *
     * @param content
     * @return
     */
    @SuppressLint("NewApi")
    public boolean sendCommand(byte[] content) {
        boolean result;
        synchronized (this) {
            int len = -1;
            if (mConnection != null) {
                len = mConnection.bulkTransfer(mEndpointIntr, content, content.length, 10000);
            }

            if (len < 0) {
                result = false;
                Log.i(TAG, "发送失败！ " + len);
            } else {
                result = true;
                Log.i(TAG, "发送" + len + "字节数据");
            }
        }
        return result;
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {

                        if (device != null) {
                            //现在排除了标签打印机

                            Log.d(TAG, "其他设备的id " + device.getProductId());

//                            if (device.getProductId() != 22304) {
//                                setDevice(device);
//                            }

                            if (device.getProductId() != 1 && device.getProductId() != 46880 && device.getProductId() != 22304) {
                                setDevice(device);
                            }

                        } else {
                            closeUsb();
                            mDevice = device;
                        }
                    } else {
                        Log.d(TAG, "permission denied for device " + device);
                    }

                }

            }
        }
    };
    private static UsbAdmin usbAdmin;

    public static UsbAdmin getInstance(Context context) {
        if (usbAdmin != null) {
            return usbAdmin;
        } else {
            usbAdmin = new UsbAdmin(context);
            return usbAdmin;
        }
    }

    public UsbAdmin(Context context) {
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(mUsbReceiver, filter);
    }
}