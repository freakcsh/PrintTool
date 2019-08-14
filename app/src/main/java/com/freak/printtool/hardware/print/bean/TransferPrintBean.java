package com.freak.printtool.hardware.print.bean;

/**
 * 交接班打印bean类
 * @author Freak
 * @date 2019/8/13.
 */

public class TransferPrintBean implements PrintfBean {
    //抬头是 交接班单据
    //门店名
    private String shopName;
    //收银员ID 改成工号
    private String cashierJobNumber;
    //开始时间
    private String beginTime;
    //退出时间
    private String endTime;

    //和列表一一对应
    private String allBill;
    private String shopBill;
    private String shopReturn;
    private String netBill;
    private String netReturn;

    private String allMoney;
    private String cash;
    private String weixin;
    private String pos;
    private String balanceMoney;
    private String cashMoney;
    private String shopReturnMoney;
    private String netPay;
    private String netShopReturnMoney;

    private String allCash;
    private String merchandiseCashMoney;
    private String standbyCash;
    private String buyCardCash;

//    private String sellCash;
//    private String renewCardCash;

    private String allCard;
    private String allPayCardMoney;
//    private String buyCardBill;
//    private String renewCardBill;

    private String sn;

    private String remark;

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getAllPayCardMoney() {
        return allPayCardMoney;
    }

    public void setAllPayCardMoney(String allPayCardMoney) {
        this.allPayCardMoney = allPayCardMoney;
    }

    public String getMerchandiseCashMoney() {
        return merchandiseCashMoney;
    }

    public void setMerchandiseCashMoney(String merchandiseCashMoney) {
        this.merchandiseCashMoney = merchandiseCashMoney;
    }

    public String getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney(String balanceMoney) {
        this.balanceMoney = balanceMoney;
    }

    public String getCashMoney() {
        return cashMoney;
    }

    public void setCashMoney(String cashMoney) {
        this.cashMoney = cashMoney;
    }

    public String getShopReturnMoney() {
        return shopReturnMoney;
    }

    public void setShopReturnMoney(String shopReturnMoney) {
        this.shopReturnMoney = shopReturnMoney;
    }

    public String getNetShopReturnMoney() {
        return netShopReturnMoney;
    }

    public void setNetShopReturnMoney(String netShopReturnMoney) {
        this.netShopReturnMoney = netShopReturnMoney;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCashierJobNumber() {
        return cashierJobNumber;
    }

    public void setCashierJobNumber(String cashierJobNumber) {
        this.cashierJobNumber = cashierJobNumber;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAllBill() {
        return allBill;
    }

    public void setAllBill(String allBill) {
        this.allBill = allBill;
    }

    public String getShopBill() {
        return shopBill;
    }

    public void setShopBill(String shopBill) {
        this.shopBill = shopBill;
    }

    public String getShopReturn() {
        return shopReturn;
    }

    public void setShopReturn(String shopReturn) {
        this.shopReturn = shopReturn;
    }

    public String getNetBill() {
        return netBill;
    }

    public void setNetBill(String netBill) {
        this.netBill = netBill;
    }

    public String getNetReturn() {
        return netReturn;
    }

    public void setNetReturn(String netReturn) {
        this.netReturn = netReturn;
    }

    public String getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(String allMoney) {
        this.allMoney = allMoney;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getAllCash() {
        return allCash;
    }

    public void setAllCash(String allCash) {
        this.allCash = allCash;
    }

//    public String getSellCash() {
//        return sellCash;
//    }
//
//    public void setSellCash(String sellCash) {
//        this.sellCash = sellCash;
//    }

    public String getStandbyCash() {
        return standbyCash;
    }

    public void setStandbyCash(String standbyCash) {
        this.standbyCash = standbyCash;
    }

    public String getBuyCardCash() {
        return buyCardCash;
    }

    public void setBuyCardCash(String buyCardCash) {
        this.buyCardCash = buyCardCash;
    }

//    public String getRenewCardCash() {
//        return renewCardCash;
//    }
//
//    public void setRenewCardCash(String renewCardCash) {
//        this.renewCardCash = renewCardCash;
//    }

    public String getAllCard() {
        return allCard;
    }

    public void setAllCard(String allCard) {
        this.allCard = allCard;
    }

//    public String getBuyCardBill() {
//        return buyCardBill;
//    }
//
//    public void setBuyCardBill(String buyCardBill) {
//        this.buyCardBill = buyCardBill;
//    }
//
//    public String getRenewCardBill() {
//        return renewCardBill;
//    }
//
//    public void setRenewCardBill(String renewCardBill) {
//        this.renewCardBill = renewCardBill;
//    }

    public String getNetPay() {
        return netPay;
    }

    public void setNetPay(String netPay) {
        this.netPay = netPay;
    }

    @Override
    public String toString() {
        return "TransferPrintBean{" +
                "shopName='" + shopName + '\'' +
                ", cashierJobNumber='" + cashierJobNumber + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", allBill='" + allBill + '\'' +
                ", shopBill='" + shopBill + '\'' +
                ", shopReturn='" + shopReturn + '\'' +
                ", netBill='" + netBill + '\'' +
                ", netReturn='" + netReturn + '\'' +
                ", allMoney='" + allMoney + '\'' +
                ", cash='" + cash + '\'' +
                ", weixin='" + weixin + '\'' +
                ", pos='" + pos + '\'' +
                ", balanceMoney"+balanceMoney+'\''+
                ", cashMoney"+cashMoney+'\''+
                ", shopReturnMoney"+shopReturnMoney+'\''+
                ", netPay='" + netPay + '\'' +
                ", netShopReturnMoney"+netShopReturnMoney+'\''+
                ", allCash='" + allCash + '\'' +
                ", merchandiseCashMoney='" + merchandiseCashMoney + '\'' +
                ", standbyCash='" + standbyCash + '\'' +
                ", buyCardCash='" + buyCardCash + '\'' +
                ", allCard='" + allCard + '\'' +
                ", allPayCardMoney='" + allPayCardMoney + '\'' +
                '}';
    }
}
