package com.freak.printtool.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.freak.printtool.hardware.module.scanner.BarcodeScannerActivity;

/**
 * 开机启动广播
 *
 * @author Freak
 * @date 2019/8/13.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent i = new Intent(context, BarcodeScannerActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
