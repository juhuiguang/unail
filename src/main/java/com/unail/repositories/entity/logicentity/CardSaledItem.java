package com.unail.repositories.entity.logicentity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 橘 on 2016/8/24.
 */
public class CardSaledItem {
    @Column(name = "card_id")
    private Long cardid;
    @Column(name = "card_no")
    private String cardno;
    @Column(name = "customer_name", nullable = true)
    private String customername;

    @Column(name = "customer_phone", nullable = true)
    private String customerphone;

    @Column(name = "sales_money", nullable = true)
    private float salesmoney;

    @Column(name = "sales_time", nullable = true, columnDefinition="DATETIME")
    private Date salestime;

    @Column(name = "sales_staff", nullable = true)
    private String salesstaff;
    @Column(name = "sales_shop", nullable = true)
    private String salesshop;
    @Column(name = "shop_name", nullable = true)
    private String salesshopname;
    @Column(name="card_kind_sales",nullable=true)
    private float sales;
    @Column(name="card_kind_name",nullable=true)
    private String cardkindname;


    public Long getCardid() {
        return cardid;
    }

    public void setCardid(Long cardid) {
        this.cardid = cardid;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomerphone() {
        return customerphone;
    }

    public void setCustomerphone(String customerphone) {
        this.customerphone = customerphone;
    }

    public float getSalesmoney() {
        return salesmoney;
    }

    public void setSalesmoney(float salesmoney) {
        this.salesmoney = salesmoney;
    }

    public Date getSalestime() {
        return salestime;
    }

    public void setSalestime(Date salestime) {
        this.salestime = salestime;
    }

    public String getSalesstaff() {
        return salesstaff;
    }

    public void setSalesstaff(String salesstaff) {
        this.salesstaff = salesstaff;
    }

    public String getSalesshop() {
        return salesshop;
    }

    public void setSalesshop(String salesshop) {
        this.salesshop = salesshop;
    }

    public String getSalesshopname() {
        return salesshopname;
    }

    public void setSalesshopname(String salesshopname) {
        this.salesshopname = salesshopname;
    }

    public float getSales() {
        return sales;
    }

    public void setSales(float sales) {
        this.sales = sales;
    }

    public String getCardkindname() {
        return cardkindname;
    }

    public void setCardkindname(String cardkindname) {
        this.cardkindname = cardkindname;
    }
}

