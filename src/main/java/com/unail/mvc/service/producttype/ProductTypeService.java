package com.unail.mvc.service.producttype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unail.repositories.entity.ProductType;
import com.unail.repositories.inter.ProductTypeRepository;

@Service
public class ProductTypeService {
	@Autowired
	ProductTypeRepository repository;

	public ProductType addProductType(ProductType producttype) {
		// TODO Auto-generated method stub
		try {
			producttype = repository.save(producttype);
			return producttype;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Object getProductType() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	public boolean delProductType(Long id) {
		// TODO Auto-generated method stub
		try {
			repository.delete(id);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
