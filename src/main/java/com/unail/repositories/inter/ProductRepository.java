package com.unail.repositories.inter;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.unail.repositories.entity.ProductInfo;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductInfo, Long> {
	//ProductInfo save(ProductInfo entity);
	//ProductInfo update(ProductInfo entity);
    public List<ProductInfo> findProductsByProducttype1(String type1);

    public List<ProductInfo> findProductsByProducttype1AndProductnameLike(String type1,String name);

}
