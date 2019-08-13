package com.freak.printtool.hardware.module.scanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.freak.printtool.R;
import com.freak.printtool.hardware.base.IActivityStatusBar;
import com.freak.printtool.hardware.module.MainActivity;

/**
 * @author Freak
 * @date 2019/8/13.
 */
public class BarcodeScannerActivity extends AppCompatActivity implements IActivityStatusBar {
    /**
     * 扫码枪工具类
     */
    private BarcodeScannerResolver mBarcodeScannerResolver;
    private EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        edt = findViewById(R.id.edt);
        //这是小键盘输入
        edt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {

                    return true;
                }
                return false;
            }
        });
        //这是扫码枪输入
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /**
                 * 当actionId == XX_SEND 或者 XX_DONE时都触发
                 * 或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                 * 注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                 */
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    Log.e("TAG", "扫码结束");
                }
                return false;
            }
        });
        initScannerResolver();
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
                Toast.makeText(BarcodeScannerActivity.this, "扫码枪数据：" + barcode, Toast.LENGTH_SHORT).show();
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

    public void printSettingOnclick(View view) {
        startActivity(new Intent(this, MainActivity.class));
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
