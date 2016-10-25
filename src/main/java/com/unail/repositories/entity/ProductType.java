package com.unail.repositories.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="product_type")
public class ProductType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4753460911913185181L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_type_no")
	private Long producttypeno;
	
	@Column(name ="product_type_name",nullable = false)
	private String producttypename;

	public Long getProducttypeno() {
		return producttypeno;
	}

	public void setProducttypeno(Long producttypeno) {
		this.producttypeno = producttypeno;
	}

	public String getProducttypename() {
		return producttypename;
	}

	public void setProducttypename(String producttypename) {
		this.producttypename = producttypename;
	}
	
}
