package com.freak.printtool.hardware.module.receipt.printreceipt;

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

import com.freak.printtool.R;
import com.freak.printtool.hardware.app.App;
import com.freak.printtool.hardware.utils.ToastUtil;
import com.posin.usbprinter.UsbPrinter;
import com.posin.usbprinter.UsbPrinterUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.freak.printtool.hardware.module.receipt.ReceiptPrinterFragment.receiptHandler;
import static com.freak.printtool.hardware.module.receipt.ReceiptPrinterFragment.receiptUSBDevice;
import static com.freak.printtool.hardware.module.receipt.ReceiptPrinterFragment.usbPrinter;


/**
 * 这是小票打印机工具类
 *
 * @author Freak
 * @date 2019/8/13.
 */

public class MyUsbPrinterUtil {
    private static final String TAG = "UsbPrinter";
    private final Context mContext;
    private final UsbManager mUsbManager;
    private volatile List<UsbDevice> mUsbPrinterList = null;
    private static String ACTION_USB_PERMISSION = "com.posin.usbdevice.USB_PERMISSION";
    private UsbPrinterUtil.OnUsbPermissionCallback onPermissionCallback = null;
    public static final byte[] PUSH_CASH = {0x1b, 0x70, 0x00, 0x1e, (byte) 0xff, 0x00};
    public UsbDeviceConnection mConnection;
    private UsbEndpoint mEndpointIntr;
    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("UsbPrinter", intent.getAction());
            if (MyUsbPrinterUtil.ACTION_USB_PERMISSION.equals(intent.getAction())) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
                if (intent.getBooleanExtra("permission", false)) {
                    if (MyUsbPrinterUtil.this.onPermissionCallback != null) {
                        MyUsbPrinterUtil.this.onPermissionCallback.onUsbPermissionEvent(device, true);
                    }
                } else if (MyUsbPrinterUtil.this.onPermissionCallback != null) {
                    MyUsbPrinterUtil.this.onPermissionCallback.onUsbPermissionEvent(device, false);
                }

