package com.unail.repositories.inter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unail.repositories.entity.Custom;
@Repository
public interface CustomRepository extends JpaRepository<Custom ,Long> {     
	//jparesponsitory 默认会有很多方法，如果默认方法不能满足，可以在这里定义新的方法。
	List<Custom> findCustomsByCustomphone(String phone);

	List<Custom> findCustomsByCustomnameLike(String string);
}
