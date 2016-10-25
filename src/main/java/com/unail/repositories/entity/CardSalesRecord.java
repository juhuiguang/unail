package com.unail.repositories.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "card_sales_record")
public class CardSalesRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7957182634879035822L;

	@Id
	@Column(name = "card_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
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
	@Temporal(TemporalType.TIMESTAMP)
	private Date salestime;
	
	@Column(name = "sales_staff", nullable = true)
	private String salesstaff;
	@Column(name = "sales_shop", nullable = true)
	private String salesshop;

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
}
