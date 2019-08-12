package com.freak.printtool.hardware.printreceipt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freak.printtool.R;
import com.freak.printtool.hardware.app.App;
import com.freak.printtool.hardware.app.Constants;
import com.freak.printtool.hardware.label.PrefUtils;
import com.freak.printtool.hardware.print.UsbAdmin;
import com.freak.printtool.hardware.utils.ACache;
import com.freak.printtool.hardware.utils.ToastUtil;
import com.posin.usbprinter.UsbPrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这是设置的打印小票机的设置
 */
public class SettingBillPrinterFragment extends Fragment implements OnClickListener {


    //usb授权设置
    public UsbAdmin mUsbAdmin = null;
    private App app;

    private static TextView receipt_device_name;
    private RelativeLayout receiptPrintConnect;
    private static TextView receipt_print_state;
    private RelativeLayout receiptOffPrint;
    private RelativeLayout receiptExaminePrintState;
    private RelativeLayout receiptPrintTest;
    private static Context mContext;
    private static ArrayList<UsbDevice> receiptDeviceList;
    public static UsbDevice receiptUSBDevice;
    public static boolean isConnected = false;
    protected static final String TAG = "SettingActivity";

    /***佳博小票打印机**/
    public static MyUsbPrinterUtil myUsbPrinterUtil = null;
    private static List<UsbDevice> devices;
    public static UsbPrinter usbPrinter = null;
    public static PrintCategory mpPrintCategory = null;
//    private Subscription subscribe;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_printer, container, false);

        receipt_device_name = view.findViewById(R.id.receipt_device_name);
        receiptPrintConnect = view.findViewById(R.id.receipt_print_connect);
        receipt_print_state = view.findViewById(R.id.receipt_print_state);
        receiptOffPrint = view.findViewById(R.id.receipt_off_print);
        receiptExaminePrintState = view.findViewById(R.id.receipt_examine_print_state);
        receiptPrintTest = view.findViewById(R.id.receipt_print_test);

        receiptPrintConnect.setOnClickListener(this);//连接打印机
        receiptOffPrint.setOnClickListener(this);//断开打印机
        receiptExaminePrintState.setOnClickListener(this);//开钱箱
        receiptPrintTest.setOnClickListener(this);//打印测试
        mContext = getActivity();
        updateButtonState(isConnected);

        app = (App) getActivity().getApplication();
//        mUsbAdmin = app.getUsbAdmin();

        ACache.get(getActivity()).put(Constants.WHETHER_PRINT, true);
//        myUsbPrinterUtil = App.getInstance().getMyUsbPrinterUtil();
//        usbPrinter = App.getInstance().getUsbPrinter();
//        receiptUSBDevice = App.getInstance().getReceiptUSBDevice();

//        subscribe = RxBus.getDefault().toObservable(StateEvent.class).subscribe(new Action1<StateEvent>() {
//            @Override
//            public void call(StateEvent printEvent) {
//                isConnected = printEvent.isConnected();
//                Log.e("freak", isConnected + "bbb");
//                Log.e("freak", printEvent.isConnected() + "aaa");
//                updateButtonState(isConnected);
//            }
//        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        updateButtonState(isConnected);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //连接打印机
            case R.id.receipt_print_connect:
                getReceiptDevice();
                break;
            //断开打印机
            case R.id.receipt_off_print:
                breakPrinter();
                break;
            //开钱箱
            case R.id.receipt_examine_print_state:

                if (usbPrinter == null) {
                    ToastUtil.shortShow("小票打印机未连接");
                } else {
                    myUsbPrinterUtil.setUsbDevice(receiptUSBDevice);
                    if (myUsbPrinterUtil.pushReceiptCash()) {
                        ToastUtil.shortShow("测试结果：钱箱打开成功...");
                        myUsbPrinterUtil.closeReceiptUsb();
                    } else {
                        ToastUtil.shortShow("测试结果：钱箱打开失败...");
                    }
                }

                break;
            //打印测试
            case R.id.receipt_print_test:
                if (usbPrinter != null) {

                    new PrintReceipt().printReceiptTest();

                } else {
                    ToastUtil.shortShow("小票打印机未连接");
                }

