package com.unail.mvc.controller.staff;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.common.TypeUtils;
import com.alienlab.db.ExecResult;
import com.unail.mvc.service.staff.StaffService;
import com.unail.repositories.entity.Staff;


@RestController
public class StaffController {
	@Autowired
	StaffService service;
	
	@RequestMapping(value="/staff/shopstaffs/{shop}",method=RequestMethod.GET)
	public String  getstaffs(@PathVariable String shop) {
		if(shop.equalsIgnoreCase("all")){
			return JSON.toJSONString(service.getstaffs(0L));
		}else{
			return JSON.toJSONString(service.getstaffs(Long.parseLong(shop)));
		}

	}
	
	@RequestMapping(value="/staff/delete/{staffid}",method=RequestMethod.DELETE)
	public String deletestaff(@PathVariable("staffid") Long id,HttpServletRequest request,HttpServletResponse response) {
		try {
			if(service.deletestaff(id)) {
				return new ExecResult(true,"此员工删除成功").toString();
			} else {
				return new ExecResult(false,"此员工删除失败").toString();
			}
		} catch(Exception e) {
			e.printStackTrace();
			return new ExecResult(false,"此员工删除过程中发生异常").toString();
		}
	}
	
	@RequestMapping(value="/staff/add",method=RequestMethod.POST)
	public String addStaff(HttpServletRequest request) {
		try {
			String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
			JSONObject form = JSONObject.parseObject(jsonBody);
			Staff staff = new Staff();
			staff.setStaffno(form.getString("staffno"));
			staff.setStaffname(form.getString("staffname"));
			staff.setStaffage(form.getIntValue("staffage")); 
			staff.setStaffphone(form.getString("staffphone"));
			JSONObject arrshop=form.getJSONObject("staffshop");
			if(arrshop!=null){
				staff.setStaffshop(arrshop.getLong("shopNo"));
			}
			Date now = new Date();
    		staff.setStaffentrytime(now);
    		
			Staff result = service.addStaff(staff);
			if(result == null) {
				return new ExecResult(false,"添加员工失败").toString();
			} else {
				ExecResult er = new ExecResult();
				er.setResult(true);
				er.setData((JSON)JSON.toJSON(result));
				return er.toString();
			}
		} catch(IOException e) {
			e.printStackTrace();
			return new ExecResult(false,"添加员工出现异常").toString();
		}
	}
	
	@RequestMapping(value="/staff/update",method=RequestMethod.POST)
	public String updateStaff(HttpServletRequest request) {
		try {
			String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
			JSONObject form = JSONObject.parseObject(jsonBody);
			Staff staff = new Staff();
			staff.setStaffid(form.getLong("staffid"));
			staff.setStaffno(form.getString("staffno"));
			staff.setStaffname(form.getString("staffname"));
			staff.setStaffage(form.getIntValue("staffage"));
			staff.setStaffphone(form.getString("staffphone"));
			JSONObject arrshop=form.getJSONObject("staffshop");
			if(arrshop!=null){
				staff.setStaffshop(arrshop.getLong("shopNo"));
			}
			staff.setStaffentrytime(TypeUtils.str2date(form.getString("staffentrytime"), "yyyy-MM-dd hh:mm:ss"));
			staff.setStaffstate(form.getIntValue("staffstate"));

			Staff result = service.updateStaff(staff);
			if(result == null) {
				return new ExecResult(false,"更新员工失败").toString();
			} else {
				ExecResult er = new ExecResult();
				er.setResult(true);
				er.setData((JSON)JSON.toJSON(result));
				return er.toString();
			}
		} catch(IOException e) {
			e.printStackTrace();
			return new ExecResult(false,"更新员工出现异常").toString();
		}
	}
	
	@RequestMapping(value="/staff/staffs/{key}",method=RequestMethod.GET)
	public String searchStaffs(@PathVariable("key") String key,HttpServletRequest request,HttpServletResponse response) {
		return JSON.toJSONString(service.searchStaffs(key));
	}
	
}