                context.unregisterReceiver(this);
            }

        }
    };

    public MyUsbPrinterUtil(Context context) {
        this.mContext = context;
        this.mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }


    /**********************************全局测试**************************************/
    private static MyUsbPrinterUtil myUsbPrinterUtil;
    public static PrintCategory mpPrintCategory = null;

    public static MyUsbPrinterUtil getInstance(Context context) {

        if (myUsbPrinterUtil != null) {
            return myUsbPrinterUtil;
        } else {
            myUsbPrinterUtil = new MyUsbPrinterUtil(context);
            return myUsbPrinterUtil;
        }
    }

    /**
     * 连接打印机
     */
    public void getReceiptDevice() {
        if (usbPrinter == null) {
//            myUsbPrinterUtil = new MyUsbPrinterUtil(mContext);
            mpPrintCategory = new PrintCategory();
            List<UsbDevice> devices = getUsbPrinterList();
            ArrayList<UsbDevice> receiptDeviceList = new ArrayList<>();
            for (UsbDevice usbDevice : devices) {
                if (isUsbPrinterDevice(usbDevice)) {
                    requestPermission(usbDevice, null);
                    /**
                     * 优库打印机：pid=33054 vid=4070
                     * 君时达打印机：pid=1803 vid=1155
                     * 票据打印机：pid=20497 vid=1046
                     * 佳博打印机：pid=1536 vid=26728
                     */
                    if (usbDevice.getVendorId() == 26728 && usbDevice.getProductId() == 1536 || usbDevice.getVendorId() == 4070 && usbDevice.getProductId() == 33054 ||
                            usbDevice.getVendorId() == 1155 && usbDevice.getProductId() == 1803 || usbDevice.getVendorId() == 1046 && usbDevice.getProductId() == 20497) {
                        receiptDeviceList.add(usbDevice);
                    }
                }
            }

            if (receiptDeviceList.isEmpty()) {
                ToastUtil.shortShow(mContext.getResources().getString(R.string.no_connected));
                return;
            }

            receiptUSBDevice = receiptDeviceList.get(0);

            if (receiptUSBDevice == null) {
                receiptHandler.sendEmptyMessage(4);
                return;
            }

            try {
                usbPrinter = new UsbPrinter(mContext, receiptUSBDevice);

                App.getInstance().setUsbPrinter(usbPrinter);
//                App.getInstance().setMyUsbPrinterUtil(myUsbPrinterUtil);
                App.getInstance().setReceiptUSBDevice(receiptUSBDevice);
                App.getInstance().setMpPrintCategory(mpPrintCategory);

//                receiptHandler.sendEmptyMessage(1);
//                App.getInstance().set
//                ReceiptPrinterFragment.isConnected=true;

//               RxBus.getDefault().post(new PrintEvent(0,true));
                /**
                 * 2月7号测试下面代码
                 */
//                ReceiptPrinterFragment settingBillPrinterFragment = new ReceiptPrinterFragment();
//                Log.e("freak",settingBillPrinterFragment.getUserVisibleHint()+"\nsettingBillPrinterFragment.getUserVisibleHint()");
//                if (settingBillPrinterFragment.getUserVisibleHint()){
//                    ReceiptPrinterFragment.updateButtonState(true);
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /*****************************************全局测试******************************************************/
    /**
     * 获取所有的小票打印机设备
     */
    public List<UsbDevice> getUsbPrinterList() {

//        if (Build.MODEL.substring(0, 3).equalsIgnoreCase("TPS")) {
        if (this.mUsbPrinterList == null) {
            this.mUsbPrinterList = this.findAllUsbPrinter();
        }

        return this.mUsbPrinterList;
//        } else {
//            Log.e("_ERROR", "ERROR--->Device is not support!  This Demo just developer for TPS device");
//            return null;
//        }
    }


    public boolean requestPermission(UsbDevice usbDevice, com.posin.usbprinter.UsbPrinterUtil.OnUsbPermissionCallback callback) {
        if (!this.mUsbManager.hasPermission(usbDevice)) {
            IntentFilter ifilter = new IntentFilter(ACTION_USB_PERMISSION);
            this.mContext.registerReceiver(this.mReceiver, ifilter);
            PendingIntent pi = PendingIntent.getBroadcast(this.mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
            this.onPermissionCallback = callback;
            this.mUsbManager.requestPermission(usbDevice, pi);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取所有的小票打印机
     */
    private List<UsbDevice> findAllUsbPrinter() {
        List<UsbDevice> result = new ArrayList();
        Log.d("UsbPrinter", "find usb printer...");
        Iterator var3 = this.mUsbManager.getDeviceList().values().iterator();

        while (var3.hasNext()) {
            UsbDevice usbDevice = (UsbDevice) var3.next();
            Log.d("UsbPrinter", String.format("usb %04X:%04X : device_id=%d, device_name=%s", new Object[]{Integer.valueOf(usbDevice.getVendorId()), Integer.valueOf(usbDevice.getProductId()), Integer.valueOf(usbDevice.getDeviceId()), usbDevice.getDeviceName()}));
            if (isUsbPrinterDevice(usbDevice)) {
                Log.d("UsbPrinter", String.format("usb printer %04X:%04X : device_id=%d, device_name=%s", new Object[]{Integer.valueOf(usbDevice.getVendorId()), Integer.valueOf(usbDevice.getProductId()), Integer.valueOf(usbDevice.getDeviceId()), usbDevice.getDeviceName()}));
                result.add(usbDevice);
            }
        }

        return result;
    }

    /**
     * 识别不同的小票打印机设备
     *
     * @param usbDevice
     * @return
     */
    public static boolean isUsbPrinterDevice(UsbDevice usbDevice) {
        /**
         * getVendorId()返回一个供应商id
         *getProductId（）为设备返回一个产品ID
         * */
        int vid = usbDevice.getVendorId();
        int pid = usbDevice.getProductId();
        return vid == 5455 && pid == 5455 || vid == 26728 && pid == 1280 || vid == 26728 && pid == 1536 || vid == '衦' || vid == 1137 || vid == 1659 || vid == 1137 || vid == 1155 && pid == 1803 || vid == 17224 || vid == 7358 || vid == 6790 || vid == 1046 && pid == 20497 || vid == 10685 || vid == 4070 && pid == 33054;
    }

    /* 打开钱箱 */
    public boolean pushReceiptCash() {
        boolean canPush = false;


        if (this.sendUsbCommand(PUSH_CASH)) {
            canPush = true;
        } else {
            canPush = false;
        }
        return canPush;
    }

    /**
     * //发送信息 一是打印消息，切纸，打开钱箱等
     *
     * @param content
     * @return
     */
    @SuppressLint("NewApi")
    public boolean sendUsbCommand(byte[] content) {
        boolean result;
        synchronized (this) {
            int len = -1;
            if (mConnection != null) {
                len = mConnection.bulkTransfer(mEndpointIntr, content, content.length, 10000);
            }

            if (len < 0) {
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }

    @SuppressLint("NewApi")
    public void setUsbDevice(UsbDevice device) {
        if (device != null) {
            UsbInterface intf = null;
            UsbEndpoint ep = null;

            int interfaceCount = device.getInterfaceCount();
            Log.i(TAG, "InterfaceCount:" + interfaceCount);
            int j;

//            mDevice = device;
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

    @SuppressLint("NewApi")
    public void closeReceiptUsb() {
        if (mConnection != null) {
            mConnection.close();
            mConnection = null;
        }
    }

    public interface OnUsbPermissionCallback {
        void onUsbPermissionEvent(UsbDevice var1, boolean var2);
    }
}
