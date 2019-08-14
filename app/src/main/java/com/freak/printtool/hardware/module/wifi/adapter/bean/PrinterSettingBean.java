package com.freak.printtool.hardware.module.wifi.adapter.bean;

/**
 *
 * @author Freak
 * @date 2019/8/13.
 */

public class PrinterSettingBean {
    private String ip;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "PrinterSettingBean{" +
                "ip='" + ip + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
