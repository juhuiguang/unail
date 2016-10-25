package com.unail.repositories.inter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unail.repositories.entity.Arrange;

@Repository
public interface ArrangeRepository extends JpaRepository<Arrange, Long> {

}
