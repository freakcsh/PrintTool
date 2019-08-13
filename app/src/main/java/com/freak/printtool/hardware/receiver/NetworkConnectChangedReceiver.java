package com.freak.printtool.hardware.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.freak.printtool.hardware.utils.ToastUtil;

/**
 * 网络状态改变监听
 *
 * @author Freak
 * @date 2019/8/13.
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    private boolean isWifiConnected = false;
    private boolean isEthernetConnected = false;
    private boolean isMobileConnected = false;
    /**
     * 上一次连接网络类型 ,防止触发多次广播
     */
    private int lastConnectedType = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //当前连接可用的网络
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            //获取以太网连接的信息
            NetworkInfo ethernetNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

            if (wifiNetworkInfo.isConnected() && wifiNetworkInfo.isAvailable()) {
                isWifiConnected = true;
            } else {
                isWifiConnected = false;
            }
            if (ethernetNetworkInfo.isConnected() && ethernetNetworkInfo.isAvailable()) {
                isEthernetConnected = true;
            } else {
                isEthernetConnected = false;
            }
            if (dataNetworkInfo.isConnected() && dataNetworkInfo.isAvailable()) {
                isMobileConnected = true;
            } else {
                isMobileConnected = false;
            }
            if (isWifiConnected) {
                if (lastConnectedType != ConnectivityManager.TYPE_WIFI) {
                    ToastUtil.shortShow("WIFI已连接");
                }
                lastConnectedType = ConnectivityManager.TYPE_WIFI;
            } else {
                if (isEthernetConnected) {
                    if (lastConnectedType != ConnectivityManager.TYPE_ETHERNET) {
                        ToastUtil.shortShow("以太网已连接");
                    }
                    lastConnectedType = ConnectivityManager.TYPE_ETHERNET;
                } else {
                    if (isMobileConnected) {
                        if (lastConnectedType != ConnectivityManager.TYPE_MOBILE) {
                            ToastUtil.shortShow("移动数据已连接");
                        }
                        lastConnectedType = ConnectivityManager.TYPE_MOBILE;
                    } else {
                        if (lastConnectedType != -1) {
                            // TODO wifi连接时断开，而以太网还连接着时，有个没有网络的空档
                            if (activeNetworkInfo == null) {
                                ToastUtil.shortShow("没有网络");
                            }
                        }
                        lastConnectedType = -1;
                    }
                }
            }
        } else {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            //用于存放网络连接信息
            //通过循环将网络信息逐个取出来
            for (int i = 0; i < networks.length; i++) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                if (networkInfo != null) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        if (networkInfo.isConnected() && networkInfo.isAvailable()) {
                            isWifiConnected = true;
                        } else {
                            isWifiConnected = false;
                        }
                    } else if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        if (networkInfo.isConnected() && networkInfo.isAvailable()) {
                            isEthernetConnected = true;
                        } else {
                            isEthernetConnected = false;
                        }
                    } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        if (networkInfo.isConnected() && networkInfo.isAvailable()) {
                            isMobileConnected = true;
                        } else {
                            isMobileConnected = false;
                        }
                    }
                }
            }
            if (isWifiConnected) {
                if (lastConnectedType != ConnectivityManager.TYPE_WIFI) {
                    ToastUtil.shortShow("WIFI已连接");
                }
                lastConnectedType = ConnectivityManager.TYPE_WIFI;
            } else {
                if (isEthernetConnected) {
                    if (lastConnectedType != ConnectivityManager.TYPE_ETHERNET) {
                        ToastUtil.shortShow("以太网已连接");
                    }
                    lastConnectedType = ConnectivityManager.TYPE_ETHERNET;
                } else {
                    if (isMobileConnected) {
                        if (lastConnectedType != ConnectivityManager.TYPE_MOBILE) {
                            ToastUtil.shortShow("移动数据已连接");
                        }
                        lastConnectedType = ConnectivityManager.TYPE_MOBILE;
                    } else {
                        ToastUtil.shortShow("没有网络");
                        lastConnectedType = -1;
                    }
                }
            }
        }
    }

    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "移动网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        } else if (type == ConnectivityManager.TYPE_ETHERNET) {
            connType = "以太网";
        }
        return connType;
    }
}

