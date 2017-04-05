package com.unail.mvc.controller.custom;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unail.repositories.entity.logicentity.LProductBlance;
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
import com.unail.mvc.service.custom.CustomService;
import com.unail.repositories.entity.Custom;
import com.unail.repositories.entity.ProductBalance;
import com.unail.repositories.entity.logicentity.CustomCardSalesInfor;


@RestController
public class CustomController {
	
	@Autowired
	CustomService service;   
	
	@RequestMapping(value="/custom/customs",method=RequestMethod.GET)
	public String getCustoms(HttpServletRequest request) {
		return JSON.toJSONString(service.getCustoms());
	}
	
	@RequestMapping(value="/custom/delete/{customno}",method=RequestMethod.DELETE)
	public String delCustom(@PathVariable("customno") Long id,HttpServletRequest request,HttpServletResponse response){
		try{
			if(service.deleteCustom(id)){
				return new ExecResult(true,"此客户删除成功").toString();
			}else{
				return new ExecResult(false,"此客户删除失败").toString();
			}

		}catch (Exception e){
			e.printStackTrace();
			return new ExecResult(false,"此客户删除过程中发生异常").toString();
		}

	}
	
	@RequestMapping(value="/custom/add",method=RequestMethod.POST)
	public String addCustom(HttpServletRequest request) {
		try {
			String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
			JSONObject form = JSONObject.parseObject(jsonBody);
			Custom custom = new Custom();
			custom.setCustomname(form.getString("customname"));
			custom.setCustomphone(form.getString("customphone"));     
			custom.setCustombirthday(form.getString("custombirthday"));
			custom.setFirstconsumetime(TypeUtils.str2date(form.getString("firstconsumetime"),"yyyy-MM-dd hh:mm:ss"));
			custom.setLatelyconsumetime(TypeUtils.str2date(form.getString("latelyconsumetime"),"yyyy-MM-dd hh:mm:ss"));
			custom.setCustomage(form.getIntValue("customage"));
			custom.setCustomarea(form.getString("customarea"));   
			custom.setIfvip(form.getIntValue("ifvip")); 
			custom.setVipcardno(form.getString("vipcardno"));  

			Custom result = service.addCustom(custom);
			if(result == null) {
				return new ExecResult(false,"添加客户失败").toString();
			} else {
				ExecResult er = new ExecResult();
				er.setResult(true);
				er.setData((JSON)JSON.toJSON(result));
				return er.toString();
			}
		} catch(IOException e) {
			e.printStackTrace();
			return new ExecResult(false,"添加客户出现异常").toString();
		}
	}
	
	@RequestMapping(value="/custom/customs/{key}",method=RequestMethod.GET)
	public String getSearchCus(@PathVariable("key") String key,HttpServletRequest request,HttpServletResponse response) {
		List<Custom> cus = service.getSearchCus(key);
		String result = JSON.toJSONString(cus);
		return result;
	}
	
	@RequestMapping(value="/custom/update",method=RequestMethod.POST)
	public String updateCustom(HttpServletRequest request) {
		try {
			String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
			JSONObject form = JSONObject.parseObject(jsonBody);
			Custom custom = new Custom();
			custom.setCustomno(form.getLong("customno"));
			custom.setCustomname(form.getString("customname"));
			custom.setCustomphone(form.getString("customphone"));    
			custom.setCustombirthday(form.getString("custombirthday"));
			custom.setFirstconsumetime(TypeUtils.str2date(form.getString("firstconsumetime"),"yyyy-MM-dd hh:mm:ss"));
			custom.setLatelyconsumetime(TypeUtils.str2date(form.getString("latelyconsumetime"),"yyyy-MM-dd hh:mm:ss"));
			custom.setCustomage(form.getIntValue("customage"));
			custom.setCustomarea(form.getString("customarea")); 
			custom.setIfvip(form.getIntValue("ifvip"));     
			custom.setVipcardno(form.getString("vipcardno"));     
			
			Custom result = service.updateCustom(custom);
			if(result == null) {
				return new ExecResult(false,"更新客户信息失败").toString();
			} else {
				ExecResult er = new ExecResult();
				er.setResult(true);
				er.setData((JSON)JSON.toJSON(result));
				return er.toString();
			}
		} catch(IOException e) {
			e.printStackTrace();
			return new ExecResult(false,"更新客户信息出现异常").toString();
		}
	}
	
	@RequestMapping(value="/custom/query/{key}",method=RequestMethod.GET)
	public String getQueryCus(@PathVariable("key") String key,HttpServletRequest request,HttpServletResponse response) {
		List<Custom> cus = service.getQueryCus(key);
		String result = JSON.toJSONString(cus);
		return result;
	}
	
	@RequestMapping(value="/custom/selectCusSalesInfor/{key}",method=RequestMethod.GET)
	public String selectCusSalesInfor(@PathVariable("key") String key,HttpServletRequest request,HttpServletResponse response) {
		List<CustomCardSalesInfor> cusSalesInfor = service.getCusSalesInfor(key);
		String result = JSON.toJSONString(cusSalesInfor);
		return result;
	}
	
	@RequestMapping(value="/custom/selectCusMoneyInfor/{key}",method=RequestMethod.GET)
	public String selectCusMoneyInfor(@PathVariable("key") String key,HttpServletRequest request,HttpServletResponse response) {
		List<LProductBlance> cusMoneyInfor = service.getCusMoneyInfor(key);
		String result = JSON.toJSONString(cusMoneyInfor);
		return result;
	}
	
	@RequestMapping(value="/custom/selectDetails/{key}",method=RequestMethod.GET)
	public String selectDetails(@PathVariable("key") String key,HttpServletRequest request,HttpServletResponse response) {
		List<CustomCardSalesInfor> cusDetails = service.getCusDetails(key);
		String result = JSON.toJSONString(cusDetails);
		return result;
	}
}
