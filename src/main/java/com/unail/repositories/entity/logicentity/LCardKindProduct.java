package com.unail.repositories.entity.logicentity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by 橘 on 2016/7/19.
 */
public class LCardKindProduct {
    @Column(name = "id")
    private Long id;
    @Column(name = "card_kind_no", nullable = true)
    private Long cardkindno;

    @Column(name = "product_no", nullable = true)
    private Long productno;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productname;

    @Column(name = "product_letter", nullable = true, length = 30)
    private String productletter;

    @Column(name = "product_type1", nullable = true)
    private String producttype1;

    @Column(name = "product_type2", nullable = true)
    private String producttype2;

    @Column(name = "product_price1", nullable = true)
    private float productprice1;

    @Column(name = "product_price2", nullable = true)
    private float productprice2;

    @Column(name = "product_unit", nullable = true)
    private String productunit;

    @Column(name = "product_count", nullable = true)
    private String productcount;

    @Column(name = "product_decs", nullable = true)
    private String productdecs;

    @Column(name = "product_cttime", nullable = true)
    private Date productcttime;

    @Column(name = "product_user", nullable = true)
    private String productuser;

    @Column(name = "serve_discount", nullable = true)
    private float servediscount;

    @Column(name = "serve_times", nullable = true)
    private int servetimes;

    @Column(name = "serve_cycle", nullable = true)
    private int servecycle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCardkindno() {
        return cardkindno;
    }

    public void setCardkindno(Long cardkindno) {
        this.cardkindno = cardkindno;
    }

    public Long getProductno() {
        return productno;
    }

    public void setProductno(Long productno) {
        this.productno = productno;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductletter() {
        return productletter;
    }

    public void setProductletter(String productletter) {
        this.productletter = productletter;
    }

    public String getProducttype1() {
        return producttype1;
    }

    public void setProducttype1(String producttype1) {
        this.producttype1 = producttype1;
    }

    public String getProducttype2() {
        return producttype2;
    }

    public void setProducttype2(String producttype2) {
        this.producttype2 = producttype2;
    }

    public float getProductprice1() {
        return productprice1;
    }

    public void setProductprice1(float productprice1) {
        this.productprice1 = productprice1;
    }

    public float getProductprice2() {
        return productprice2;
    }

    public void setProductprice2(float productprice2) {
        this.productprice2 = productprice2;
    }

    public String getProductunit() {
        return productunit;
    }

    public void setProductunit(String productunit) {
        this.productunit = productunit;
    }

    public String getProductcount() {
        return productcount;
    }

    public void setProductcount(String productcount) {
        this.productcount = productcount;
    }

    public String getProductdecs() {
        return productdecs;
    }

    public void setProductdecs(String productdecs) {
        this.productdecs = productdecs;
    }

    public Date getProductcttime() {
        return productcttime;
    }

    public void setProductcttime(Date productcttime) {
        this.productcttime = productcttime;
    }

    public String getProductuser() {
        return productuser;
    }

    public void setProductuser(String productuser) {
        this.productuser = productuser;
    }

    public float getServediscount() {
        return servediscount;
    }

    public void setServediscount(float servediscount) {
        this.servediscount = servediscount;
    }

    public int getServetimes() {
        return servetimes;
    }

    public void setServetimes(int servetimes) {
        this.servetimes = servetimes;
    }

    public int getServecycle() {
        return servecycle;
    }

    public void setServecycle(int servecycle) {
        this.servecycle = servecycle;
    }
}
