package com.unail.repositories.entity.logicentity;

import com.unail.repositories.entity.Custom;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 橘 on 2016/11/14.
 */
public class LProductBlance {
    /**
     *
     */
    private static final long serialVersionUID = 5784591331831805484L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "balance_no", nullable = true)
    private String balanceno;

    @Column(name = "consume_time", nullable = true, columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date consumetime;

    @Column(name = "consume_pro_no", nullable = true)
    private Long consumeprono;

    @Column(name = "pro_name", nullable = true)
    private String proname;

    @Column(name = "pro_price", nullable = true)
    private Float proprice;


    @Column(name = "pro_count", nullable = true)
    private Integer procount;


    @Column(name = "staff", nullable = true)
    private Integer staff;

    @Column(name = "cashier", nullable = true)
    private Integer cashier;

    @Column(name = "user_satisfaction", nullable = true)
    private String usersatisfaction;

    @Column(name = "cashprice", nullable = true)
    private Float cashprice;

    @Column(name = "dealprice", nullable = true)
    private Float dealprice;

    @Column(name = "extraprice", nullable = true)
    private Float extraprice;

    @Column(name = "pay_total", nullable = true)
    private Float pay_total;

    @Column(name = "product_total", nullable = true)
    private Float product_total;

    @ManyToOne
    @JoinColumn(name="custom_no")
    private Custom custom;
    @Column(name = "consume_shop", nullable = true)
    private Long consumeShop;

    @Column(name = "product_type1", nullable = true)
    private String productType;

    @Column(name = "staff_name", nullable = true)
    private String staffName;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Long getConsumeShop() {
        return consumeShop;
    }

    public void setConsumeShop(Long consumeShop) {
        this.consumeShop = consumeShop;
    }

    public Custom getCustom() {
        return custom;
    }

    public void setCustom(Custom custom) {
        this.custom = custom;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBalanceno() {
        return balanceno;
    }

    public void setBalanceno(String balanceno) {
        this.balanceno = balanceno;
    }

    public Date getConsumetime() {
        return consumetime;
    }

    public void setConsumetime(Date consumetime) {
        this.consumetime = consumetime;
    }

    public Long getConsumeprono() {
        return consumeprono;
    }

    public void setConsumeprono(Long consumeprono) {
        this.consumeprono = consumeprono;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public Float getProprice() {
        return proprice;
    }

    public void setProprice(Float proprice) {
        this.proprice = proprice;
    }

    public Integer getProcount() {
        return procount;
    }

    public void setProcount(Integer procount) {
        this.procount = procount;
    }

    public Integer getStaff() {
        return staff;
    }

    public void setStaff(Integer staff) {
        this.staff = staff;
    }

    public Integer getCashier() {
        return cashier;
    }

    public void setCashier(Integer cashier) {
        this.cashier = cashier;
    }

    public String getUsersatisfaction() {
        return usersatisfaction;
    }

    public void setUsersatisfaction(String usersatisfaction) {
        this.usersatisfaction = usersatisfaction;
    }

    public Float getCashprice() {
        return cashprice;
    }

    public void setCashprice(Float cashprice) {
        this.cashprice = cashprice;
    }

    public Float getDealprice() {
        return dealprice;
    }

    public void setDealprice(Float dealprice) {
        this.dealprice = dealprice;
    }

    public Float getExtraprice() {
        return extraprice;
    }

    public void setExtraprice(Float extraprice) {
        this.extraprice = extraprice;
    }

    public Float getPay_total() {
        return pay_total;
    }

    public void setPay_total(Float pay_total) {
        this.pay_total = pay_total;
    }

    public Float getProduct_total() {
        return product_total;
    }

    public void setProduct_total(Float product_total) {
        this.product_total = product_total;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