                break;
            default:
                break;
        }

    }


    /**
     * 断开打印机连接
     */
    public static void breakPrinter() {
        if (usbPrinter != null) {
            /**
             * 断开打印设备的连接
             */
            usbPrinter.close();
            usbPrinter = null;
            myUsbPrinterUtil.closeReceiptUsb();
            myUsbPrinterUtil = null;
            mpPrintCategory = null;
            receiptDeviceList = null;
            receiptUSBDevice = null;
            App.getInstance().setUsbPrinter(null);
            App.getInstance().setMyUsbPrinterUtil(null);
            App.getInstance().setReceiptUSBDevice(null);
            App.getInstance().setMpPrintCategory(null);

            receiptHandler.sendEmptyMessage(3);
            ToastUtil.shortShow("已断开连接");
        } else {
            ToastUtil.shortShow("打印机未连接");
        }
        if (isConnected) {
            receipt_device_name.setText("设备名称: " + "小票打印机");
            receipt_print_state.setText(mContext.getResources().getString(R.string.on_line));
        } else {
            receipt_device_name.setText("设备名称:未连接");
            receipt_print_state.setText(mContext.getResources().getString(R.string.off_line));
        }

    }

    /**
     * 连接打印机
     */
    public static void getReceiptDevice() {
        if (usbPrinter == null) {
            myUsbPrinterUtil = new MyUsbPrinterUtil(mContext);
            mpPrintCategory = new PrintCategory();
            devices = myUsbPrinterUtil.getUsbPrinterList();//获取所有打印设备
            receiptDeviceList = new ArrayList<>();
            for (UsbDevice usbDevice : devices) {

                if (MyUsbPrinterUtil.isUsbPrinterDevice(usbDevice)) {
                    myUsbPrinterUtil.requestPermission(usbDevice, null);//请求权限
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
                usbPrinter = new UsbPrinter(mContext, receiptUSBDevice);//打印对象
                /**
                 * 设置全局变量
                 */
                App.getInstance().setUsbPrinter(usbPrinter);
                App.getInstance().setMyUsbPrinterUtil(myUsbPrinterUtil);
                App.getInstance().setReceiptUSBDevice(receiptUSBDevice);
                App.getInstance().setMpPrintCategory(mpPrintCategory);

                receiptHandler.sendEmptyMessage(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 用于接受连接状态消息的 Handler
    @SuppressLint("HandlerLeak")
    public static Handler receiptHandler = new Handler() {
        @SuppressLint("ShowToast")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    ToastUtil.shortShow("连接成功");
                    isConnected = true;
                    Constants.ISCONNECTED = isConnected;
                    break;

                case 2:
                    isConnected = false;
                    ToastUtil.shortShow(mContext.getResources().getString(R.string.conn_failed));
                    break;

                case 3:
                    isConnected = false;
                    Constants.ISCONNECTED = isConnected;
                    Log.i(TAG, "连接关闭!");
                    break;
                case 4:
                    isConnected = false;
                    ToastUtil.shortShow(mContext.getResources().getString(R.string.conn_no));
                    break;
                case 0:
                    ToastUtil.shortShow("打印通信正常");
                    break;
                case -1:
                    ToastUtil.shortShow("打印机通信异常，请检查蓝牙连接");
                    vibrator();
                    break;
                case -2:
                    ToastUtil.shortShow("打印缺纸");
                    vibrator();
                    break;
                case -3:
                    ToastUtil.shortShow("打印机开盖");
                    vibrator();
                    break;

                default:
                    break;
            }

            updateButtonState(isConnected);//更新打印机状态
        }
    };


    static int count = 0;

    public static void vibrator() {
        count++;
        PrefUtils.setInt(mContext, "count3", count);
        Log.e(TAG, "" + count);
    }

    /**
     * 更新状态
     *
     * @param isConnected
     */
    public static void updateButtonState(boolean isConnected) {
        if (isConnected) {
            receipt_device_name.setText("设备名称: " + "小票打印机");
            receipt_print_state.setText(mContext.getResources().getString(R.string.on_line));
        } else {
            receipt_print_state.setText(mContext.getResources().getString(R.string.off_line));
            receipt_device_name.setText("设备名称:未连接");
        }
        PrefUtils.setBoolean(mContext, Constants.CONNECTSTATE, isConnected);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        subscribe.unsubscribe();
    }


}
