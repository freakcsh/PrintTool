package com.freak.printtool.hardware.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.freak.printtool.R;
import com.freak.printtool.hardware.base.IActivityStatusBar;
import com.freak.printtool.hardware.module.receipt.printreceipt.MyUsbPrinterUtil;
import com.freak.printtool.hardware.module.receipt.printreceipt.PrintCategory;
import com.freak.printtool.hardware.receiver.NetworkConnectChangedReceiver;
import com.freak.printtool.hardware.receiver.UsbDeviceConnectChangedReceiver;
import com.freak.printtool.hardware.utils.LogUtil;
import com.posin.usbprinter.UsbPrinter;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 全局配置
 *
 * @author Freak
 * @date 2019/8/13.
 */

public class App extends Application {
    public static final int DESIGN_WIDTH = 375;
    private static App instance;
    private Set<Activity> allActivities;
    private NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;
    /**
     * 表示是否连接
     */
    public boolean isConnected;
    /**
     * 表示是否是移动网络
     */
    public boolean isMobile;
    /**
     * 表示是否是WiFi
     */
    public boolean isWifi;
    /**
     * 表示WiFi开关是否打开
     */
    public boolean isEnablaWifi;
    /**
     * 表示移动网络数据是否打开
     */
    public boolean isEnableMobile;
    /**
     * 表示是否是以太网
     */
    public boolean isEthernet;

    private UsbPrinter usbPrinter = null;

    private MyUsbPrinterUtil myUsbPrinterUtil = null;

    public UsbDevice receiptUSBDevice = null;

    private PrintCategory mpPrintCategory = null;

    private UsbDeviceConnectChangedReceiver mUsbDeviceConnectChangedReceiver;


