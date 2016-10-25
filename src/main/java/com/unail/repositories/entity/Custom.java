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
@Table(name = "custom")
public class Custom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -629791863661641210L;
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)    
	@Column(name = "custom_no")
	private Long customno;
	
	@Column(name = "custom_name", nullable = true)
	private String customname;
	
	@Column(name = "custom_phone", nullable = true)
	private String customphone;
	
	@Column(name = "custom_birthday", nullable = true)
	private String custombirthday;
	
	@Column(name = "if_vip", nullable = true, columnDefinition = "0")
	private int ifvip ;
	
	@Column(name = "vip_cardno",nullable = true)
	private String vipcardno;
	
	@Column(name = "first_consumetime", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date firstconsumetime;
	
	@Column(name = "lately_consumetime", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date latelyconsumetime;
	
	@Column(name = "custom_age", nullable = true)
	private int customage;
	
	@Column(name = "custom_area", nullable = true)
	private String customarea;

	@Column(name = "wechat_nickname", nullable = true)
	private String wechatnickname;
	
	@Column(name = "wechat_head", nullable = true)
	private String wechathead;
	
	@Column(name = "wechat", nullable = true)
	private String wechat;

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

	public String getCustomphone() {
		return customphone;
	}

	public void setCustomphone(String customphone) {
		this.customphone = customphone;
	}

	public String getCustombirthday() {
		return custombirthday;
	}

	public void setCustombirthday(String custombirthday) {
		this.custombirthday = custombirthday;
	}

	public int getIfvip() {
		return ifvip;
	}

	public void setIfvip(int ifvip) {
		this.ifvip = ifvip;
	}

	public String getVipcardno() {
		return vipcardno;
	}

	public void setVipcardno(String vipcardno) {
		this.vipcardno = vipcardno;
	}

	public Date getFirstconsumetime() {
		return firstconsumetime;
	}

	public void setFirstconsumetime(Date firstconsumetime) {
		this.firstconsumetime = firstconsumetime;
	}

	public Date getLatelyconsumetime() {
		return latelyconsumetime;
	}

	public void setLatelyconsumetime(Date latelyconsumetime) {
		this.latelyconsumetime = latelyconsumetime;
	}

	public int getCustomage() {
		return customage;
	}

	public void setCustomage(int customage) {
		this.customage = customage;
	}

	public String getCustomarea() {
		return customarea;
	}

	public void setCustomarea(String customarea) {
		this.customarea = customarea;
	}

	public String getWechatnickname() {
		return wechatnickname;
	}

	public void setWechatnickname(String wechatnickname) {
		this.wechatnickname = wechatnickname;
	}

	public String getWechathead() {
		return wechathead;
	}

	public void setWechathead(String wechathead) {
		this.wechathead = wechathead;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	
	
}
