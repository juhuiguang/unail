package com.unail.mvc.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alienlab.db.DAO;
import com.alienlab.response.SqlService;
import com.alienlab.system.repositories.entity.Role;
import com.alienlab.system.repositories.entity.User;
import com.alienlab.system.repositories.inter.RoleRepository;
import com.alienlab.system.repositories.inter.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository repository;
	@Autowired
	RoleRepository roleRepository;

	public List<User> getusers() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}
	
	public List<Role> getroles() {
		return roleRepository.findAll();
	}

	public boolean deleteuser(Long id) {
		// TODO Auto-generated method stub
		try {
			repository.delete(id);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Object searchUsers(String key) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM tb_user WHERE user_loginname LIKE '%"+key+"%' OR user_name LIKE '%"+key+"%'";
		SqlService sqlService = new SqlService();
		List<User> result = DAO.list2T(sqlService.selectResult(sql, null), User.class);
		return result;   
	}

	public User addUser(User user) {
		// TODO Auto-generated method stub
		try {
			user = repository.save(user);
			return user;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public User updateUser(User user) {
		// TODO Auto-generated method stub
		try{
			user = repository.save(user);
			return user;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}
