package com.unail.mvc.service.arrange;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alienlab.db.DAO;
import com.alienlab.response.SqlService;
import com.unail.repositories.entity.Arrange;
import com.unail.repositories.inter.ArrangeRepository;

@Service
public class ArrangeService {
	
	@Autowired
	ArrangeRepository repository;
	
	public List<Arrange> getRangeTimeCus(String startDate, String endDate) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM arrange WHERE if_finish='0' AND LEFT(arrange_time,10)>='"+startDate+"' AND LEFT(arrange_time,10)<='"+endDate+"' ORDER BY arrange_time DESC";
		SqlService sqlService = new SqlService();
		List<Arrange> result = DAO.list2T(sqlService.selectResult(sql, null), Arrange.class);
		return result;
	}

	public Arrange addArrange(Arrange arrange) {
		// TODO Auto-generated method stub
		try {
			arrange = repository.save(arrange);
			return arrange;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public boolean deleteArrange(Long id) {
		// TODO Auto-generated method stub
		try {
			repository.delete(id);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
}
