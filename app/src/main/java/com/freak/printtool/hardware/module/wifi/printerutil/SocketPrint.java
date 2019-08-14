package com.freak.printtool.hardware.module.wifi.printerutil;


import android.util.Log;

import com.freak.printtool.hardware.utils.DateUtil;
import com.freak.printtool.hardware.utils.LogUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author Freak
 * @date 2019/8/14.
 */
public class SocketPrint {
    //定义编码方式
    private static String encoding = null;
    private String ip;
    private Socket sock = null;
    private int port;
    /**
     * 连接超时时间
     */
    private final static int SOCKET_RECEIVE_TIME_OUT = 2500;

    /**
     * 初始化Pos实例
     *
     * @param ip       打印机IP
     * @param port     打印机端口号
     * @param encoding 编码
     * @throws IOException
     */
    public SocketPrint(String ip, int port, String encoding) {
        try {
            this.ip = ip;
            this.port = port;
            if (sock != null) {
                closeIOAndSocket();
            } else {
                SocketAddress socketAddress = new InetSocketAddress(ip, port);
                sock = new Socket();
                sock.connect(socketAddress, SOCKET_RECEIVE_TIME_OUT);
            }
            if (sock.isConnected()) {
                //中文打印要看设置设置的中文格式对应的是哪一个，这个佳博wifi打印机是串口的，编码是GB2312,如果设置不对就会乱码
                String time = DateUtil.getTime();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), encoding));
                bufferedWriter.write("打印测试");
                bufferedWriter.newLine();
                bufferedWriter.write("----------------------------");
                bufferedWriter.newLine();
                bufferedWriter.write("收银员：10001");
                bufferedWriter.newLine();
                bufferedWriter.write("测试时间：" + time);
                bufferedWriter.newLine();
                bufferedWriter.write("----------------------------");
                bufferedWriter.newLine();
                bufferedWriter.flush();
                bufferedWriter.close();
                sock.close();
                LogUtil.e("已打开");
            } else {
                LogUtil.e("没有打开");
            }
        } catch (Exception e) {
            Log.e("king", e.toString());
        }
    }

    /**
     * 关闭IO流和Socket
     *
     * @throws IOException
     */
    public void closeIOAndSocket() {
        try {
            sock.close();
        } catch (Exception e) {

        }
    }
}
