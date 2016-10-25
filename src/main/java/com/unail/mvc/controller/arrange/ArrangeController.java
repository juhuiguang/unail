package com.unail.mvc.controller.arrange;

import java.io.IOException;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.common.TypeUtils;
import com.alienlab.db.ExecResult;
import com.unail.mvc.service.arrange.ArrangeService;
import com.unail.repositories.entity.Arrange;

@RestController
public class ArrangeController {
	
	@Autowired
	ArrangeService service;
	
	@RequestMapping(value="/arrange/arranges")
	public String getRangeTimeCus(HttpServletRequest request) {
		try {
			String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject param = JSONObject.parseObject(jsonBody);

            String startDate = param.getString("startT");
            String endDate = param.getString("endT");
			List<Arrange> arranges = service.getRangeTimeCus(startDate,endDate);
			ExecResult er = new ExecResult();
            er.setResult(true);
            er.setData((JSON) JSON.toJSON(arranges));
            return er.toString();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ExecResult(false, "获取所选时间周的客户预约信息发生异常.").toString();
        }
	}
	
	@RequestMapping(value="/arrange/add")
	public String addArrange(HttpServletRequest request) {
		try {
			String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
			JSONObject form = JSONObject.parseObject(jsonBody);
			
			Arrange arrange = new Arrange();
			arrange.setCustomname(form.getString("customname"));
			arrange.setCustomphone(form.getString("customphone"));
			arrange.setCustomtype(form.getString("customtype"));
			arrange.setArrangetime(TypeUtils.str2date(form.getString("datestr"), "yyyy-MM-dd hh:mm"));
			String products = "";
			JSONArray productarray = form.getJSONArray("products");
			for(int i=0; i<productarray.size(); i++) {
				JSONObject productitem = productarray.getJSONObject(i);
				if(productitem.containsKey("productno") && productitem.getLong("productno") >0) {
					if(products.equals("")) {
						products += productitem.getString("productno");
					} else {
						products += "," + productitem.getString("productno");
					}
				}
			}
			arrange.setArrangeproduct(products);
			JSONObject arrshop=form.getJSONObject("arrangeshop");
			if(arrshop!=null){
				arrange.setArrangeshop(arrshop.getLong("shopNo"));
			}
			
			Arrange result = service.addArrange(arrange);
			if(result == null) {
				return new ExecResult(false,"预约信息添加失败").toString();
			} else {
				ExecResult er = new ExecResult();
				er.setResult(true);
				er.setData((JSON)JSON.toJSON(result));
				return er.toString();
			}
			
		} catch(IOException e) {
			e.printStackTrace();
			return new ExecResult(false,"预约数据添加发生异常").toString();
		}
	}
	
	@RequestMapping(value="/arrange/delete/{arrangeno}",method=RequestMethod.DELETE)
	public String delArrange(@PathVariable("arrangeno") Long id,HttpServletRequest request,HttpServletResponse response) {
		try {
			if(service.deleteArrange(id)) {
				return new ExecResult(true,"此客户预约数据删除成功").toString();
			} else {
				return new ExecResult(false,"此客户预约数据删除失败").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ExecResult(false,"此客户预约数据删除过程中发生异常").toString();
		}
	}
}
