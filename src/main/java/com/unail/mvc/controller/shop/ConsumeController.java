package com.unail.mvc.controller.shop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.db.ExecResult;
import com.unail.mvc.service.shop.ConsumeService;
import com.unail.repositories.entity.*;
import com.unail.repositories.entity.logicentity.CardSaledItem;
import com.unail.repositories.entity.logicentity.ConsumeItem;
import com.unail.repositories.inter.CardRepository;
import com.unail.repositories.inter.CustomRepository;
import com.unail.repositories.inter.ProductRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by 橘 on 2016/7/30.
 */
@Controller
public class ConsumeController {
    @Autowired
    private CustomRepository customRepository;
    @Autowired
    private ConsumeService consumeService;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ProductRepository productRepository;
    @RequestMapping("/shop/consume")
    @ResponseBody
    public String consume(HttpServletRequest request){
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject form = JSONObject.parseObject(jsonBody);
            JSONObject custom=form.getJSONObject("custom");
            JSONObject shop=form.getJSONObject("currentshop");

            JSONArray consumeproducts=form.getJSONArray("consumeproducts");
            JSONObject result=new JSONObject();
            JSONArray success=new JSONArray();
            JSONArray errors=new JSONArray();
            String balanceno=UUID.randomUUID().toString();//用户的一次结算
            for(int i=0;i<consumeproducts.size();i++){//逐个处理项目结算
                JSONObject consumeproduct=consumeproducts.getJSONObject(i);
                //用于验证
                ProductInfo product=productRepository.findOne(consumeproduct.getJSONObject("product").getLong("productno"));
                //计算记录，使用的前台传递的产品信息
                ProductBalance balance=getBalance(consumeproduct,balanceno,custom);
                balance.setConsumeShop(shop.getLong("shopNo"));


                JSONArray dealcards=consumeproduct.getJSONArray("usedcards");
                List<CardUseDetail> details=new ArrayList<CardUseDetail>();
                for(int j=0;j<dealcards.size();j++){//每一张结算卡片
                    JSONObject dealcard=dealcards.getJSONObject(j);
                    Card c=cardRepository.findOne(dealcard.getJSONObject("card").getLong("cardid"));
                    if(c!=null){
                        if(c.getCustom()==null&&balance.getCustom()!=null){
                            c.setCustom(balance.getCustom());
                        }
                        Float money=dealcard.getFloat("surplussales");
                        int times=dealcard.getInteger("surplustimes");
                        try{
                            CardUseDetail detail=consumeService.consumeCard(c,product,money,times);
                            if(detail!=null){
                                details.add(detail);
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                            JSONObject erroritem=new JSONObject();
                            erroritem.put("error",ex.getMessage());
                            errors.add(erroritem);
                        }
                    }
                }
                if(details.size()==dealcards.size()){//如果卡片全部结算完成
                    consumeService.logconsume(balance,details);//该项目结算成功
                    JSONObject successitem=new JSONObject();
                    successitem.put("dealproduct",balance.getConsumeprono());
                    success.add(successitem);
                }else{//存在未成功的卡片结算，将已成功的进行回滚，该项目结算失败。
                    for(int j=0;j<details.size();j++){
                        consumeService.rollbackconsume(details.get(i));
                    }
                }
            }
            result.put("success",success);
            result.put("errors",errors);
            ExecResult er=new ExecResult();
            er.setResult(!(errors.size()>0));
            er.setData(result);
            return er.toString();
        }catch (Exception ex){
            ex.printStackTrace();
            return new ExecResult(false,"结算发生异常").toString();
        }
    }

    private ProductBalance getBalance(JSONObject product,String balanceno,JSONObject custom){
        ProductBalance balance=new ProductBalance();
        JSONObject productitem=product.getJSONObject("product");
        Float cash=product.getFloat("cash");
        balance.setCashprice(cash!=null?cash:0f);
        balance.setConsumeprono(productitem.getLong("productno"));
        balance.setConsumetime(new Date());
        balance.setDealprice(product.getFloat("dealprice"));
        balance.setExtraprice(product.getFloat("extradiscount"));
        balance.setBalanceno(balanceno);
        balance.setProcount(product.getInteger("productcount"));
        balance.setProname(productitem.getString("productname"));
        balance.setProprice(product.getFloat("price"));
        balance.setPay_total(balance.getCashprice()+balance.getDealprice());
        balance.setProduct_total(balance.getProprice()*balance.getProcount());
        JSONObject staff=product.getJSONObject("staff");
        if(staff!=null){
            balance.setStaff(staff.getInteger("staffid"));
        }

        if(product.containsKey("cashier")){
            balance.setCashier(product.getInteger("cashier"));
        }
        if(custom!=null){
            Custom cus=customRepository.findOne(custom.getLong("customno"));
            balance.setCustom(cus);
        }
        return balance;
    }

    @RequestMapping("/shop/consumedetail")
    @ResponseBody
    public String getConsumeDetail(HttpServletRequest request){
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject params = JSONObject.parseObject(jsonBody);
            Long shop=params.getLong("shop");
            String date=params.getString("date");
            List<ConsumeItem> result=consumeService.getConsumeDetail(shop,date);
            ExecResult er=new ExecResult();
            er.setData((JSON) JSON.toJSON(result));
            er.setResult(true);
            return er.toString();
        }catch(Exception ex){
            ex.printStackTrace();
            return new ExecResult(false,"获取门店结算明细发生异常").toString();
        }
    }

    @RequestMapping("/shop/cardsaledetail")
    @ResponseBody
    public String getCardSaleDetail(HttpServletRequest request){
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject params = JSONObject.parseObject(jsonBody);
            Long shop=params.getLong("shop");
            String date=params.getString("date");
            List<CardSaledItem> result=consumeService.getCardSaleDetail(shop,date);
            ExecResult er=new ExecResult();
            er.setData((JSON) JSON.toJSON(result));
            er.setResult(true);
            return er.toString();
        }catch(Exception ex){
            ex.printStackTrace();
            return new ExecResult(false,"获取卡片售卖明细发生异常").toString();
        }
    }


}
