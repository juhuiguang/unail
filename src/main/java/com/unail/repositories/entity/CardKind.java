package com.unail.repositories.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.*;

import com.mysql.fabric.xmlrpc.base.Data;

@Entity
@Table(name = "card_kind")
public class CardKind implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2078604716794573040L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "card_kind_no")
	private Long cardkindno;

	@Column(name="card_kind_type")
	private String cardkindtype;

	public String getCardkindtype() {
		return cardkindtype;
	}

	public void setCardkindtype(String cardkindtype) {
		this.cardkindtype = cardkindtype;
	}

	@Column(name = "card_kind_name", nullable = true)
	private String cardkindname;
	
	@Column(name = "card_kind_desc", nullable = true)
	private String cardkinddesc;
	
	@Column(name = "card_kind_duetime", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date cardkindduetime;
	
	@Column(name = "card_kind_sales", nullable = true)
	private float cardkindsales;

	@Column(name = "card_kind_rate", nullable = true)
	private Float cardkindrate=1.0f;
	
	@Column(name = "if_calculate_sales", nullable = true, columnDefinition = "0")
	private int ifcalculatesales;
	
	@Column(name = "card_kind_useshop", nullable = true)
	private String cardkinduseshop;
	
	@Column(name = "card_money", nullable = true)
	private float cardmoney;
	
	@Column(name = "card_times", nullable = true)
	private int cardtimes;
	
	@Column(name = "balance_type", nullable = true)
	private String balancetype;
	
	@Column(name = "card_number_prefix", nullable = true)
	private String cardnumberprefix;
	
	@Column(name = "card_number_start", nullable = true)
	private String cardnumberstart;
	
	@Column(name = "card_number_end", nullable = true)
	private String cardnumberend;

	@Column(name = "cardkind_status")
	private int cardkindstatus=1;

	@Transient
	private List<CardKindProduct> products;

	public List<CardKindProduct> getProducts() {
		return products;
	}

	public void setProducts(List<CardKindProduct> products) {
		this.products = products;
	}

	public int getCardkindstatus() {
		return cardkindstatus;
	}

	public void setCardkindstatus(int cardkindstatus) {
		this.cardkindstatus = cardkindstatus;
	}

	public Long getCardkindno() {
		return cardkindno;
	}

	public void setCardkindno(Long cardkindno) {
		this.cardkindno = cardkindno;
	}

	public String getCardkindname() {
		return cardkindname;
	}

	public void setCardkindname(String cardkindname) {
		this.cardkindname = cardkindname;
	}

	public String getCardkinddesc() {
		return cardkinddesc;
	}

	public void setCardkinddesc(String cardkinddesc) {
		this.cardkinddesc = cardkinddesc;
	}

	public Date getCardkindduetime() {
		return cardkindduetime;
	}

	public void setCardkindduetime(Date cardkindduetime) {
		this.cardkindduetime = cardkindduetime;
	}

	public float getCardkindsales() {
		return cardkindsales;
	}

	public void setCardkindsales(float cardkindsales) {
		this.cardkindsales = cardkindsales;
	}

	public int getIfcalculatesales() {
		return ifcalculatesales;
	}

	public void setIfcalculatesales(int ifcalculatesales) {
		this.ifcalculatesales = ifcalculatesales;
	}

	public String getCardkinduseshop() {
		return cardkinduseshop;
	}

	public void setCardkinduseshop(String cardkinduseshop) {
		this.cardkinduseshop = cardkinduseshop;
	}

	public float getCardmoney() {
		return cardmoney;
	}

	public void setCardmoney(float cardmoney) {
		this.cardmoney = cardmoney;
	}

	public int getCardtimes() {
		return cardtimes;
	}

	public void setCardtimes(int cardtimes) {
		this.cardtimes = cardtimes;
	}

	public String getBalancetype() {
		return balancetype;
	}

	public void setBalancetype(String balancetype) {
		this.balancetype = balancetype;
	}

	public String getCardnumberprefix() {
		return cardnumberprefix;
	}

	public void setCardnumberprefix(String cardnumberprefix) {
		this.cardnumberprefix = cardnumberprefix;
	}

	public String getCardnumberstart() {
		return cardnumberstart;
	}

	public void setCardnumberstart(String cardnumberstart) {
		this.cardnumberstart = cardnumberstart;
	}

	public String getCardnumberend() {
		return cardnumberend;
	}

	public void setCardnumberend(String cardnumberend) {
		this.cardnumberend = cardnumberend;
	}

	public Float getCardkindrate() {
		return cardkindrate;
	}

	public void setCardkindrate(Float cardkindrate) {
		this.cardkindrate = cardkindrate;
	}

	public boolean isvalid(){
		if(this.cardkindstatus==0){
			return false;
		}else if(new Date().compareTo(this.cardkindduetime)>0){
			return false;
		}else{
			return true;
		}
	}
}
