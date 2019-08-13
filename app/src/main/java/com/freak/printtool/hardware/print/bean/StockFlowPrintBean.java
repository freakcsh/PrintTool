package com.freak.printtool.hardware.print.bean;


import java.util.List;

/**
 * 收货打印
 *
 * @author Freak
 * @date 2019/8/13.
 */

public class StockFlowPrintBean implements PrintfBean {
    //门店名
    private String shopName;
    //收银员ID 改成工号
    private String cashierId;
    //打印的具体项
    private List<StockFlowToPrint> stockFlowToPrintList;
    //收货时间
    private String date;
    //订单号
    private String sn;
    //门店电话
    private String phone;
    //门店地址
    private String address;
    //申请总数
    private String applyTotal;
    //实收总数
    private String realTotal;
    //收货备注
    private String remark;

    public class StockFlowToPrint{
        //商品名
        private String name;
        //申请数量
        private String applyCount;
        //实收数量
        private String realCount;
        //单位
        private String unit;
        //是否散秤
        private boolean isWeighting;
        //是否拒绝
        private boolean isReject;

        public StockFlowToPrint(String name, String applyCount, String realCount, String unit, boolean isWeighting,boolean isReject) {
            this.name = name;
            this.applyCount = applyCount;
            this.realCount = realCount;
            this.unit = unit;
            this.isWeighting = isWeighting;
            this.isReject = isReject;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getApplyCount() {
            return applyCount;
        }

        public void setApplyCount(String applyCount) {
            this.applyCount = applyCount;
        }

        public String getRealCount() {
            return realCount;
        }

        public void setRealCount(String realCount) {
            this.realCount = realCount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public boolean isWeighting() {
            return isWeighting;
        }

        public void setWeighting(boolean weighting) {
            isWeighting = weighting;
        }

        public boolean isReject() {
            return isReject;
        }

        public void setReject(boolean reject) {
            isReject = reject;
        }

        @Override
        public String toString() {
            return "StockFlowToPrint{" +
                    "name='" + name + '\'' +
                    ", applyCount='" + applyCount + '\'' +
                    ", realCount='" + realCount + '\'' +
                    ", unit='" + unit + '\'' +
                    ", isWeighting=" + isWeighting +
                    ", isReject=" + isReject +
                    '}';
        }
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public List<StockFlowToPrint> getStockFlowToPrintList() {
        return stockFlowToPrintList;
    }

    public void setStockFlowToPrintList(List<StockFlowToPrint> stockFlowToPrintList) {
        this.stockFlowToPrintList = stockFlowToPrintList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApplyTotal() {
        return applyTotal;
    }

    public void setApplyTotal(String applyTotal) {
        this.applyTotal = applyTotal;
    }

    public String getRealTotal() {
        return realTotal;
    }

    public void setRealTotal(String realTotal) {
        this.realTotal = realTotal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "StockFlowPrintBean{" +
                "shopName='" + shopName + '\'' +
                ", cashierId='" + cashierId + '\'' +
                ", stockFlowToPrintList=" + stockFlowToPrintList +
                ", date='" + date + '\'' +
                ", sn='" + sn + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", applyTotal='" + applyTotal + '\'' +
                ", realTotal='" + realTotal + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
