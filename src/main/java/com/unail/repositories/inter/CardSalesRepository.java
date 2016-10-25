package com.unail.repositories.inter;

import com.unail.repositories.entity.CardSalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 橘 on 2016/8/20.
 */
@Repository
public interface CardSalesRepository extends JpaRepository<CardSalesRecord,Long> {
}
