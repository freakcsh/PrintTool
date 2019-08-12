package com.freak.printtool.hardware.print.bean;



/**
 * 这是是为了统一打印的规格
 *
 * @anthor dmin
 * created at 2017/7/29 10:26
 */
public class CashierOrderItemsForPrintBean  {
    //这是产品的id
    private String id;
    //这是积分的数量 与数量有关 与选择有关
    private String ecoin;
    //这是现价小计 与数量有关 与选择有关
    private String price;
    //这是数量
    private String quantity;
    //这是名字
    private String name;
    //这是支付方法 与选择有关 现金 或者现金加积分
    private String paymentMethod;

//    private String productNum;
    private String momentPrice;//原价
    private String promotionPrice;//单品总优惠
    private String oriPrice;//原价小计
    private String discounts;//优惠价格
    private String finalPrice;//优惠的后售价
    private String  unit;//单位
    private String isWeighting;
    public CashierOrderItemsForPrintBean() {

    }

    /**
     * 初始化数据
     */
    public CashierOrderItemsForPrintBean(String id, String ecoin, String price, String quantity, String paymentMethod, String name) {
        this.id = id;
        this.ecoin = ecoin;
        this.price = price;
        this.quantity = quantity;//
        this.paymentMethod = paymentMethod;
        this.name = name;
    }
//下单
    public CashierOrderItemsForPrintBean(String id, String ecoin, String momentPrice, String quantity, String paymentMethod, String name,String price,String promotionPrice,String oriPrice,String isWeighting) {
        this.id = id;
        this.ecoin = ecoin;
        this.price = price;
        this.quantity = quantity;//
        this.paymentMethod = paymentMethod;
        this.name = name;
        this.momentPrice=momentPrice;
        this.promotionPrice=promotionPrice;
        this.oriPrice=oriPrice;
        this.isWeighting = isWeighting;

    }
//收银订单
//商品名字、原售价、优惠后的售价、优惠金额、数量、单位、售价小计
    public CashierOrderItemsForPrintBean(String name, String momentPrice, String finalPrice, String promotionPrice, String quantity, String unit, String price,String isWeighting) {
        this.name=name;
        this.momentPrice=momentPrice;
        this.finalPrice=finalPrice;
        this.promotionPrice=promotionPrice;
        this.quantity=quantity;
        this.unit=unit;
        this.price=price;
        this.isWeighting = isWeighting;
    }
//
//    public CashierOrderItemsForPrintBean(String id, String ecoin, String price, String quantity, String paymentMethod, String name) {
//        this.id = id;
//        this.ecoin = ecoin;
//        this.price = price;
//        this.quantity = quantity;//
//        this.paymentMethod = paymentMethod;
//        this.name = name;
//    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(String oriPrice) {
        this.oriPrice = oriPrice;
    }

    public String getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(String promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public String getMomentPrice() {
        return momentPrice;
    }

    public void setMomentPrice(String momentPrice) {
        this.momentPrice = momentPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setEcoin(String ecoin) {
        this.ecoin = ecoin;
    }

    public String getEcoin() {
        return this.ecoin;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return this.price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public String getIsWeighting() {
        return isWeighting;
    }

    public void setIsWeighting(String isWeighting) {
        this.isWeighting = isWeighting;
    }

    @Override
    public String toString() {
        return "SyncOrderItemBean{" +
                "id='" + id + '\'' +
                ", ecoin='" + ecoin + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                ", name='" + name + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
