package com.unail.mvc.service.custom;

import java.util.List;

import com.unail.repositories.entity.logicentity.LProductBlance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alienlab.db.DAO;
import com.alienlab.response.SqlService;
import com.unail.repositories.entity.Custom;
import com.unail.repositories.entity.ProductBalance;
import com.unail.repositories.entity.ProductInfo;
import com.unail.repositories.entity.logicentity.CustomCardSalesInfor;
import com.unail.repositories.inter.CustomRepository;

import sun.launcher.resources.launcher;

@Service
public class CustomService {

	@Autowired
	CustomRepository repository;
	
	//取客户所有数据
	public List<Custom> getCustoms() {
		return repository.findAll();
	}
	
	//例子（学长举的栗子） 
	public List<Custom> getCustomsByPhone(String phone){
		return repository.findCustomsByCustomphone(phone);
	}

	//删除某一客户数据
	public boolean deleteCustom(Long id) {
		// TODO Auto-generated method stub
		try {
			repository.delete(id);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
	}

	public Custom addCustom(Custom custom) {
		// TODO Auto-generated method stub
		try {
			custom = repository.save(custom);
			return custom;
		} catch(Exception e) {
			e.printStackTrace();
			return null;   
		}   
		
	}

	public List<Custom> getSearchCus(String key) {
		// TODO Auto-generated method stub
		return repository.findCustomsByCustomnameLike("%"+key+"%");
	}

	public Custom updateCustom(Custom custom) {
		// TODO Auto-generated method stub
		try{
			custom = repository.save(custom);
			return custom;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Custom> getQueryCus(String key) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.* FROM custom a WHERE a.custom_name LIKE '%"+key+"%' OR a.custom_phone LIKE '%"+key+"%' OR a.vip_cardno LIKE '%"+key+"%' GROUP BY a.custom_no";
		SqlService s=new SqlService();
		List list=s.selectResult(sql,null);
        return DAO.list2T(list,Custom.class);
	}

	public List<CustomCardSalesInfor> getCusSalesInfor(String key) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.custom_name,b.card_no,b.card_id,b.surplus_sales,b.surplus_times,c.card_kind_type,c.card_kind_name,c.balance_type FROM custom a LEFT JOIN card b ON b.user_no=a.custom_no LEFT JOIN card_kind c ON b.card_kind_no=c.card_kind_no WHERE a.custom_no='"+key+"' order by b.card_id desc";
		SqlService s=new SqlService();
		List list=s.selectResult(sql,null);
		return DAO.list2T(list,CustomCardSalesInfor.class);
	}
	
	public List<LProductBlance> getCusMoneyInfor(String key) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.*,b.`product_type1`,c.`staff_name` FROM product_balance a " +
						"LEFT JOIN product_info b ON a.`consume_pro_no`=b.`product_no` " +
						"LEFT JOIN staff c ON c.`staff_no`=a.`staff` " +
						"WHERE  custom_no='"+key+"' ORDER BY consume_time DESC ";
		SqlService s=new SqlService();
		List list=s.selectResult(sql,null);
		return DAO.list2T(list,LProductBlance.class);
	}

	public List<CustomCardSalesInfor> getCusDetails(String key) {
		// TODO Auto-generated method stub
		String sql = "SELECT b.balance_type,c.* FROM card a LEFT JOIN card_kind b ON a.card_kind_no=b.card_kind_no LEFT JOIN card_use_detail c ON c.card_id=a.card_id WHERE a.card_id='"+key+"'";
		SqlService s=new SqlService();
		List list=s.selectResult(sql,null);
		return DAO.list2T(list,CustomCardSalesInfor.class);
	}

	
}
