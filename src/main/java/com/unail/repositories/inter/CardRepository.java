package com.unail.repositories.inter;

import com.unail.repositories.entity.Card;
import com.unail.repositories.entity.CardKind;
import com.unail.repositories.entity.Custom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CardRepository extends PagingAndSortingRepository<Card, Long> {
    Page<Card> findCardsByCardkind(CardKind cardkind, Pageable pageRequest);
    @Query("select a from Card a where a.cardkind=?1 and a.cardno like CONCAT('%',?2,'%')")
    Page<Card> findCardsByQuery(CardKind cardkind,String keyword ,Pageable page);
    Card findFirstCardByCardkindOrderByCardseqDesc(CardKind cardKind);
    //查询创建卡片范围内有无已存在卡片
    List<Card> findCardsByCardkindAndCardseqBetween(CardKind cardkind,int min,int max);
    List<Card> findCardsByCustomAndCardstatusAndCardduetimeGreaterThan (Custom custom,int cardstatus,Date date);
    Card findCardByCardno(String no);
}
