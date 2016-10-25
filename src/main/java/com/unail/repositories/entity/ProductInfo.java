package com.unail.repositories.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name = "product_info")
public class ProductInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6370458167426037874L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "product_no")
	private Long productno ;
	
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

	
	
	
}
