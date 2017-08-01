package com.unail.mvc.service.staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alienlab.db.DAO;
import com.alienlab.response.SqlService;
import com.unail.repositories.entity.Staff;
import com.unail.repositories.entity.logicentity.StaffInfor;
import com.unail.repositories.inter.StaffRepository;

@Service
public class StaffService {
	@Autowired
	StaffRepository repository;

	public List<StaffInfor> getstaffs() {
		// TODO Auto-generated method stub
		String sql = "SELECT a.*,b.shop_name FROM staff a LEFT JOIN shop b ON b.shop_id=a.staff_shop ORDER BY a.staff_no";
		SqlService sqlService = new SqlService();
		List<StaffInfor> result = DAO.list2T(sqlService.selectResult(sql, null), StaffInfor.class);
		return result;
	}

	public List<Staff> getstaffs(Long shopid) {
		if(shopid>0){
			// TODO Auto-generated method stub
			List<Staff> result =repository.findStaffsByStaffshop(shopid);
			return result;
		}else{
			List<Staff> result=repository.findAll();
			return result;
		}

	}
	public boolean deletestaff(Long id) {
		// TODO Auto-generated method stub
		try {
			repository.delete(id);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Staff addStaff(Staff staff) {
		// TODO Auto-generated method stub
		try {
			staff = repository.save(staff);
			return staff;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Staff updateStaff(Staff staff) {
		// TODO Auto-generated method stub
		try{
			staff = repository.save(staff);
			return staff;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<StaffInfor> searchStaffs(String key) {
		// TODO Auto-generated method stub
	//	return repository.findStaffsByStaffnameLike("%"+key+"%");
		String sql = "SELECT a.*,b.shop_name FROM staff a LEFT JOIN shop b ON b.shop_id=a.staff_shop WHERE staff_no LIKE '%"+key+"%' OR staff_name LIKE '%"+key+"%' ORDER BY a.staff_no";
		SqlService sqlService = new SqlService();
		List<StaffInfor> result = DAO.list2T(sqlService.selectResult(sql, null), StaffInfor.class);
		return result;
	}
	
}
