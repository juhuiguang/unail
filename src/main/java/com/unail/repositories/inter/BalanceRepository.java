package com.unail.repositories.inter;

import com.unail.repositories.entity.ProductBalance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 橘 on 2016/7/30.
 */
public interface BalanceRepository extends JpaRepository<ProductBalance,Long> {}
