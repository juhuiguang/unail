package com.unail.mvc.controller.producttype;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.db.ExecResult;
import com.unail.mvc.service.producttype.ProductTypeService;
import com.unail.repositories.entity.ProductType;

@RestController
public class ProductTypeController {
	@Autowired
	ProductTypeService service;
	
	@RequestMapping(value="/producttype/producttypes")
	public String getProductType(HttpServletRequest request){
		return JSON.toJSONString(service.getProductType());
	}
	
	@RequestMapping(value="/producttype/addproducttype",method=RequestMethod.POST)
	public String addProductType(HttpServletRequest request) {
		try{
			String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
			JSONObject form= JSONObject.parseObject(jsonBody);
			ProductType producttype = new ProductType();
			producttype.setProducttypename(form.getString("ptypename"));   
			ProductType result = service.addProductType(producttype);
			
			if(result == null) {
    			return new ExecResult(false,"添加产品类型失败").toString();
    		} else {
    			ExecResult er = new ExecResult();
    			er.setResult(true);
    			er.setData((JSON)JSON.toJSON(result));
    			return er.toString();
    		}
    	} catch(IOException e) {
    		e.printStackTrace();
    		return new ExecResult(false,"添加产品类型出现异常").toString();
    	}
	}
	
	@RequestMapping(value="/producttype/delete/{ptypeno}")
	public String delProductType(@PathVariable("ptypeno") Long id,HttpServletRequest request,HttpServletResponse response) {
		try{
			if(service.delProductType(id)) {
    			return new ExecResult(true,"此产品类型删除成功").toString();
			} else {
    			return new ExecResult(true,"此产品类型删除失败").toString();
			}
		} catch(Exception e) {
			e.printStackTrace();
    		return new ExecResult(false,"此产品类型删除过程中发生异常").toString();
		}
	}
}
