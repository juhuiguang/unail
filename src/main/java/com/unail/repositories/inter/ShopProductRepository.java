package com.unail.repositories.inter;

import com.unail.repositories.entity.Shop;
import com.unail.repositories.entity.ShopProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by 橘 on 2016/6/20.
 */
public interface ShopProductRepository extends JpaRepository<ShopProduct, Long> {
    @Transactional
    void deleteByProductno(Long productno);
    List<ShopProduct> findByProductno(Long productno);
}
