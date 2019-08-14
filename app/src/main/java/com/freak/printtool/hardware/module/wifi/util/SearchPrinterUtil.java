package com.freak.printtool.hardware.module.wifi.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * @author Freak
 * @date 2019/8/13.
 */

public class SearchPrinterUtil {

    private Socket mSocket = null;
    /**
     * 打印机是否开启
     */
    public boolean printerIsOpen = false;
    /**
     * 连接超时时间
     */
    private final static int SOCKET_RECEIVE_TIME_OUT = 2500;
    /**
     * 是否搜索完成
     */
    private boolean searchFinish = false;


    public boolean isSearchFinish() {
        return searchFinish;
    }


    /**
     * 获取ip地址
     *
     * @param context 连接上下文
     * @return
     */
    public static String getIpAddress(Context context) {
        //获取网络信息对象
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        //判断是否连接网络
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                //网络是处于2G/3G/4G环境网络
//                try {
//                    for (Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces(); enumeration.hasMoreElements(); ) {
//                        NetworkInterface networkInterface = enumeration.nextElement();
//                        for (Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses(); inetAddressEnumeration.hasMoreElements(); ) {
//                            InetAddress inetAddress = inetAddressEnumeration.nextElement();
//                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
//                                return inetAddress.getHostAddress();
//                            }
//                        }
//                    }
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }
                return null;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                //网络是wifi环境网络
                //获取wifi管理者
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                //获取wifi信息
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                //获取ip地址前三段
                String ipv4 = intIPToStringIP(wifiInfo.getIpAddress());
                return ipv4;
            }
        } else {

        }
        return null;
    }


    /**
     * 将得到的int类型的IP转换为String类型,截取ip的前三段
     *
     * @param ip 获取的wifi地址信息
     * @return
     */
    public static String intIPToStringIP(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + ".";
    }

    /**
     * 搜索已经打开的打印机
     *
     * @param ipAddress ip地址
     * @param netPort   端口
     * @return
     */
    public boolean search(String ipAddress, int netPort) {
        if (mSocket == null) {
            try {
                SocketAddress socketAddress = new InetSocketAddress(Inet4Address.getByName(ipAddress), netPort);
                mSocket = new Socket();
                mSocket.connect(socketAddress, SOCKET_RECEIVE_TIME_OUT);
                if (mSocket.isConnected()) {
                    printerIsOpen = true;
                }
            } catch (UnknownHostException e) {
                closeSocket();
                return false;
            } catch (IOException e) {
                closeSocket();
                return false;
            }
        } else {
            try {
                mSocket.close();
                SocketAddress socketAddress = new InetSocketAddress(Inet4Address.getByName(ipAddress), netPort);
                mSocket.connect(socketAddress, SOCKET_RECEIVE_TIME_OUT);
                if (mSocket.isConnected()) {
                    printerIsOpen = true;
                }
            } catch (IOException e) {
                closeSocket();
                return false;
            }
        }
        if (printerIsOpen) {
            closeSocket();
        }
        return printerIsOpen;
    }

    /**
     * 关闭socket
     */
    private void closeSocket() {
        if (mSocket != null) {
            try {
                mSocket.close();
                mSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
