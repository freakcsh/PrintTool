package com.freak.printtool.hardware.print.bean;

import java.util.ArrayList;

/**
 * 网络订单打印bean类
 * @author Freak
 * @date 2019/8/13.
 */

public class WebOrderPrintBean implements PrintfBean {
    //门店名
    private String shopName;
    //订单号
    private String sn;
    //时间
    private String time;
    //收银员ID
    private String cashierId;
    //商品列表
    private ArrayList<ProductItems> productItemses;
    //总数量
    private String quality;
    //服务费
    private String serviceFee;
    //微信金额
    private String totalWeixin;
    //折扣
    private String discount;
    //用户名字
    private String userName;
    //用户电话
    private String userPhone;
    //用户地址
    private String userAddress;
    //总积分
    private String totalEcoin;
    //会员卡号
    private String memberCard;
    //门店电话
    private String phone;
    //门店地址
    private String address;
    //积分余额
    private String surplusEcoin;
    //配送方式
    private String deliveryType;
    //配送费
    private String DeliveryFee;

    public String getDeliveryFee() {
        return DeliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        DeliveryFee = deliveryFee;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public ArrayList<ProductItems> getProductItemses() {
        return productItemses;
    }

    public void setProductItemses(ArrayList<ProductItems> productItemses) {
        this.productItemses = productItemses;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getTotalWeixin() {
        return totalWeixin;
    }

    public void setTotalWeixin(String totalWeixin) {
        this.totalWeixin = totalWeixin;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotalEcoin() {
        return totalEcoin;
    }

    public void setTotalEcoin(String totalEcoin) {
        this.totalEcoin = totalEcoin;
    }

    public String getMemberCard() {
        return memberCard;
    }

    public void setMemberCard(String memberCard) {
        this.memberCard = memberCard;
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

    public String getSurplusEcoin() {
        return surplusEcoin;
    }

    public void setSurplusEcoin(String surplusEcoin) {
        this.surplusEcoin = surplusEcoin;
    }

    public static class ProductItems {
        //商品名字
        private String name;
        //商品单价
        private String price;
        //商品数量
        private String num;
        //商品小计
        private String total;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return name + "  " +
                    price + "   " +
                    num + " " +
                    total + "\n";
        }
    }

    @Override
    public String toString() {
        return "WebOrderPrintBean{" +
                "shopName='" + shopName + '\'' +
                ", sn='" + sn + '\'' +
                ", time='" + time + '\'' +
                ", cashierId='" + cashierId + '\'' +
                ", productItemses=" + productItemses +
                ", quality='" + quality + '\'' +
                ", serviceFee='" + serviceFee + '\'' +
                ", totalWeixin='" + totalWeixin + '\'' +
                ", discount='" + discount + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", totalEcoin='" + totalEcoin + '\'' +
                ", memberCard='" + memberCard + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", surplusEcoin='" + surplusEcoin + '\'' +
                '}';
    }
}
