package com.unail.repositories.entity;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.*;

@Entity
@Table(name = "shop_product")
public class ShopProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1073826906671046981L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "shop_pro_no", nullable = true)
	private String shopprono;
	
	@Column(name = "shop_no", nullable = true)
	private Long shopno;
	
	@Column(name = "product_no", nullable = true)
	private Long productno;

	@Column(name = "product_type1", nullable = true)
	private String producttype1;
	
	@Column(name = "product_price1", nullable = true)
	private Float productprice1;
	
	@Column(name = "product_price2", nullable = true)
	private Float productprice2;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShopprono() {
		return shopprono;
	}

	public void setShopprono(String shopprono) {
		this.shopprono = shopprono;
	}

	public Long getShopno() {
		return shopno;
	}

	public void setShopno(Long shopno) {
		this.shopno = shopno;
	}

	public Long getProductno() {
		return productno;
	}

	public void setProductno(Long productno) {
		this.productno = productno;
	}

	public String getProducttype1() {
		return producttype1;
	}

	public void setProducttype1(String producttype1) {
		this.producttype1 = producttype1;
	}

	public Float getProductprice1() {
		return productprice1;
	}

	public void setProductprice1(float productprice1) {
		this.productprice1 = productprice1;
	}

	public Float getProductprice2() {
		return productprice2;
	}

	public void setProductprice2(float productprice2) {
		this.productprice2 = productprice2;
	}
	
	
}
