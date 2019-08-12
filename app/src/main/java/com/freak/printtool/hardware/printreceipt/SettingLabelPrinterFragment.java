package com.freak.printtool.hardware.printreceipt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;


import com.freak.printtool.R;
import com.freak.printtool.hardware.app.Constants;
import com.freak.printtool.hardware.label.PrefUtils;
import com.freak.printtool.hardware.label.PrintLabelGaomi;
import com.freak.printtool.hardware.label.UsbDeviceList;
import com.freak.printtool.hardware.print.bean.ProductLabelBean;
import com.freak.printtool.hardware.utils.ACache;
import com.freak.printtool.hardware.utils.ToastUtil;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.usb.USBPort;
import com.printer.sdk.utils.XLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




/**
 * 设置 - 标签
 *
 * @anthor dmin
 * created at 2017/11/14 20:24
 */

public class SettingLabelPrinterFragment extends Fragment implements View.OnClickListener {

    public static final int CONNECT_DEVICE = 1;

    protected static final String TAG = "SettingActivity";

    //    public PrinterInstance myPrinter;
    //这是默认是usb
    private int interfaceType = 1;

//    public String devicesName = "未知设备";

    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";

    private static TextView tv_device_name;

    private RelativeLayout rlPrintConnect;

    private RelativeLayout rlPrintTest;

    private TextView btnConnect;

    private TextView btnSelfprintTest;

    private IntentFilter bluDisconnectFilter;

    private RelativeLayout examinePrintState, offPrint;
    public static TextView labelPrintState;
    private Switch swFrontReversePrinter;
    private TextView directionSelect;
    private static Context mContext;
    private boolean direction;
    private SpannableString spannableString;
    public static PrinterInstance myPrinter;
    private static UsbDevice mUSBDevice;
    private static List<UsbDevice> deviceList;
    private static String deviceArrayLisr;
    public static String devicesName = "未知设备";
    private static String devicesAddress;
    // 打印机连接状态
    public static boolean isConnected = false;
    private static boolean hasRegDisconnectReceiver = false;
    private String strStatus = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_label_printer, container, false);

        init(view);

        return view;
    }


    private void init(View view) {

        tv_device_name = (TextView) view.findViewById(R.id.tv_device_name);

        btnConnect = (TextView) view.findViewById(R.id.btn_connect);

        btnSelfprintTest = (TextView) view.findViewById(R.id.btn_selfprint_test);

        rlPrintConnect = (RelativeLayout) view.findViewById(R.id.rl_print_connect);

        rlPrintTest = (RelativeLayout) view.findViewById(R.id.rl_print_test);

        examinePrintState = view.findViewById(R.id.examine_print_state);//检查打印机状态

        offPrint = view.findViewById(R.id.off_print);//断开打印机连接

        labelPrintState = view.findViewById(R.id.label_print_state);//显示打印机状态

        swFrontReversePrinter = view.findViewById(R.id.sw_front_reverse_printer);
        directionSelect = view.findViewById(R.id.btn_direction_select);

        mContext = getActivity();
        examinePrintState.setOnClickListener(this);//检查打印机状态
        offPrint.setOnClickListener(this);//断开打印机连接

        btnConnect.setOnClickListener(this);
        btnSelfprintTest.setOnClickListener(this);
        rlPrintConnect.setOnClickListener(this);
        rlPrintTest.setOnClickListener(this);


        if (null != ACache.get(getContext().getApplicationContext()).getAsObject(Constants.PRINT_DIRECTION)) {
            direction = (boolean) ACache.get(getContext().getApplicationContext()).getAsObject(Constants.PRINT_DIRECTION);
            Log.e("cai", "缓存状态：" + direction);
        } else {
            ACache.get(mContext.getApplicationContext()).put(Constants.PRINT_DIRECTION, false);
        }

        String strDirection = directionSelect.getText().toString();
        spannableString = new SpannableString(strDirection);
        Log.e("cai", spannableString.toString());


        swFrontReversePrinter.setChecked(direction);
        if (direction) {


            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),R.color.colorGrayText)), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),R.color.colorAccent)), 6, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            directionSelect.setText(spannableString);
            directionSelect.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),R.color.colorAccent)), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),R.color.colorGrayText)), 6, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            directionSelect.setText(spannableString);
            directionSelect.setMovementMethod(LinkMovementMethod.getInstance());
        }

        swFrontReversePrinter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("cai", "+++" + isChecked);
                if (isChecked) {
                    spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),R.color.colorGrayText)), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),R.color.colorAccent)), 6, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    directionSelect.setText(spannableString);
                    directionSelect.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),R.color.colorAccent)), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),R.color.colorGrayText)), 6, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    directionSelect.setText(spannableString);
                    directionSelect.setMovementMethod(LinkMovementMethod.getInstance());
                }
                //保存打印正反方向状态数据到缓存
                ACache.get(mContext.getApplicationContext()).put(Constants.PRINT_DIRECTION, isChecked);
                updateButtonState(isConnected);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateButtonState(isConnected);
        Log.e("freak", "isConnected" + isConnected);
        Log.e("freak", "onStart");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rl_print_connect:
                //连接打印机
            case R.id.btn_connect:
