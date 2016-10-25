package com.unail.repositories.entity;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name = "card_kind_product")
public class CardKindProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4079061606262407109L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "card_kind_no", nullable = true)
	private Long cardkindno;
	
	@Column(name = "product_no", nullable = true)
	private Long productno;
	
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