    public static synchronized App getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LogUtil.init("PrintTool", true);
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //限制竖屏
                //8.0.0版本系统，同时设置竖屏和设置全屏透明冲突，
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                resetDensity(getApplicationContext(), DESIGN_WIDTH);
                resetDensity(activity, DESIGN_WIDTH);
                setImmersiveStatusBar(activity);

            }

            @Override
            public void onActivityStarted(Activity activity) {
                setToolBar(activity);
                resetDensity(getApplicationContext(), DESIGN_WIDTH);
                resetDensity(activity, DESIGN_WIDTH);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                resetDensity(getApplicationContext(), DESIGN_WIDTH);
                resetDensity(activity, DESIGN_WIDTH);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                resetDensity(getApplicationContext(), DESIGN_WIDTH);
                resetDensity(activity, DESIGN_WIDTH);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
        //网络状态 USB设备监听
        initReceiver();

    }

    /**
     * 设置ToolBar
     *
     * @param activity
     */
    private void setToolBar(final Activity activity) {
        if (activity.findViewById(R.id.tool_bar) != null && ((AppCompatActivity) activity).getSupportActionBar() == null) {
            Toolbar toolbar = activity.findViewById(R.id.tool_bar);
            if (!TextUtils.isEmpty(activity.getTitle())) {
                toolbar.setTitle(activity.getTitle());
            } else {
                toolbar.setTitle("");
            }

            if (((IActivityStatusBar) activity).getStatusBarColor() != 0) {
                toolbar.setBackgroundColor(((IActivityStatusBar) activity).getStatusBarColor());
            } else {
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            }

            ((AppCompatActivity) activity).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
        }
    }

    /**
     * 设置状态栏
     *
     * @param activity
     */
    private void setImmersiveStatusBar(Activity activity) {
        if (activity instanceof IActivityStatusBar) {
            if (((IActivityStatusBar) activity).getStatusBarColor() != 0) {
                setTranslucentStatus(activity);
                addImmersiveStatusBar(activity, ((IActivityStatusBar) activity).getStatusBarColor());
            } else {
                if (((IActivityStatusBar) activity).getDrawableStatusBar() != 0) {
                    setTranslucentStatus(activity);
                    addImmersiveShadeStatusBar(activity, ((IActivityStatusBar) activity).getDrawableStatusBar());
                }
            }
        }
    }

    /**
     * 添加自定义状态栏
     *
     * @param activity
     */
    private void addImmersiveStatusBar(Activity activity, int color) {
        ViewGroup contentFrameLayout = activity.findViewById(android.R.id.content);
        View contentView = contentFrameLayout.getChildAt(0);
        if (contentView != null && Build.VERSION.SDK_INT >= 14) {
            contentView.setFitsSystemWindows(true);
        }

        View statusBar = new View(activity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (hasNotchInScreen(activity)) {
            params.height = getNotchSize(activity)[1];
        } else {
            params.height = getStatusBarHeight();
        }
        params.height = getStatusBarHeight();
        statusBar.setLayoutParams(params);
        statusBar.setBackgroundColor(color);
        contentFrameLayout.addView(statusBar);
    }

    /**
     * 设置状态栏渐变色
     *
     * @param activity activity
     * @param drawable drawable资源文件
     */
    private void addImmersiveShadeStatusBar(Activity activity, @DrawableRes int drawable) {
        ViewGroup contentFrameLayout = activity.findViewById(android.R.id.content);
        View contentView = contentFrameLayout.getChildAt(0);
        if (contentView != null && Build.VERSION.SDK_INT >= 14) {
            contentView.setFitsSystemWindows(true);
        }

        View statusBar = new View(activity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (hasNotchInScreen(activity)) {
            params.height = getNotchSize(activity)[1];
        } else {
            params.height = getStatusBarHeight();
        }
//        params.height = getStatusBarHeight();
        statusBar.setLayoutParams(params);
        statusBar.setBackgroundResource(drawable);
        contentFrameLayout.addView(statusBar);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 设置状态栏为透明
     *
     * @param activity
     */
    private void setTranslucentStatus(Activity activity) {
        //******** 5.0以上系统状态栏透明 ********

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 以pt为单位重新计算大小
     */
    public static void resetDensity(Context context, float designWidth) {
        if (context == null) {
            return;
        }
        Point size = new Point();
        ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(size);
        Resources resources = context.getResources();
        resources.getDisplayMetrics().xdpi = size.x / designWidth * 72f;
        DisplayMetrics metrics = getMetricsOnMIUI(context.getResources());
        if (metrics != null) {
            metrics.xdpi = size.x / designWidth * 72f;
        }
    }

    /**
     * 解决MIUI屏幕适配问题
     *
     * @param resources
     * @return
     */
    private static DisplayMetrics getMetricsOnMIUI(Resources resources) {
        if ("MiuiResources".equals(resources.getClass().getSimpleName()) || "XResources".equals(resources.getClass().getSimpleName())) {
            try {
                Field field = Resources.class.getDeclaredField("mTmpMetrics");
                field.setAccessible(true);
                return (DisplayMetrics) field.get(resources);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
    /**
     * 判断是否是刘海屏  华为手机
     *
     * @param context
     * @return
     */
    public static boolean hasNotchInScreen(Context context) {

        boolean ret = false;

        try {

            ClassLoader cl = context.getClassLoader();

            Class hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");

            Method get = hwNotchSizeUtil.getMethod("hasNotchInScreen");

            ret = (boolean) get.invoke(hwNotchSizeUtil);

        } catch (ClassNotFoundException e) {

            Log.e("test", "hasNotchInScreen ClassNotFoundException");

        } catch (NoSuchMethodException e) {

            Log.e("test", "hasNotchInScreen NoSuchMethodException");

        } catch (Exception e) {

            Log.e("test", "hasNotchInScreen Exception");

        }
        LogUtil.e("是否刘海屏-->" + ret);
        return ret;

    }

    /**
     * 获取刘海屏尺寸 华为手机
     *
     * @param context
     * @return
     */
    public static int[] getNotchSize(Context context) {

        int[] ret = new int[]{0, 0};

        try {

            ClassLoader cl = context.getClassLoader();

            Class hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");

            Method get = hwNotchSizeUtil.getMethod("getNotchSize");

            ret = (int[]) get.invoke(hwNotchSizeUtil);

        } catch (ClassNotFoundException e) {

            LogUtil.e("getNotchSize ClassNotFoundException");

        } catch (NoSuchMethodException e) {

            LogUtil.e("getNotchSize NoSuchMethodException");

        } catch (Exception e) {

            LogUtil.e("getNotchSize Exception");

        }
        LogUtil.e("刘海屏尺寸-->" + ret.toString());
        return ret;

    }
    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    /**
     * 退出app
     */
    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        //注销网络监听
        if (mNetworkConnectChangedReceiver != null) {
            unregisterReceiver(mNetworkConnectChangedReceiver);
        }
        if (mUsbDeviceConnectChangedReceiver != null) {
            unregisterReceiver(mUsbDeviceConnectChangedReceiver);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private void initReceiver() {
        //网络
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.ic_network_off.WIFI_STATE_CHANGED");
        filter.addAction("android.net.ic_network_off.STATE_CHANGE");
        filter.setPriority(1000);
        if (mNetworkConnectChangedReceiver == null) {
            mNetworkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        }
        registerReceiver(mNetworkConnectChangedReceiver, filter);
        //USB
        IntentFilter filter1 = new IntentFilter();
        //筛选的条件
        filter1.addAction("android.hardware.usb.action.USB_STATE");
        filter1.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        filter1.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        filter1.setPriority(1000);
        if (mUsbDeviceConnectChangedReceiver == null) {
            mUsbDeviceConnectChangedReceiver = new UsbDeviceConnectChangedReceiver();
        }
        //注册广播 动态注册
        registerReceiver(mUsbDeviceConnectChangedReceiver, filter1);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mNetworkConnectChangedReceiver != null) {
            unregisterReceiver(mNetworkConnectChangedReceiver);
        }
        if (mUsbDeviceConnectChangedReceiver != null) {
            unregisterReceiver(mUsbDeviceConnectChangedReceiver);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mNetworkConnectChangedReceiver != null) {
            unregisterReceiver(mNetworkConnectChangedReceiver);
        }
        if (mUsbDeviceConnectChangedReceiver != null) {
            unregisterReceiver(mUsbDeviceConnectChangedReceiver);
        }
    }

    public boolean isConnected() {
        return isWifi || isMobile;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isMobile() {
        return isMobile;
    }

    public boolean isEthernet() {
        return isEthernet;
    }

    public void setEthernet(boolean ethernet) {
        isEthernet = ethernet;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    public boolean isWifi() {
        return isWifi;
    }

    public void setWifi(boolean wifi) {
        isWifi = wifi;
    }

    public boolean isEnablaWifi() {
        return isEnablaWifi;
    }

    public void setEnablaWifi(boolean enablaWifi) {
        isEnablaWifi = enablaWifi;
    }

    public boolean isEnableMobile() {
        return isEnableMobile;
    }

    public void setEnableMobile(boolean enableMobile) {
        isEnableMobile = enableMobile;
    }

    public MyUsbPrinterUtil getMyUsbPrinterUtil() {
        return myUsbPrinterUtil;
    }

    public UsbDevice getReceiptUSBDevice() {
        return receiptUSBDevice;
    }

    public PrintCategory getMpPrintCategory() {
        return mpPrintCategory;
    }

    public void setMpPrintCategory(PrintCategory mpPrintCategory) {
        this.mpPrintCategory = mpPrintCategory;
    }

    public void setReceiptUSBDevice(UsbDevice receiptUSBDevice) {
        this.receiptUSBDevice = receiptUSBDevice;
    }

    public void setMyUsbPrinterUtil(MyUsbPrinterUtil myUsbPrinterUtil) {
        this.myUsbPrinterUtil = myUsbPrinterUtil;
    }

    public UsbPrinter getUsbPrinter() {
        return usbPrinter;
    }

    public void setUsbPrinter(UsbPrinter usbPrinter) {
        this.usbPrinter = usbPrinter;
    }
}
