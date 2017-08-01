package com.unail.mvc.controller.user;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alienlab.utils.Azdg;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.sql.ast.SQLDeclareItem.Type;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.common.TypeUtils;
import com.alienlab.db.ExecResult;
import com.alienlab.system.repositories.entity.User;
import com.alienlab.system.service.RoleService;
import com.unail.mvc.service.user.UserService;

@RestController
public class UserController {
	@Autowired
	UserService service;
	@Autowired
	RoleService roleservice;
	
	@RequestMapping(value="/user/users",method=RequestMethod.GET)
	public String getusers(HttpServletRequest request) {
		return JSON.toJSONString(service.getusers());
	}
	
	@RequestMapping(value="/user/roles",method=RequestMethod.GET)
	public String getroles(HttpServletRequest request) {
		return JSON.toJSONString(service.getroles());
	}
	
	@RequestMapping(value="/user/delete/{userid}",method=RequestMethod.DELETE)
	public String deleteuser(@PathVariable("userid") Long id,HttpServletRequest request,HttpServletResponse response) {
		try {
			if(service.deleteuser(id)) {
				return new ExecResult(true,"此用户删除成功").toString();
			} else {
				return new ExecResult(false,"此用户删除失败").toString();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ExecResult(false,"此用户删除过程中发生异常").toString();
		}
	}
	
	@RequestMapping(value="/user/users/{key}",method=RequestMethod.GET)
	public String searchUsers(@PathVariable("key") String key,HttpServletRequest request,HttpServletResponse response) {
		return JSON.toJSONString(service.searchUsers(key));
	}
	
	@RequestMapping(value="/user/add",method=RequestMethod.POST)
	public String addUser(HttpServletRequest request) {
		try {
			String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
			JSONObject form = JSONObject.parseObject(jsonBody);
			User user = new User();
			user.setLoginname(form.getString("loginname"));
			String pwd=form.getString("password");
			Azdg a=new Azdg();
			String password= a.encrypt(pwd);
			user.setPassword(password);
			user.setUsername(form.getString("username"));
			Date now = new Date();
			user.setCreatetime(now);
			user.setLastlogin(now);
			user.setPurview(form.getString("purview"));
			user.setStatus("1");
			
			User result = service.addUser(user);
			if(result == null) {
				return new ExecResult(false,"添加用户失败").toString();
			} else {
				ExecResult er = new ExecResult();
				er.setResult(true);
				er.setData((JSON)JSON.toJSON(result));
				return er.toString();
			}
		} catch(IOException e) {
			e.printStackTrace();
			return new ExecResult(false,"添加用户出现异常").toString();
		}
	}
	
	@RequestMapping(value="/user/update",method=RequestMethod.POST)
	public String updateUser(HttpServletRequest request) {
		try {
			String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
			JSONObject form = JSONObject.parseObject(jsonBody);
			User user = new User();
			user.setUserid(form.getLong("userid"));
			user.setLoginname(form.getString("loginname"));
			String pwd=form.getString("password");
			Azdg a=new Azdg();
			String password= a.encrypt(pwd);
			user.setPassword(password);
			user.setUsername(form.getString("username"));
			user.setCreatetime(TypeUtils.str2date(form.getString("createtime"), "yyyy-MM-dd hh:mm:ss"));
			user.setLastlogin(TypeUtils.str2date(form.getString("lastlogin"), "yyyy-MM-dd hh:mm:ss"));
			user.setPurview(form.getString("purview"));
			user.setStatus(form.getString("status"));
			
			User result = service.updateUser(user);
			if(result == null) {
				return new ExecResult(false,"修改用户失败").toString();
			} else {
				ExecResult er = new ExecResult();
				er.setResult(true);
				er.setData((JSON)JSON.toJSON(result));
				return er.toString();
			}
			
		} catch(IOException e) {
			e.printStackTrace();
			return new ExecResult(false,"修改用户出现异常").toString();
		}
	}
	
	
	
}
