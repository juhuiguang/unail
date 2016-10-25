package com.unail.repositories.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javassist.SerialVersionUID;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 橘 on 2016/6/20.
 */
@Entity
@Table(name = "shop")
public class Shop implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4073322830620998772L;
	
    @Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)   
    @Column(name = "shop_id")
    private Long shopNo;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "shop_addr")
    private String shopAddr;

    @Column(name = "shop_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date shoptime; 
    
    @Column(name = "shop_no")
    private String shopcode;

    @Column(name = "shop_phone")
    private String shopPhone1;

    @Column(name = "shop_phone2")
    private String shopPhone2;

    public Long getShopNo() {
        return shopNo;
    }

    public void setShopNo(Long shopNo) {
        this.shopNo = shopNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddr() {
        return shopAddr;
    }

    public void setShopAddr(String shopAddr) {
        this.shopAddr = shopAddr;
    }

    public Date getShoptime() {
		return shoptime;
	}

	public void setShoptime(Date shoptime) {
		this.shoptime = shoptime;
	}

	public String getShopcode() {
        return shopcode;
    }

    public void setShopcode(String shopcode) {
        this.shopcode = shopcode;
    }

    public String getShopPhone1() {
        return shopPhone1;
    }

    public void setShopPhone1(String shopPhone1) {
        this.shopPhone1 = shopPhone1;
    }

    public String getShopPhone2() {
        return shopPhone2;
    }

    public void setShopPhone2(String shopPhone2) {
        this.shopPhone2 = shopPhone2;
    }
}
