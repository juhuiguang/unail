package com.unail.repositories.inter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unail.repositories.entity.Shop;

/**
 * Created by 橘 on 2016/6/20.
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {    

	List<Shop> findShopsByShopNameLike(String string);

}
