package com.unail.repositories.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.mysql.fabric.xmlrpc.base.Data;

@Entity
@Table(name = "card_use_detail")
public class CardUseDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6090437666205972195L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "detail_no")
	private Long detailno;
	
	@Column(name = "card_id",nullable = true)
	private Long cardno;
	
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

	public Long getDetailno() {
		return detailno;
	}

	public void setDetailno(Long detailno) {
		this.detailno = detailno;
	}

	public Long getCardno() {
		return cardno;
	}

	public void setCardno(Long cardno) {
		this.cardno = cardno;
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
