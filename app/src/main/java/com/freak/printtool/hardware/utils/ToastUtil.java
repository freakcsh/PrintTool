package com.freak.printtool.hardware.utils;


///**
// * Created by codeest on 2016/8/4.
// * 自定义toast格式
// */
//public class ToastUtil {
//
//    static ToastUtil td;
//
//    static Toast systemToast;
//    public static void show(@StringRes int resId) {
//        show(App.getInstance().getString(resId));
//    }
//
//    public static void shortShow(@StringRes int resId) {
//        shortShow(App.getInstance().getString(resId));
//    }
//
//    public static void show(String msg) {
//
//        if (systemToast == null) {
//            systemToast = Toast.makeText(App.getInstance(), msg, Toast.LENGTH_SHORT);
//        }else {
//            systemToast.setText(msg);
//        }
//        systemToast.show();
//    }
//
//    public static void shortShow(String msg) {
//        if (systemToast == null) {
//            systemToast = Toast.makeText(App.getInstance(), msg, Toast.LENGTH_SHORT);
//        }else {
//            systemToast.setText(msg);
//        }
//        systemToast.show();
//    }
//
//
//    Context context;
//    Toast toast;
//    String msg;
//    View contentView;
//    TextView tvMsg;
//
//    public ToastUtil(Context context) {
//        this.context = context;
//        contentView = View.inflate(context, R.layout.view_dialog_toast, null);
//    }
//
//    public Toast create() {
//        if (toast == null) {
//            tvMsg = (TextView) contentView.findViewById(R.id.tv_toast_msg);
//            toast = new Toast(context);
//            if (contentView == null){
//                contentView = View.inflate(context, R.layout.view_dialog_toast, null);
//            }
//            toast.setView(contentView);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.setDuration(Toast.LENGTH_LONG);
//            tvMsg.setText(msg);
//        }else {
//            tvMsg.setText(msg);
//        }
//        return toast;
//    }
//
//    public Toast createShort() {
//
//        if (toast == null) {
//            tvMsg = (TextView) contentView.findViewById(R.id.tv_toast_msg);
//            toast = new Toast(context);
//            if (contentView == null){
//                contentView = View.inflate(context, R.layout.view_dialog_toast, null);
//            }
//
//
//            toast.setView(contentView);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.setDuration(Toast.LENGTH_SHORT);
//            tvMsg.setText(msg);
//        }else {
//            tvMsg.setText(msg);
//        }
//
//
//        return toast;
//    }
//
//    public void show() {
//        if (toast != null) {
//            toast.show();
//        }
//    }
//
//    public void setText(String text) {
//        msg = text;
//    }
//}


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.freak.printtool.R;
import com.freak.printtool.hardware.app.App;


/**
 * 自定义toast格式
 *
 * @author Freak
 * @date 2019/8/13.
 */
public class ToastUtil {

    static ToastUtil td;

    public static void show(int resId) {
        show(App.getInstance().getString(resId));
    }

    public static void show(String msg) {
        if (td == null) {
            td = new ToastUtil(App.getInstance());
        }
        td.setText(msg);
        td.create().show();
    }

    public static void shortShow(String msg) {
        if (td == null) {
            td = new ToastUtil(App.getInstance());
        }
        td.setText(msg);
        td.createShort().show();
    }

    Context context;
    Toast toast;
    String msg;
    TextView tvMsg = null;

    public ToastUtil(Context context) {
        this.context = context;
    }

    public Toast create() {

        if (toast == null) {
            View contentView = View.inflate(context, R.layout.view_dialog_toast, null);
            tvMsg = (TextView) contentView.findViewById(R.id.tv_toast_msg);
            toast = new Toast(context);
            toast.setView(contentView);
            toast.setGravity(Gravity.TOP, 0, 800);
            toast.setDuration(Toast.LENGTH_LONG);
            tvMsg.setText(msg);
        } else {
            tvMsg.setText(msg);
        }

        return toast;
    }

    //不管触发多少次toast调用，只会持续一次toast显示时长
    public Toast createShort() {

        if (toast == null) {
            View contentView = View.inflate(context, R.layout.view_dialog_toast, null);
            tvMsg = (TextView) contentView.findViewById(R.id.tv_toast_msg);
            toast = new Toast(context);
            toast.setView(contentView);
            toast.setGravity(Gravity.TOP, 0, 800);
            toast.setDuration(Toast.LENGTH_SHORT);
            tvMsg.setText(msg);
        } else {
            tvMsg.setText(msg);
        }
        return toast;
    }

    public void show() {
        if (toast != null) {
            toast.show();
        }
    }

    public void setText(String text) {
        msg = text;
    }
}
