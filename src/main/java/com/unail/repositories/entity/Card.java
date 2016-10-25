package com.unail.repositories.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name = "card")
public class Card implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5156560505458094654L;

	@Id
	@Column(name="card_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long cardid;

	public Long getCardid() {
		return cardid;
	}

	public void setCardid(Long cardid) {
		this.cardid = cardid;
	}

	@Column(name = "card_no")
	private String cardno;

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	@Column(name="card_seq")
	private Integer cardseq;
	//@Column(name = "card_kind_no", nullable = true)
	//private Long cardkindno;


	public Integer getCardseq() {
		return cardseq;
	}

	public void setCardseq(Integer cardseq) {
		this.cardseq = cardseq;
	}

	@ManyToOne
	@JoinColumn(name="card_kind_no")
	private CardKind cardkind;
	
	//@Column(name = "user_no", nullable = true)
	//private String userno;
	@ManyToOne
	@JoinColumn(name="user_no")
	private Custom custom;
	
	@Column(name = "surplus_sales", nullable = true)
	private Float surplussales=0f;
	
	@Column(name = "surplus_times", nullable = true)
	private Integer surplustimes=0;
	
	@Column(name = "last_consumetime", nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastconsumetime=null;
	
	@Column(name = "last_consumepro", nullable = true)
	private String lastconsumepro;
	
	@Column(name = "last_consumesales", nullable = true)
	private Float lastconsumesales=0f;
	
	@Column(name = "last_staff", nullable = true)
	private String laststaff;
	@Column(name = "card_status", nullable = true)
	private Integer cardstatus=1;
	@Column(name="card_duetime")
	private Date cardduetime;
	@Column(name="card_value")
	private Float cardvalue=0.0f;
	@Column(name="card_remainvalue")
	private Float cardremainvalue=0.0f;


	public Date getCardduetime() {
		return cardduetime;
	}

	public void setCardduetime(Date cardduetime) {
		this.cardduetime = cardduetime;
	}

	public Integer getCardstatus() {
		return cardstatus;
	}

	public void setCardstatus(Integer cardstatus) {
		this.cardstatus = cardstatus;
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

//	public Long getCardkindno() {
//		return cardkindno;
//	}

//	public void setCardkindno(Long cardkindno) {
//		this.cardkindno = cardkindno;
//	}
//
//	public String getUserno() {
//		return userno;
//	}
//
//	public void setUserno(String userno) {
//		this.userno = userno;
//	}

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

	public String getLastconsumepro() {
		return lastconsumepro;
	}

	public void setLastconsumepro(String lastconsumepro) {
		this.lastconsumepro = lastconsumepro;
	}

	public Float getLastconsumesales() {
		return lastconsumesales;
	}

	public void setLastconsumesales(Float lastconsumesales) {
		this.lastconsumesales = lastconsumesales;
	}

	public String getLaststaff() {
		return laststaff;
	}

	public void setLaststaff(String laststaff) {
		this.laststaff = laststaff;
	}

	public Float getCardvalue() {
		return cardvalue;
	}

	public void setCardvalue(Float cardvalue) {
		this.cardvalue = cardvalue;
	}

	public Float getCardremainvalue() {
		return cardremainvalue;
	}

	public void setCardremainvalue(Float cardremainvalue) {
		this.cardremainvalue = cardremainvalue;
	}

	//现金结算
	public boolean dealcash(Float cash){
		return false;
	}

	//次数结算
	public boolean dealcount(Integer count){
		return false;
	}
}
