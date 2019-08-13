package com.freak.printtool.hardware.print.bean;

import java.util.ArrayList;

/**
 * 结算打印bean类
 */

/**
 * 增加子项的支付方式，用来判断是否有优惠
 *
 * @author Freak
 * @date 2019/8/13.
 */
public class CollectionPrintBean implements PrintfBean {
    /**
     * 门店名
     */
    private String shopName;
    /**
     * 订单号
     */
    private String sn;
    /**
     * 时间
     */
    private String time;
    /**
     * 收银员ID 改成工号
     */
    private String cashierId;
    /**
     * 商品列表
     */
    private ArrayList<ProductItems> productItemses;
    /**
     * 总数量
     */
    private String quality;
    /**
     * 服务费
     */
    private String serviceFee;
    /**
     * 总现金 改为总额
     */
    private String totalCash;
    /**
     * 微信金额 改为支付方式
     */
    private String totalWeixin;
    /**
     * 折扣
     */
    private String discount;
    /**
     * 总积分
     */
    private String totalEcoin;
    /**
     * 会员卡号
     */
    private String memberCard;
    /**
     * 门店电话
     */
    private String phone;
    /**
     * 门店地址
     */
    private String address;
    /**
     * 积分余额
     */
    private String surplusEcoin;
    /**
     * 总优惠
     */
    private String discounts;
    /**
     * 实收
     */
    private String totalReceipts;

    public String getTotalReceipts() {
        return totalReceipts;
    }

    public void setTotalReceipts(String totalReceipts) {
        this.totalReceipts = totalReceipts;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
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

    public String getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(String totalCash) {
        this.totalCash = totalCash;
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
        /**
         * 商品名字
         */
        private String name;
        /**
         * 商品现价小计
         */
        private String price;
        /**
         * 商品数量
         */
        private String num;
        /**
         * 商品原价小计
         */
        private String total;
        /**
         * 这是子项的积分
         */
        private String ecoin;
        /**
         * 这是子项的支付方式
         */
        private String paymentMethod;


        /**
         * 原售价
         */
        private String momentPrice;
        /**
         * 优惠后售价
         */
        private String finalPrice;
        /**
         * 单品优惠
         */
        private String promotionPrice;
        /**
         * 数量
         */
        private String quantity;
        /**
         * 单位
         */
        private String unit;
        /**
         * 支付方式
         */
        private String paymentMethodName;



        public String getMomentPrice() {
            return momentPrice;
        }

        public void setMomentPrice(String momentPrice) {
            this.momentPrice = momentPrice;
        }

        public String getFinalPrice() {
            return finalPrice;
        }

        public void setFinalPrice(String finalPrice) {
            this.finalPrice = finalPrice;
        }

        public String getPromotionPrice() {
            return promotionPrice;
        }

        public void setPromotionPrice(String promotionPrice) {
            this.promotionPrice = promotionPrice;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getPaymentMethodName() {
            return paymentMethodName;
        }

        public void setPaymentMethodName(String paymentMethodName) {
            this.paymentMethodName = paymentMethodName;
        }

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

        public String getEcoin() {
            return ecoin;
        }

        public void setEcoin(String ecoin) {
            this.ecoin = ecoin;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        @Override
        public String toString() {
            return "ProductItems{" +
                    "name='" + name + '\'' +
                    ", price='" + price + '\'' +
                    ", momentPrice='" + momentPrice + '\'' +
                    ", finalPrice='" + finalPrice + '\'' +
                    ", ecoin='" + ecoin + '\'' +
                    ", promotionPrice='" + promotionPrice + '\'' +
                    ", quantity='" + quantity + '\'' +
                    ", unit='" + unit + '\'' +
                    ", paymentMethodName='" + paymentMethodName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CollectionPrintBean{" +
                "shopName='" + shopName + '\'' +
                ", sn='" + sn + '\'' +
                ", time='" + time + '\'' +
                ", cashierId='" + cashierId + '\'' +
                ", productItemses=" + productItemses +
                ", quality='" + quality + '\'' +
                ", serviceFee='" + serviceFee + '\'' +
                ", totalCash='" + totalCash + '\'' +
                ", totalWeixin='" + totalWeixin + '\'' +
                ", discount='" + discount + '\'' +
                ", totalEcoin='" + totalEcoin + '\'' +
                ", memberCard='" + memberCard + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", surplusEcoin='" + surplusEcoin + '\'' +
                '}';
    }
}
