package com.freak.printtool.hardware.module.nosdkprint;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freak.printtool.R;

/**
 * @author Freak
 * @date 2019/8/14.
 */
public class NoSdkPrintFragment extends Fragment implements View.OnClickListener {
    private TextView mNoSdkDeviceName;
    private TextView mNoSdkPrintState;
    private RelativeLayout mNoSdkPrintConnect;
    private RelativeLayout mNoSdkOffPrint;
    private RelativeLayout mNoSdkPrintTest;
    private UsbAdmin mUsbAdmin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_sdk, container, false);
        mNoSdkDeviceName = view.findViewById(R.id.no_sdk_device_name);
        mNoSdkPrintConnect = view.findViewById(R.id.no_sdk_print_connect);
        mNoSdkPrintState = view.findViewById(R.id.no_sdk_print_state);
        mNoSdkOffPrint = view.findViewById(R.id.no_sdk_off_print);
        mNoSdkPrintTest = view.findViewById(R.id.no_sdk_print_test);
        mNoSdkOffPrint.setOnClickListener(this);
        mNoSdkPrintTest.setOnClickListener(this);
        mNoSdkPrintConnect.setOnClickListener(this);
        mUsbAdmin = UsbAdmin.getInstance(getActivity());
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no_sdk_print_connect:
                HardwareUtil.isConnected(mUsbAdmin);
                //连接可能需要1秒时间，设置延时任务，更新连接状态
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(0);
                    }
                }, 2000);

                break;
            case R.id.no_sdk_off_print:
                mUsbAdmin.closeUsb();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(1);
                    }
                }, 2000);
                break;
            case R.id.no_sdk_print_test:
                HardwareUtil.isPrintfData("", mUsbAdmin);
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mUsbAdmin.getUsbStatus()) {
                        mNoSdkDeviceName.setText("设备名称: " + "小票打印机");
                        mNoSdkPrintState.setText(getActivity().getResources().getString(R.string.on_line));
                    } else {
                        mNoSdkDeviceName.setText("设备名称:未连接");
                        mNoSdkPrintState.setText(getActivity().getResources().getString(R.string.off_line));
                    }
                    break;
                case 1:
                    if (mUsbAdmin.getUsbStatus()) {
                        mNoSdkDeviceName.setText("设备名称: " + "小票打印机");
                        mNoSdkPrintState.setText(getActivity().getResources().getString(R.string.on_line));
                    } else {
                        mNoSdkDeviceName.setText("设备名称:未连接");
                        mNoSdkPrintState.setText(getActivity().getResources().getString(R.string.off_line));
                    }
                    break;

                default:
                    break;
            }
        }
    };
}
