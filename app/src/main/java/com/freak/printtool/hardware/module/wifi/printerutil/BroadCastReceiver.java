package com.freak.printtool.hardware.module.wifi.printerutil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.freak.printtool.hardware.utils.LogUtil;
import com.freak.printtool.hardware.utils.ToastUtil;
import com.gprinter.command.GpCom;

/**
 * @author Freak
 * @date 2019/8/13.
 */

public class BroadCastReceiver extends BroadcastReceiver {
    private static final int REQUEST_PRINT_RECEIPT = 0xfc;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtil.e("Action-->" + action);
        // GpCom.ACTION_DEVICE_REAL_STATUS 为广播的IntentFilter
        if (action.equals(GpCom.ACTION_DEVICE_REAL_STATUS)) {

            // 业务逻辑的请求码，对应哪里查询做什么操作
            int requestCode = intent.getIntExtra(GpCom.EXTRA_PRINTER_REQUEST_CODE, -1);
            // 判断请求码，是则进行业务操作
            if (requestCode == REQUEST_PRINT_RECEIPT) {

                int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                String str;
                if (status == GpCom.STATE_NO_ERR) {
                    str = "打印机正常";
                } else {
                    str = "打印机 ";
                    if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                        str += "脱机";
                    }
                    if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                        str += "缺纸";
                    }
                    if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                        str += "打印机开盖";
                    }
                    if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                        str += "打印机出错";
                    }
                    if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                        str += "查询超时";
                    }
                }
                LogUtil.e("打印机状态--》" + str);
                ToastUtil.show(str);
            }
        }
    }
}
