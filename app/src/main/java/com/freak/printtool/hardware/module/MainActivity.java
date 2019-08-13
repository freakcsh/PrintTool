package com.freak.printtool.hardware.module;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;


import com.freak.printtool.R;
import com.freak.printtool.hardware.base.IActivityStatusBar;
import com.freak.printtool.hardware.module.receipt.ReceiptPrinterFragment;
import com.freak.printtool.hardware.module.label.LabelPrinterFragment;
import com.freak.printtool.hardware.module.scanner.BarcodeScannerActivity;
import com.freak.printtool.hardware.module.scanner.BarcodeScannerResolver;
import com.freak.printtool.hardware.module.wifi.WifiPrintFragment;

/**
 * 这是新页面，增加了测试打印机
 *
 * @author Freak
 * @date 2019/8/13.
 */
public class MainActivity extends AppCompatActivity implements OnClickListener, IActivityStatusBar {
    /**
     * 小票打印机
     */
    private TextView tvReceiptPrinter;

    /**
     * 标签打印机
     */
    private TextView tvLabelPrint;
    /**
     * wifi打印机
     */
    private TextView tvWifiPrint;
    /**
     * fragment的对象
     */
    private ReceiptPrinterFragment mReceiptPrinterFragment;
    private LabelPrinterFragment mLabelPrinterFragment;
    private WifiPrintFragment mWifiPrintFragment;
    /**
     * 扫码枪工具类
     */
    private BarcodeScannerResolver mBarcodeScannerResolver;

    /**
     * 定义FragmentManager对象
     */
    FragmentManager fManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        fManager = getSupportFragmentManager();
        initScannerResolver();
        tvReceiptPrinter = (TextView) findViewById(R.id.tv_receipt);
        tvWifiPrint = (TextView) findViewById(R.id.tv_wifi);
        tvLabelPrint = (TextView) findViewById(R.id.tv_label);
        tvReceiptPrinter.setOnClickListener(this);
        tvLabelPrint.setOnClickListener(this);
        tvWifiPrint.setOnClickListener(this);
        setChoiceItem(0);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 打印与外设
            case R.id.tv_receipt:
                setChoiceItem(0);
                break;
            // 标签机设置
            case R.id.tv_label:
                setChoiceItem(1);
                break;
            //wifi打印机
            case R.id.tv_wifi:
                setChoiceItem(2);
                break;
            default:
                break;
        }
    }

    /**
     * 定义一个选中一个item后的处理
     *
     * @param index
     */
    private void setChoiceItem(int index) {
        // 重置选项+隐藏所有Fragment
        FragmentTransaction transaction = fManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            //这是打印设置
            case 0:
                if (mReceiptPrinterFragment == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    mReceiptPrinterFragment = new ReceiptPrinterFragment();
                    transaction.add(R.id.fg_content, mReceiptPrinterFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mReceiptPrinterFragment);
                }
                tvReceiptPrinter.setSelected(true);
                tvLabelPrint.setSelected(false);
                tvWifiPrint.setSelected(false);
                tvWifiPrint.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
                tvReceiptPrinter.setTextColor(ContextCompat.getColor(this, R.color.white));
                tvLabelPrint.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
                break;
            //标签机设置
            case 1:
                if (mLabelPrinterFragment == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    mLabelPrinterFragment = new LabelPrinterFragment();
                    transaction.add(R.id.fg_content, mLabelPrinterFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mLabelPrinterFragment);
                }
                tvLabelPrint.setSelected(true);
                tvReceiptPrinter.setSelected(false);
                tvWifiPrint.setSelected(false);
                tvWifiPrint.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
                tvLabelPrint.setTextColor(ContextCompat.getColor(this, R.color.white));
                tvReceiptPrinter.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
                break;
            case 2:
                if (mWifiPrintFragment == null) {
                    //为空，创建并添加
                    mWifiPrintFragment = new WifiPrintFragment();
                    transaction.add(R.id.fg_content, mWifiPrintFragment);
                } else {
                    // 如果不为空，显示
                    transaction.show(mWifiPrintFragment);
                }
                //设置选中颜色
                tvLabelPrint.setSelected(false);
                tvReceiptPrinter.setSelected(false);
                tvWifiPrint.setSelected(true);
                tvWifiPrint.setTextColor(ContextCompat.getColor(this, R.color.white));
                tvLabelPrint.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
                tvReceiptPrinter.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
                break;
            case 3:
                break;
            default:
                break;
        }

        transaction.commitAllowingStateLoss();
    }

    /**
     * 隐藏所有的Fragment,避免fragment混乱
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mReceiptPrinterFragment != null) {
            transaction.hide(mReceiptPrinterFragment);
        }
        if (mLabelPrinterFragment != null) {
            transaction.hide(mLabelPrinterFragment);
        }
    }

    /**
     * 初始化扫码监听
     */
    private void initScannerResolver() {
        mBarcodeScannerResolver = new BarcodeScannerResolver();
        mBarcodeScannerResolver.setScanSuccessListener(new BarcodeScannerResolver.OnScanSuccessListener() {
            @Override
            public void onScanSuccess(String barcode) {
                Log.e("TAG", barcode);
                Toast.makeText(MainActivity.this, "扫码枪数据：" + barcode, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
            mBarcodeScannerResolver.resolveKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public int getStatusBarColor() {
        return 0;
    }

    @Override
    public int getDrawableStatusBar() {
        return 0;
    }
}
