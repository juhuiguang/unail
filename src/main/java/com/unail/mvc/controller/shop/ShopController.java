package com.unail.mvc.controller.shop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.common.TypeUtils;
import com.alienlab.db.ExecResult;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.unail.mvc.service.shop.ShopService;
import com.unail.repositories.entity.Shop;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 橘 on 2016/6/20.
 */
@RestController
public class ShopController {
    @Autowired
    ShopService service ;
    @RequestMapping(value="/shop/shops")
    public String getShops(HttpServletRequest request){
        return JSON.toJSONString(service.getShops());
    }
    @RequestMapping(value="/shop/shops/{shopid}")
    public String getShop(@PathVariable("shopid") String shopid){
        return JSON.toJSONString(service.getShop(Long.valueOf(shopid)));
    }
    
    @RequestMapping(value="/shop/delete/{shopid}")
    public String delShop(@PathVariable("shopid") Long id,HttpServletRequest request,HttpServletResponse response) {
    	try {
    		if(service.deleteShop(id)) {
    			return new ExecResult(true,"此门店删除成功").toString();
    		} else {
    			return new ExecResult(false,"此门店删除失败").toString();
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    		return new ExecResult(false,"此门店删除过程中发生异常").toString();
    	}
    }
    
    @RequestMapping(value="/shop/add")
    public String addShop(HttpServletRequest request,HttpServletResponse response) {
    	try {
    		String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
    		JSONObject form = JSONObject.parseObject(jsonBody);
    		Shop shop = new Shop();
    		shop.setShopName(form.getString("shopName"));
    		shop.setShopAddr(form.getString("shopAddr"));
    		shop.setShopcode(form.getString("shopcode"));
    		Date now = new Date(); 
    		shop.setShoptime(now);    
    		shop.setShopPhone1(form.getString("shopPhone1"));  
    		shop.setShopPhone2(form.getString("shopPhone2"));   
    		
    		Shop result = service.addShop(shop);
    		if(result == null) {
    			return new ExecResult(false,"添加门店失败").toString();
    		} else {
    			ExecResult er = new ExecResult();
    			er.setResult(true);
    			er.setData((JSON)JSON.toJSON(result));
    			return er.toString();
    		}
    	} catch(IOException e) {
    		e.printStackTrace();
    		return new ExecResult(false,"添加门店出现异常").toString();
    	}
    }
    
    @RequestMapping(value="/shop/shops/{key}",method=RequestMethod.GET)
    public String getSearchShop(@PathVariable("key") String key,HttpServletRequest request,HttpServletResponse response) {
    	List<Shop> shop = service.getSearchShop(key);
    	String result = JSON.toJSONString(shop);
    	return result;    
    }
    
    @RequestMapping(value="/shop/update")
    public String updateShop(HttpServletRequest request) {
    	try{
    		String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
    		JSONObject form = JSONObject.parseObject(jsonBody);
    		Shop shop = new Shop();
    		shop.setShopNo(form.getLong("shopNo"));
    		shop.setShopName(form.getString("shopName"));
    		shop.setShopAddr(form.getString("shopAddr")); 
    		shop.setShopcode(form.getString("shopcode"));
       		shop.setShoptime(TypeUtils.str2date(form.getString("shoptime").split("T")[0], "yyyy-MM-dd"));  		
    		shop.setShopPhone1(form.getString("shopPhone1"));  
    		shop.setShopPhone2(form.getString("shopPhone2"));
    		
    		Shop result = service.updateShop(shop);
    		if(result == null) {
    			return new ExecResult(false,"更新门店信息失败").toString();
    		}else {
    			ExecResult er = new ExecResult();
    			er.setResult(true);
    			er.setData((JSON)JSON.toJSON(result));
    			return er.toString();
    		}
    	} catch(IOException e) {
    		e.printStackTrace();
    		return new ExecResult(false,"更新门店信息出现异常").toString();
    	}
    }
    
}
