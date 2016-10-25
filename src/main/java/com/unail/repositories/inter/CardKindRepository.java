package com.unail.repositories.inter;

import com.unail.repositories.entity.CardKind;
import com.unail.repositories.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardKindRepository extends JpaRepository<CardKind, Long> {
    List<CardKind> findCardkindsByCardkinduseshopLike(String shop);
}
