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
@Table(name = "staff")
public class Staff implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4160473555055434501L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="staff_id")
	private Long staffid;
	
	@Column(name = "staff_no",nullable = true)
	private String staffno;
	
	@Column(name = "staff_name",nullable = true)
	private String staffname;
	
	@Column(name = "staff_phone",nullable = true)
	private String staffphone;
	
	@Column(name = "staff_age",nullable = true)
	private int staffage;
	
	@Column(name = "staff_shop",nullable = true)
	private Long staffshop;
	
	@Column(name = "staff_entrytime",nullable = true, columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date staffentrytime;
	
	@Column(name = "staff_state",nullable = true, columnDefinition = "0")
	private int staffstate;

	public Long getStaffid() {
		return staffid;
	}

	public void setStaffid(Long staffid) {
		this.staffid = staffid;
	}

	public String getStaffno() {
		return staffno;
	}

	public void setStaffno(String staffno) {
		this.staffno = staffno;
	}

	public String getStaffname() {
		return staffname;
	}

	public void setStaffname(String staffname) {
		this.staffname = staffname;
	}

	public String getStaffphone() {
		return staffphone;
	}

	public void setStaffphone(String staffphone) {
		this.staffphone = staffphone;
	}
	
	public int getStaffage() {
		return staffage;
	}

	public void setStaffage(int staffage) {
		this.staffage = staffage;
	}

	public Long getStaffshop() {
		return staffshop;
	}

	public void setStaffshop(Long staffshop) {
		this.staffshop = staffshop;
	}

	public Date getStaffentrytime() {
		return staffentrytime;
	}

	public void setStaffentrytime(Date staffentrytime) {
		this.staffentrytime = staffentrytime;
	}

	public int getStaffstate() {
		return staffstate;
	}

	public void setStaffstate(int staffstate) {
		this.staffstate = staffstate;
	}
	
	
}
