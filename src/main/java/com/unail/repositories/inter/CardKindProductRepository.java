package com.unail.repositories.inter;

import com.unail.repositories.entity.CardKind;
import com.unail.repositories.entity.CardKindProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CardKindProductRepository extends JpaRepository<CardKindProduct, Long> {
    @Transactional
    void deleteByCardkindno(Long cardKindNO);//根据cardkindno批量删除
    List<CardKindProduct> findCardKindProductsByCardkindno(Long cardkindno);//根据cardkindno查找
    CardKindProduct findCardKindProductByCardkindnoAndProductno(Long cardkindno,Long productno);//根据cardkindno与productno查找
}
