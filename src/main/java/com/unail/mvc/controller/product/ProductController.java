package com.unail.mvc.controller.product;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.common.ChineseLetter;
import com.alienlab.db.ExecResult;
import com.unail.mvc.service.shop.ShopService;
import com.unail.repositories.entity.Shop;
import com.unail.repositories.entity.ShopProduct;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;

import com.unail.mvc.service.product.ProductService;
import com.unail.repositories.entity.ProductInfo;

@RestController
public class ProductController {
	@Autowired
	ProductService ps ;
	@Autowired
	ShopService ss;
	@RequestMapping(value="/product/products")
	public String getProducts(HttpServletRequest request){
		JSONObject req= new JSONObject();
		try {
			String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
			req=JSONObject.parseObject(jsonBody);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String special=req.getString("special");
		List<ProductInfo> productInfos=ps.getAllProducts(special);
		if(productInfos!=null){
			ExecResult er=new ExecResult();
			er.setResult(true);
			er.setData((JSON)JSON.toJSON(productInfos));
			return er.toString();
		}else{
			return new ExecResult(false,"获得产品数据失败").toString();
		}

	}

	@RequestMapping(value="/product/add",method= RequestMethod.POST)
	public String addProduct(HttpServletRequest request){
		try {
			String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
			JSONObject form= JSONObject.parseObject(jsonBody);
			ProductInfo product=new ProductInfo();
			product.setProductcount(form.getString("count"));
			product.setProductdecs(form.getString("desc"));
			product.setProductname(form.getString("productname"));
//			product.setProductdecs(form.getString("productdesc"));
			ChineseLetter cte = new ChineseLetter();
			product.setProductletter(cte.getAllFirstLetter(form.getString("productname")));
			product.setProducttype1(form.getString("producttype"));
			product.setProductprice1(form.getFloat("price1"));
			product.setProductprice2(form.getFloat("price2"));
			//product.setProductcount(form.getString("productcount"));
			product.setProductunit(form.getString("unit"));

			ProductInfo result=ps.addProduct(product);
			if(result==null){
				return new ExecResult(false,"添加产品失败").toString();
			}else{
				JSONArray shopsarry=form.getJSONArray("shops");
				ShopProduct [] shops=null;
				if(shopsarry.size()>0){//如果有指定的门店价格。
					shops=new ShopProduct[shopsarry.size()];
					for(int i=0;i<shopsarry.size();i++){
						JSONObject jo=shopsarry.getJSONObject(i);
						shops[i]=new ShopProduct();
						shops[i].setProductno(result.getProductno());
						shops[i].setShopno(jo.getLong("shopNo"));
						shops[i].setProductprice1(jo.getFloat("price1"));
						shops[i].setProductprice2(jo.getFloat("price2"));
					}
				}else{//没有指定的门店，获取当前用户权限对应的门店
					if(form.containsKey("specialshop")&&!form.getString("specialshop").equalsIgnoreCase("ALL")){//如果是某个门店的店长
						shops=new ShopProduct[1];
						Shop s=ss.getShop(form.getLong("specialshop"));
						shops[0]=new ShopProduct();
						shops[0].setProductno(result.getProductno());
						shops[0].setShopno(s.getShopNo());
						shops[0].setProductprice1(form.getFloat("price1"));
						shops[0].setProductprice2(form.getFloat("price2"));
						product.setProductcount(form.getString("productcount"));
						product.setProductunit(form.getString("productunit"));

					}else{//如果是ALL权限
						List<Shop> shopslist=ss.getShops();
						shops=new ShopProduct[shopslist.size()];
						for(int i=0;i<shopslist.size();i++){
							shops[i]=new ShopProduct();
							shops[i].setProductno(result.getProductno());
							shops[i].setShopno(shopslist.get(i).getShopNo());
							shops[i].setProductprice1(form.getFloat("price1"));
							shops[i].setProductprice2(form.getFloat("price2"));
						}
					}
				}

				if(ps.saveShops(result.getProductno(),shops)){
					ExecResult er=new ExecResult();
					er.setResult(true);
					er.setData((JSON)JSON.toJSON(result));
					return er.toString();
				}else{
					return new ExecResult(false,"添加产品失败").toString();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new ExecResult(false,"添加产品发生异常").toString();
		}
	}
	@RequestMapping(value="/product/getshops/{productno}")
	public String loadShops(@PathVariable("productno") Long productno,HttpServletRequest request){
		List<ShopProduct> shops=ps.loadshops(productno);
		ExecResult er=new ExecResult();
		er.setResult(true);
		er.setData((JSON)JSON.toJSON(shops));
		return er.toString();
	}
	//更新产品
	@RequestMapping(value="/product/update",method= RequestMethod.POST)
	public String updateProduct(HttpServletRequest request){
		try{
			String jsonBody = IOUtils.toString(request.getInputStream(),"UTF-8");
			JSONObject form= JSONObject.parseObject(jsonBody);
			ProductInfo product=new ProductInfo();
			product.setProductno(form.getLong("productno"));
			product.setProductname(form.getString("productname"));
			product.setProductdecs(form.getString("productdecs"));
			ChineseLetter cte = new ChineseLetter();
			product.setProductletter(cte.getAllFirstLetter(form.getString("productname")));
			product.setProducttype1(form.getString("producttype1"));
			product.setProductprice1(form.getFloat("productprice1"));
			product.setProductprice2(form.getFloat("productprice2"));
			product.setProductcount(form.getString("productcount"));
			product.setProductunit(form.getString("productunit"));

			//更新产品信息
			ProductInfo result=ps.updateProduct(product);

			if(result==null){
				return new ExecResult(false,"更新产品失败").toString();
			}else{
				JSONArray shopsarry=form.getJSONArray("shops");
				ShopProduct [] shops=null;
				if(shopsarry.size()>0){//如果有指定的门店价格。
					shops=new ShopProduct[shopsarry.size()];
					for(int i=0;i<shopsarry.size();i++){
						JSONObject jo=shopsarry.getJSONObject(i);
						shops[i]=new ShopProduct();
						shops[i].setProductno(result.getProductno());
						shops[i].setShopno(jo.getLong("shopNo"));
						shops[i].setProductprice1(jo.getFloat("price1"));
						shops[i].setProductprice2(jo.getFloat("price2"));
					}
				}
				if(ps.saveShops(result.getProductno(),shops)){
					ExecResult er=new ExecResult();
					er.setResult(true);
					er.setData((JSON)JSON.toJSON(result));
					return er.toString();
				}else{
					return new ExecResult(false,"更新产品失败").toString();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return new ExecResult(false,"更新产品发生异常").toString();
		}
	}
	@RequestMapping(value="/product/delete/{no}",method= RequestMethod.DELETE)
	public String delProducts(@PathVariable("no") Long id,HttpServletRequest request,HttpServletResponse response){
		try{
			if(ps.deleteProducts(id)){
				return new ExecResult(true,"产品删除成功").toString();
			}else{
				return new ExecResult(false,"产品删除失败").toString();
			}

		}catch (Exception e){
			e.printStackTrace();
			return new ExecResult(false,"产品删除过程中发生异常").toString();
		}

	}

	@RequestMapping(value="/product/products/{type}")
	public String getProductsByType(@PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response){
		List<ProductInfo> p=ps.getProductsByType(type);
		String result=JSON.toJSONString(p);
		System.out.println(result);
		return result;
	}

    @RequestMapping(value="/product/products/{type}/{name}")
    public String getProductsByTypeAndName(@PathVariable("type") String type,@PathVariable("name") String name){
        List<ProductInfo> p=ps.getProductsByTypeAndName(type,name);
        String result=JSON.toJSONString(p);
        System.out.println(result);
        return result;
    }
}
