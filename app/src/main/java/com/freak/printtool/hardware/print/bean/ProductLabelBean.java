package com.freak.printtool.hardware.print.bean;

/**
 * 这是标签打印机
 *
 * @anthor dmin
 * created at 2017/7/18 12:05
 * 现在是没有等级和规格
 */

/**
 * 增加自定义编码
 *
 * @author Freak
 * @date 2019/8/13.
 */

public class ProductLabelBean {
    //商品id
    private String productId;
    // 名字
    private String name;

    // private  List<Specifications> specifications;//规格
    // private List<SpecificationValues> specificationValues;//这是规则 具体的形式
    // private String grade;//等级 现在的json没有这个字段

    // 产地
    private String producer;
    // 零售价
    private String price;

    // 计量单位
    private String unit;
    // 条码 需要去掉商家id
    private String sn;

    //会员价
    private String cash;
    //积分
    private String eprice;

    //新增
    //库存
    private String stock;
    //是否称重
    private String isWeighing;
    //是否自定义编码
    private String isCustom;

    //是否多个条码，打印标签时使用
    private boolean multiCode;

    private String type;

    private String supervisor;//物价员

    private String rank;//等级

    private String specification;//规格

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getEprice() {
        return eprice;
    }

    public void setEprice(String eprice) {
        this.eprice = eprice;
    }

    public String getCashAndEPrice() {

        return this.getCash() + "   " + this.getEprice();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getIsWeighing() {
        return isWeighing;
    }

    public void setIsWeighing(String isWeighing) {
        this.isWeighing = isWeighing;
    }

    public String getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(String isCustom) {
        this.isCustom = isCustom;
    }

    public boolean isMultiCode() {
        return multiCode;
    }

    public void setMultiCode(boolean multiCode) {
        this.multiCode = multiCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ProductLabelBean{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", producer='" + producer + '\'' +
                ", price='" + price + '\'' +
                ", unit='" + unit + '\'' +
                ", sn='" + sn + '\'' +
                ", cash='" + cash + '\'' +
                ", eprice='" + eprice + '\'' +
                ", stock='" + stock + '\'' +
                ", isWeighing='" + isWeighing + '\'' +
                ", isCustom='" + isCustom + '\'' +
                ", multiCode=" + multiCode +
                ", type='" + type + '\'' +
                ", supervisor='" + supervisor + '\'' +
                ", rank='" + rank + '\'' +
                ", specification='" + specification + '\'' +
                '}';
    }
}
