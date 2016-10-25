package com.unail.repositories.inter;

import com.unail.repositories.entity.CardUseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by 橘 on 2016/7/31.
 */
public interface CardDetailRepository extends JpaRepository<CardUseDetail,Long> {
    List<CardUseDetail> findCardUserDetailsByConsumepro(String productname);
    List<CardUseDetail> findCardUserDetailsByConsumeproAndConsumetimeBetween(String productname, Date start, Date end);
}