//                showSelectDevicesDialog();
                getDevice();
                break;
            case R.id.off_print://断开打印机连接
                breakPrinter();
                break;
            case R.id.examine_print_state://检查打印机状态


                Log.e("freak", "isConnected==" + isConnected);
                if (isConnected) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int i = myPrinter.getCurrentStatus();
                            if (i == 0) {
                                strStatus = "打印机状态正常";
                            } else if (i == -1) {
                                strStatus = "接收数据失败";
                            } else if (i == -2) {
                                strStatus = "打印机缺纸";
                            } else if (i == -3) {
                                strStatus = "打印机纸将尽";
                            } else if (i == -4) {
                                strStatus = "打印机开盖";
                            } else if (i == -5) {
                                strStatus = "发送数据失败";
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.shortShow(strStatus);
                                    XLog.i(TAG, "zl at SettingActivity.java onClick()------> btn_status_test");
                                }
                            });

                        }

                    }).start();
                } else {
                    ToastUtil.shortShow(mContext.getResources().getString(R.string.no_connected));
                }
                break;
            case R.id.rl_print_test:
                //打印测试
            case R.id.btn_selfprint_test:

                if (!isConnected) {
                    ToastUtil.shortShow("设备未连接，请重连");
                    break;
                }

                ProductLabelBean productLabelBean = new ProductLabelBean();
                productLabelBean.setName("测试");
                productLabelBean.setUnit("包");
                productLabelBean.setCash("2.00");
                productLabelBean.setEprice("1");
                productLabelBean.setProducer("广州");
                productLabelBean.setSn("0000000000001");
                productLabelBean.setPrice("3.00");

                new PrintLabelGaomi().doPrintTSPL(myPrinter, getActivity(), productLabelBean);

                break;

            default:
                break;

        }

    }

    /**
     * 断开连接
     */
    public static void breakPrinter() {
        if (myPrinter != null) {
            /**
             * 断开打印设备的连接
             */
            myPrinter.closeConnection();
            //清空打印对象
            myPrinter = null;
            XLog.i(TAG, "yxz at SettingActivity.java  onClick()  mPrinter:" + myPrinter);
        }
        if (isConnected) {
            tv_device_name.setText("设备名称: " + "思普瑞特价签机");
            labelPrintState.setText(mContext.getResources().getString(R.string.on_line));
        } else {
            tv_device_name.setText("设备名称:未连接");
            labelPrintState.setText(mContext.getResources().getString(R.string.off_line));
            ToastUtil.shortShow("已断开连接");
        }
        ToastUtil.shortShow("打印机未连接");
    }

    public static void getDevice() {
        if (myPrinter == null) {
            /**
             * 检测所有的打印设备
             */
            //获取usb权限
            UsbManager manager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
            HashMap<String, UsbDevice> devices = manager.getDeviceList();
            deviceList = new ArrayList<>();
            for (UsbDevice device : devices.values()) {
                /**
                 * 判断是都是打印设备。目前支持 (1155 != vendorId || 22304 != productId) && (1659 != vendorId || 8965 != productId) 两种
                 */
                if (USBPort.isUsbPrinter(device)) {
                    if (device.getVendorId() == 1155 && device.getProductId() == 22304) {
                        deviceArrayLisr = device.getDeviceName() + "\nvid: "
                                + device.getVendorId() + " pid: "
                                + device.getProductId();
                        deviceList.add(device);
                    }

                }
            }


            if (deviceList.isEmpty()) {
                ToastUtil.shortShow(mContext.getResources().getString(R.string.no_connected));
                return;
            }
            mUSBDevice = deviceList.get(0);
            if (mUSBDevice == null) {
                mHandler.obtainMessage(PrinterConstants.Connect.FAILED).sendToTarget();
                return;
            }
            myPrinter = PrinterInstance.getPrinterInstance(mContext, mUSBDevice, mHandler);
            devicesName = mUSBDevice.getDeviceName();
            devicesAddress = "vid: " + mUSBDevice.getVendorId() + "  pid: " + mUSBDevice.getProductId();
            UsbManager mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
            if (mUsbManager.hasPermission(mUSBDevice)) {
                myPrinter.openConnection();
            } else {
                // 没有权限询问用户是否授予权限
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                mContext.registerReceiver(mUsbReceiver, filter);
                // 该代码执行后，系统弹出一个对话框
                mUsbManager.requestPermission(mUSBDevice, pendingIntent);
            }

        }

    }


    /**
     * 弹出选择设备dialog
     */
    private void showSelectDevicesDialog() {

        Intent intent = new Intent(getActivity(), UsbDeviceList.class);
        startActivityForResult(intent, CONNECT_DEVICE);
    }


    /**
     * 用于接受连接状态消息的 Handler
     */
    @SuppressLint("HandlerLeak")
    public static Handler mHandler = new Handler() {
        @SuppressLint("ShowToast")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case PrinterConstants.Connect.SUCCESS:

                    ToastUtil.shortShow("连接成功");

                    isConnected = true;
                    Constants.ISCONNECTED = isConnected;
                    Constants.DEVICE_NAME = devicesName;
                    break;

                case PrinterConstants.Connect.FAILED:
                    isConnected = false;
                    ToastUtil.shortShow(mContext.getResources().getString(R.string.conn_failed));
                    Log.i(TAG, "连接失败!");
                    break;

                case PrinterConstants.Connect.CLOSED:
                    isConnected = false;
                    Constants.ISCONNECTED = isConnected;
                    Constants.DEVICE_NAME = devicesName;
                    Log.i(TAG, "连接关闭!");
                    break;
                case PrinterConstants.Connect.NODEVICE:
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

            updateButtonState(isConnected);
        }
    };


    static int count = 0;

    public static void vibrator() {
        count++;
        PrefUtils.setInt(mContext, "count3", count);
        Log.e(TAG, "" + count);

        // TODO: 2017/12/5 禁用视频
//        MediaPlayer player = new MediaPlayer().create(mContext, R.raw.test2);
//        player.start();
    }


    private static void updateButtonState(boolean isConnected) {
        if (isConnected) {
            tv_device_name.setText("设备名称: " + "思普瑞特价签机");
            labelPrintState.setText(mContext.getResources().getString(R.string.on_line));
//            getDevice();
        } else {
            labelPrintState.setText(mContext.getResources().getString(R.string.off_line));
            tv_device_name.setText("设备名称:未连接");
            XLog.d(TAG, "yxz at SettingActivity.java updateButtonState() ---end");

        }
        PrefUtils.setBoolean(mContext, Constants.CONNECTSTATE, isConnected);
    }


    // 安卓3.1以后才有权限操作USB
    @SuppressLint("ShowToast")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == CONNECT_DEVICE) {
            // 连接设备
            // usb
            mUSBDevice = data.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            myPrinter = PrinterInstance.getPrinterInstance(getActivity(), mUSBDevice, mHandler);
            devicesName = "思普瑞特价签机";
            UsbManager mUsbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
            if (mUsbManager.hasPermission(mUSBDevice)) {
                myPrinter.openConnection();
            } else {
                // 没有权限询问用户是否授予权限
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
                        new Intent(ACTION_USB_PERMISSION), 0);
                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                getActivity().registerReceiver(mUsbReceiver, filter);
                // 该代码执行后，系统弹出一个对话框
                mUsbManager.requestPermission(mUSBDevice, pendingIntent);
            }

        }

    }

    private static final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        @SuppressLint("NewApi")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.w(TAG, "receiver action: " + action);

            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    mContext.unregisterReceiver(mUsbReceiver);
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                            && mUSBDevice.equals(device)) {
                        myPrinter.openConnection();
                    } else {
                        mHandler.obtainMessage(PrinterConstants.Connect.FAILED).sendToTarget();
                        Log.e(TAG, "permission denied for device " + device);
                    }
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        XLog.e(TAG, "yxz at SettingActivity.java onDestroy()   progressdialog");
        super.onDestroy();
        if (hasRegDisconnectReceiver) {
            mContext.unregisterReceiver(mUsbReceiver);
            hasRegDisconnectReceiver = false;
            // Log.i(TAG, "关闭了广播！");
        }
    }

}
