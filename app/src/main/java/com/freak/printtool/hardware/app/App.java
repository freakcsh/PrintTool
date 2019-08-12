package com.freak.printtool.hardware.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Message;

import com.freak.printtool.hardware.print.UsbAdmin;
import com.freak.printtool.hardware.printreceipt.MyUsbPrinterUtil;
import com.freak.printtool.hardware.printreceipt.PrintCategory;
import com.freak.printtool.hardware.receiver.NetworkConnectChangedReceiver;
import com.freak.printtool.hardware.receiver.UsbDeviceConnectChangedReceiver;
import com.posin.usbprinter.UsbPrinter;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 全局配置
 */

public class App extends Application {
    /**
     * 开发的时候使用开发账号
     * 打包使用省账号
     */

    /**
     * 每一个省区的代号
     */
    private String province_name;

    //这是
    private String clientId;

    private static App instance;
    private Set<Activity> allActivities;
    private UsbAdmin usbAdmin;


    private NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;
    /**
     * 表示是否连接
     */
    public boolean isConnected;
    //    表示是否是移动网络
    public boolean isMobile;
    //    表示是否是WiFi
    public boolean isWifi;
    //    表示WiFi开关是否打开
    public boolean isEnablaWifi;
    //    表示移动网络数据是否打开
    public boolean isEnableMobile;
    //     表示是否是以太网
    public boolean isEthernet;

    public Map<String, Boolean> booleanMap;

    private UsbPrinter usbPrinter = null;

    private MyUsbPrinterUtil myUsbPrinterUtil=null;

    public  UsbDevice receiptUSBDevice=null;

    private PrintCategory mpPrintCategory = null;

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();
    private UsbDeviceConnectChangedReceiver mUsbDeviceConnectChangedReceiver;


    public UsbAdmin getUsbAdmin() {
        return usbAdmin;
    }

    public static synchronized App getInstance() {
        return instance;
    }

    public MyUsbPrinterUtil getMyUsbPrinterUtil() {
        return myUsbPrinterUtil;
    }

    public UsbDevice getReceiptUSBDevice() {
        return receiptUSBDevice;
    }

    public PrintCategory getMpPrintCategory() {
        return mpPrintCategory;
    }

    public void setMpPrintCategory(PrintCategory mpPrintCategory) {
        this.mpPrintCategory = mpPrintCategory;
    }

    public void setReceiptUSBDevice(UsbDevice receiptUSBDevice) {
        this.receiptUSBDevice = receiptUSBDevice;
    }

    public void setMyUsbPrinterUtil(MyUsbPrinterUtil myUsbPrinterUtil) {
        this.myUsbPrinterUtil = myUsbPrinterUtil;
    }

    public UsbPrinter getUsbPrinter() {
        return usbPrinter;
    }

    public void setUsbPrinter(UsbPrinter usbPrinter) {
        this.usbPrinter = usbPrinter;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //网络状态 USB设备监听
        initReceiver();
    }


    public void setBooleanMap(Map<String, Boolean> promotionMap) {
        this.booleanMap = promotionMap;
    }


    public Map<String, Boolean> getBooleanMap() {
        return this.booleanMap;
    }


    //##################################### 以下是activity的收集 ####################################
    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    /**
     * 退出app
     */
    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }

        //注销网络监听
        if (mNetworkConnectChangedReceiver != null) {
            unregisterReceiver(mNetworkConnectChangedReceiver);
        }


        if (mUsbDeviceConnectChangedReceiver != null) {
            unregisterReceiver(mUsbDeviceConnectChangedReceiver);
        }

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private void initReceiver() {
        //网络
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.ic_network_off.WIFI_STATE_CHANGED");
        filter.addAction("android.net.ic_network_off.STATE_CHANGE");
        filter.setPriority(1000);
        if (mNetworkConnectChangedReceiver == null) {
            mNetworkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        }

        registerReceiver(mNetworkConnectChangedReceiver, filter);

        //USB
        IntentFilter filter1 = new IntentFilter();
        //筛选的条件
        filter1.addAction("android.hardware.usb.action.USB_STATE");
        filter1.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        filter1.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        filter1.setPriority(1000);
        if (mUsbDeviceConnectChangedReceiver == null) {
            mUsbDeviceConnectChangedReceiver = new UsbDeviceConnectChangedReceiver();
        }
        //注册广播 动态注册
        registerReceiver(mUsbDeviceConnectChangedReceiver, filter1);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        if (mNetworkConnectChangedReceiver != null) {
            unregisterReceiver(mNetworkConnectChangedReceiver);
        }


        if (mUsbDeviceConnectChangedReceiver != null) {
            unregisterReceiver(mUsbDeviceConnectChangedReceiver);
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        if (mNetworkConnectChangedReceiver != null) {
            unregisterReceiver(mNetworkConnectChangedReceiver);
        }

        if (mUsbDeviceConnectChangedReceiver != null) {
            unregisterReceiver(mUsbDeviceConnectChangedReceiver);
        }

    }

    public boolean isConnected() {
        return isWifi || isMobile;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isMobile() {
        return isMobile;
    }

    public boolean isEthernet() {
        return isEthernet;
    }

    public void setEthernet(boolean ethernet) {
        isEthernet = ethernet;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    public boolean isWifi() {
        return isWifi;
    }

    public void setWifi(boolean wifi) {
        isWifi = wifi;
    }

    public boolean isEnablaWifi() {
        return isEnablaWifi;
    }

    public void setEnablaWifi(boolean enablaWifi) {
        isEnablaWifi = enablaWifi;
    }

    public boolean isEnableMobile() {
        return isEnableMobile;
    }

    public void setEnableMobile(boolean enableMobile) {
        isEnableMobile = enableMobile;
    }

}
