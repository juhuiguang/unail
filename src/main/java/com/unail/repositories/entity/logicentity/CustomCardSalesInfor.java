package com.unail.repositories.entity.logicentity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.unail.repositories.entity.CardKind;
import com.unail.repositories.entity.Custom;

public class CustomCardSalesInfor {
	
	@Column(name = "custom_no")
	private Long customno;
	
	@Column(name = "custom_name", nullable = true)
	private String customname;
	
	@Column(name="card_id")
	private Long cardid;
	
	@Column(name = "card_no")
	private String cardno;
	
	@JoinColumn(name="card_kind_no")
	private CardKind cardkind;
	
	@JoinColumn(name="user_no")
	private Custom custom;
	
	@Column(name = "surplus_sales", nullable = true)
	private Float surplussales=0f;
	
	@Column(name = "surplus_times", nullable = true)
	private Integer surplustimes=0;
	
	@Column(name = "last_consumetime", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastconsumetime=null;
	
	@Column(name = "card_status", nullable = true)
	private Integer cardstatus=1;
	
	@Column(name = "card_kind_no")
	private Long cardkindno;

	@Column(name="card_kind_type")
	private String cardkindtype;
	
	@Column(name = "card_kind_name", nullable = true)
	private String cardkindname;
	
	@Column(name = "card_times", nullable = true)
	private int cardtimes;
	
	@Column(name = "balance_type", nullable = true)
	private String balancetype;
	
	@Column(name = "detail_no")
	private Long detailno;
	
	@Column(name = "card_id",nullable = true)
	private Long cardid2;
	
	@Column(name = "consume_time",nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date consumetime;
	
	@Column(name = "card_consume_sales",nullable = true)
	private float cardconsumesales;
	
	@Column(name = "card_consume_times",nullable = true)
	private int cardconsumetimes;
	
	@Column(name = "card_surplus_sales",nullable = true)
	private float cardsurplussales;
	
	@Column(name = "card_surplus_times",nullable = true)
	private int cardsurplustimes;
	
	@Column(name = "consume_pro",nullable = true)
	private String consumepro;
	
	@Column(name = "staff",nullable = true)
	private String staff;

	public Long getCustomno() {
		return customno;
	}

	public void setCustomno(Long customno) {
		this.customno = customno;
	}

	public String getCustomname() {
		return customname;
	}

	public void setCustomname(String customname) {
		this.customname = customname;
	}

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

	public CardKind getCardkind() {
		return cardkind;
	}

	public void setCardkind(CardKind cardkind) {
		this.cardkind = cardkind;
	}

	public Custom getCustom() {
		return custom;
	}

	public void setCustom(Custom custom) {
		this.custom = custom;
	}

	public Float getSurplussales() {
		return surplussales;
	}

	public void setSurplussales(Float surplussales) {
		this.surplussales = surplussales;
	}

	public Integer getSurplustimes() {
		return surplustimes;
	}

	public void setSurplustimes(Integer surplustimes) {
		this.surplustimes = surplustimes;
	}

	public Date getLastconsumetime() {
		return lastconsumetime;
	}

	public void setLastconsumetime(Date lastconsumetime) {
		this.lastconsumetime = lastconsumetime;
	}

	public Integer getCardstatus() {
		return cardstatus;
	}

	public void setCardstatus(Integer cardstatus) {
		this.cardstatus = cardstatus;
	}

	public Long getCardkindno() {
		return cardkindno;
	}

	public void setCardkindno(Long cardkindno) {
		this.cardkindno = cardkindno;
	}

	public String getCardkindtype() {
		return cardkindtype;
	}

	public void setCardkindtype(String cardkindtype) {
		this.cardkindtype = cardkindtype;
	}

	public String getCardkindname() {
		return cardkindname;
	}

	public void setCardkindname(String cardkindname) {
		this.cardkindname = cardkindname;
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

	public Long getDetailno() {
		return detailno;
	}

	public void setDetailno(Long detailno) {
		this.detailno = detailno;
	}

	public Long getCardid2() {
		return cardid2;
	}

	public void setCardid2(Long cardid2) {
		this.cardid2 = cardid2;
	}

	public Date getConsumetime() {
		return consumetime;
	}

	public void setConsumetime(Date consumetime) {
		this.consumetime = consumetime;
	}

	public float getCardconsumesales() {
		return cardconsumesales;
	}

	public void setCardconsumesales(float cardconsumesales) {
		this.cardconsumesales = cardconsumesales;
	}

	public int getCardconsumetimes() {
		return cardconsumetimes;
	}

	public void setCardconsumetimes(int cardconsumetimes) {
		this.cardconsumetimes = cardconsumetimes;
	}

	public float getCardsurplussales() {
		return cardsurplussales;
	}

	public void setCardsurplussales(float cardsurplussales) {
		this.cardsurplussales = cardsurplussales;
	}

	public int getCardsurplustimes() {
		return cardsurplustimes;
	}

	public void setCardsurplustimes(int cardsurplustimes) {
		this.cardsurplustimes = cardsurplustimes;
	}

	public String getConsumepro() {
		return consumepro;
	}

	public void setConsumepro(String consumepro) {
		this.consumepro = consumepro;
	}

	public String getStaff() {
		return staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}
	
}
