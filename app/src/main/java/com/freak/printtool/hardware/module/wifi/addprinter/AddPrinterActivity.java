package com.freak.printtool.hardware.module.wifi.addprinter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.freak.printtool.R;
import com.freak.printtool.hardware.base.IActivityStatusBar;
import com.freak.printtool.hardware.module.wifi.PrinterSettingActivity;
import com.freak.printtool.hardware.module.wifi.util.SearchPrinterUtil;
import com.freak.printtool.hardware.utils.DialogUtil;
import com.freak.printtool.hardware.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 打印机手动添加
 *
 * @author Freak
 * @date 2019/8/13.
 */
public class AddPrinterActivity extends AppCompatActivity implements IActivityStatusBar, View.OnClickListener {
    private TextView mTextViewIpCommit;
    private EditText mEditTextIpAddress;
    public static final int ADD_PRINTER_REQUEST_CODE = 1105;
    public static final int ADD_PRINTER_RESULT_CODE = 1106;
    private ExecutorService mExecutorService;

    public static void startAction(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, AddPrinterActivity.class);
        activity.startActivityForResult(intent, ADD_PRINTER_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_printer);
        super.onCreate(savedInstanceState);
        setTitle("打印机手动添加");
        initView();
    }

    public void initView() {
        mEditTextIpAddress = findViewById(R.id.edit_text_ip_address);
        mTextViewIpCommit = findViewById(R.id.text_view_ip_commit);
        mTextViewIpCommit.setOnClickListener(this);
    }

    private String getIpAddress() {
        return mEditTextIpAddress.getText().toString().trim();
    }

    @Override
    public int getStatusBarColor() {
        return ContextCompat.getColor(this, R.color.colorPrimary);
    }

    @Override
    public int getDrawableStatusBar() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_view_ip_commit:
                if (TextUtils.isEmpty(getIpAddress())) {
                    ToastUtil.show("请输入ip地址");
                } else {
                    if (TextUtils.isEmpty(getIpAddress())) {
                        DialogUtil.showToastDialog(this, "温馨提示", "wifi暂未连接，请先连接wifi，然后在进行打印机添加操作", "知道了");
                    } else {
                        boolean isIp = isIpv4(getIpAddress());
                        if (isIp) {
                            RunThread runThread = new RunThread();
                            runThread.start();

                        } else {
                            ToastUtil.show("请输入正确的ip地址");
                        }
                    }
                }

                break;
            default:
                break;
        }
    }

    /**
     * 判断ip地址是否正确
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIpv4(String ipAddress) {
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    /**
     * 检测打印机是否开启线程
     */
    private class RunThread extends Thread {
        private ScheduledExecutorService mScheduledExecutorService;

        public RunThread() {
        }

        @Override
        public void run() {
            super.run();
            if (mExecutorService == null) {
                mExecutorService = Executors.newCachedThreadPool();
            }
            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    SearchPrinterUtil mSearchPrinterUtil = new SearchPrinterUtil();
                    mSearchPrinterUtil.search(getIpAddress(), 9100);
                    if (mSearchPrinterUtil.printerIsOpen) {
                        Logger.e("开启的打印机--》" + getIpAddress());
                        Intent intent = new Intent(AddPrinterActivity.this, PrinterSettingActivity.class);
                        intent.putExtra("ip", getIpAddress());
                        setResult(ADD_PRINTER_RESULT_CODE, intent);
                        mHandler.sendEmptyMessage(0);
                        finish();
                    } else {
                        mHandler.sendEmptyMessage(0);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show("打印机未打开，请检查打印机设置");
                            }
                        });
                    }
                }
            });

        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                default:
                    break;
            }
        }
    };
}
