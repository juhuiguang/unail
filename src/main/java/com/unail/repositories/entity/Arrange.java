package com.unail.repositories.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "arrange")
public class Arrange implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2504513633791493987L;
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)   
	@Column(name = "arrange_no")
	private Long arrangeno;
	
	@Column(name = "custom_name", nullable = true)
	private String customname;
	
	@Column(name = "custom_phone", nullable = true)
	private String customphone;
	
	@Column(name = "custom_type", nullable = true)
	private String customtype;
	
	@Column(name = "arrange_time", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date arrangetime;

	@Column(name = "if_finish", nullable = true, columnDefinition = "0")
	private int iffinish;
	
	@Column(name = "arrange_product", nullable = true)
	private String arrangeproduct;
	
	@Column(name = "arrange_shop", nullable = true)
	private Long arrangeshop;

	public Long getArrangeno() {
		return arrangeno;
	}

	public void setArrangeno(Long arrangeno) {
		this.arrangeno = arrangeno;
	}

	public String getCustomname() {
		return customname;
	}

	public void setCustomname(String customname) {
		this.customname = customname;
	}

	public String getCustomphone() {
		return customphone;
	}

	public void setCustomphone(String customphone) {
		this.customphone = customphone;
	}

	public String getCustomtype() {
		return customtype;
	}

	public void setCustomtype(String customtype) {
		this.customtype = customtype;
	}

	public Date getArrangetime() {
		return arrangetime;
	}

	public void setArrangetime(Date arrangetime) {
		this.arrangetime = arrangetime;
	}

	public int getIffinish() {
		return iffinish;
	}

	public void setIffinish(int iffinish) {
		this.iffinish = iffinish;
	}

	public String getArrangeproduct() {
		return arrangeproduct;
	}

	public void setArrangeproduct(String arrangeproduct) {
		this.arrangeproduct = arrangeproduct;
	}

	public Long getArrangeshop() {
		return arrangeshop;
	}

	public void setArrangeshop(Long arrangeshop) {
		this.arrangeshop = arrangeshop;
	}
	

}
