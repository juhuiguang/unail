package com.unail.mvc.controller.card;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.common.DateUtils;
import com.alienlab.common.TypeUtils;
import com.alienlab.db.ExecResult;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.unail.mvc.service.card.CardService;
import com.unail.mvc.service.shop.ConsumeService;
import com.unail.repositories.entity.*;
import com.unail.repositories.entity.logicentity.CardKindItem;
import com.unail.repositories.entity.logicentity.LCardKindProduct;
import com.unail.repositories.inter.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by 橘 on 2016/7/4.
 */
@RestController
public class CardController {
    @Autowired
    CardService cardService;
    @Autowired
    CardKindRepository cardKindRepository;
    @Autowired
    CustomRepository customRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    CardKindProductRepository cardKindProductRepository;

    @RequestMapping(value = "/card/cardkinds")
    public String getCardkinds(HttpServletRequest request) {
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject param = JSONObject.parseObject(jsonBody);
            String shop = param.getString("shop");
            String type = param.getString("type");
            List<CardKindItem> data = cardService.getCardkinds(shop, type);
            ExecResult er = new ExecResult();
            er.setResult(true);
            er.setData((JSON) JSON.toJSON(data));
            return er.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ExecResult(false, "获取卡种列表发生异常.").toString();
        }
    }

    @RequestMapping(value = "/card/loadCardkindProducts")
    public String loadCardkindProducts(HttpServletRequest request){
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject param = JSONObject.parseObject(jsonBody);
            List<LCardKindProduct> list=cardService.loadCardkindProduct(param.getString("cardkindno"));
            ExecResult er = new ExecResult();
            er.setResult(true);
            er.setData((JSON) JSON.toJSON(list));
            return er.toString();
        }catch(Exception ex){
            ex.printStackTrace();
            return new ExecResult(false, "获取卡种对应产品发生异常.").toString();
        }

    }

    @RequestMapping(value = "/card/delcardkind")
    public String delCardkind(HttpServletRequest request) {
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject param = JSONObject.parseObject(jsonBody);
            Long kindno = param.getLong("cardkindno");

            boolean result = cardService.delKind(kindno);
            if (result) {
                ExecResult er = new ExecResult();
                er.setResult(true);
                return er.toString();
            } else {
                return new ExecResult(false, "卡种下已有生成的卡片，无法删除。").toString();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return new ExecResult(false, "删除卡种列表发生异常.").toString();
        }
    }

    @RequestMapping(value = "/card/addkind")
    public String addCardKind(HttpServletRequest request) {
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject form = JSONObject.parseObject(jsonBody);

            CardKind ck = new CardKind();
            //如果表单提交时有主键NO，表示修改
            if (form.containsKey("cardkindno") && form.getLong("cardkindno") > 0) {
                ck.setCardkindno(form.getLong("cardkindno"));
            }
            ck.setCardkindtype(form.getString("cardkindtype"));
            ck.setBalancetype(form.getString("balancetype"));
            ck.setCardkinddesc(form.getString("cardkinddesc"));
            String date=form.getString("cardkindduetime2");
            if(date.indexOf("T")>0){
                ck.setCardkindduetime(TypeUtils.str2date(date.split("T")[0], "yyyy-MM-dd"));
            }else{
                Date d=new Date();
                d.setTime(form.getLong("cardkindduetime2"));
                ck.setCardkindduetime(d);
            }
           // ck.setCardkindduetime(TypeUtils.str2date(form.getString("cardkindduetime").split("T")[0], "yyyy-MM-dd"));
            ck.setCardkindname(form.getString("cardkindname"));
            ck.setCardkindsales(form.getFloat("cardkindsales"));
            ck.setIfcalculatesales(form.getBoolean("ifcalculatesales")?1:0);
            String shops = "";
            JSONArray shoparray = form.getJSONArray("shops");
            for (int i = 0; i < shoparray.size(); i++) {
                JSONObject shopitem = shoparray.getJSONObject(i);
                if (shopitem.containsKey("$isselected") && shopitem.getBoolean("$isselected")) {
                    if (shops.equals("")) {
                        shops += shopitem.getString("shopNo");
                    } else {
                        shops += "," + shopitem.getString("shopNo");
                    }

                }
            }
            ck.setCardkinduseshop(shops);
            ck.setCardmoney(form.getFloat("cardmoney"));
            //计算现金价值系数
            if(ck.getCardmoney()>0){
                //修正小数位数过多问题
                //String result = String.format(String.valueOf(ck.getCardkindsales()/ck.getCardmoney()),"%.2f");
                NumberFormat ddf1=NumberFormat.getNumberInstance() ;
                ddf1.setMaximumFractionDigits(2);
                String result=ddf1.format(ck.getCardkindsales()/ck.getCardmoney());
                ck.setCardkindrate(Float.parseFloat(result));
            }else{
                ck.setCardkindrate(0f);
            }

            ck.setCardnumberprefix(form.getString("cardnumberprefix"));
            ck.setCardtimes(form.getInteger("cardtimes"));
            ck = cardService.addCardkind(ck);
            if (ck.getCardkindno() != null && ck.getCardkindno() > 0) {
                JSONArray products = form.getJSONArray("products");
                List<CardKindProduct> ps = new ArrayList<CardKindProduct>();
                for (int i = 0; i < products.size(); i++) {
                    JSONObject pitem = products.getJSONObject(i);
                    CardKindProduct ckp = new CardKindProduct();
                    if (pitem.containsKey("id") && pitem.getLong("id") > 0) {
                        ckp.setId(pitem.getLong("id"));
                    }
                    ckp.setServediscount(pitem.getFloat("servediscount"));
                    ckp.setServetimes(pitem.getInteger("servetimes"));
                    ckp.setServecycle(pitem.getInteger("servecycle"));
                    ckp.setCardkindno(ck.getCardkindno());
                    ckp.setProductno(pitem.getLong("productno"));
                    ps.add(ckp);
                }
                if (cardService.addKindProducts(ck.getCardkindno(),ps)) {
                    return new ExecResult(true, "卡种创建成功.").toString();
                } else {
                    return new ExecResult(false, "卡种对应的产品设置错误.").toString();
                }
            } else {
                return new ExecResult(false, "卡种创建错误.").toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ExecResult(false, "卡种创建发生异常.").toString();
        }
    }
    @RequestMapping(value = "/card/updateCardkindStatus")
    public String updateCardkindStatus(HttpServletRequest request){
        try{
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject form = JSONObject.parseObject(jsonBody);
            CardKind c=cardService.getCardKind(form.getLong("cardkindno"));
            c.setCardkindstatus(form.getInteger("cardkindstatus"));
            cardKindRepository.save(c);
            return new ExecResult(true, "卡种状态设置成功.").toString();
        }catch (Exception ex){
            ex.printStackTrace();
            return new ExecResult(false, "卡种状态设置发生异常.").toString();
        }

    }

    @RequestMapping(value = "/card/updatekind")
    public String updateCardKind(HttpServletRequest request) {
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject form = JSONObject.parseObject(jsonBody);

            CardKind ck = new CardKind();
            //如果表单提交时有主键NO，表示修改
            if (form.containsKey("cardkindno") && form.getLong("cardkindno") > 0) {
                ck.setCardkindno(form.getLong("cardkindno"));
            }
            ck.setCardkindtype(form.getString("cardkindtype"));
            ck.setBalancetype(form.getString("balancetype"));
            ck.setCardkinddesc(form.getString("cardkinddesc"));
            String date=form.getString("cardkindduetime2");
            if(date.indexOf("T")>0){
                ck.setCardkindduetime(TypeUtils.str2date(date.split("T")[0], "yyyy-MM-dd"));
            }else{
                Date d=new Date();
                d.setTime(form.getLong("cardkindduetime2"));
                ck.setCardkindduetime(d);
            }

            ck.setCardkindname(form.getString("cardkindname"));
            ck.setCardkindsales(form.getFloat("cardkindsales"));
            ck.setIfcalculatesales(form.getBoolean("ifcalculatesales")?1:0);
            String shops = "";
            JSONArray shoparray = form.getJSONArray("shops");
            for (int i = 0; i < shoparray.size(); i++) {
                JSONObject shopitem = shoparray.getJSONObject(i);
                if (shopitem.containsKey("$isselected") && shopitem.getBoolean("$isselected")) {
                    if (shops.equals("")) {
                        shops += shopitem.getString("shopNo");
                    } else {
                        shops += "," + shopitem.getString("shopNo");
                    }

                }
            }
            ck.setCardkinduseshop(shops);
            ck.setCardmoney(form.getFloat("cardmoney"));
            //计算现金价值系数
            if(ck.getCardmoney()>0){
                //修正小数位数过多问题
                //String result = String.format(String.valueOf(ck.getCardkindsales()/ck.getCardmoney()),"%.2f");
                NumberFormat ddf1=NumberFormat.getNumberInstance() ;
                ddf1.setMaximumFractionDigits(2);
                String result=ddf1.format(ck.getCardkindsales()/ck.getCardmoney());
                ck.setCardkindrate(Float.parseFloat(result));
            }else{
                ck.setCardkindrate(0f);
            }
            ck.setCardnumberprefix(form.getString("cardnumberprefix"));
            ck.setCardtimes(form.getInteger("cardtimes"));
            ck = cardService.addCardkind(ck);

            //更新对应的产品
            JSONArray products = form.getJSONArray("products");
            List<CardKindProduct> ps = new ArrayList<CardKindProduct>();
            for (int i = 0; i < products.size(); i++) {
                JSONObject pitem = products.getJSONObject(i);
                CardKindProduct ckp = new CardKindProduct();
                if (pitem.containsKey("id") && pitem.getLong("id") > 0) {
                    ckp.setId(pitem.getLong("id"));
                }
                ckp.setServediscount(pitem.getFloat("servediscount"));
                ckp.setServetimes(pitem.getInteger("servetimes"));
                ckp.setServecycle(pitem.getInteger("servecycle"));
                ckp.setCardkindno(ck.getCardkindno());
                ckp.setProductno(pitem.getLong("productno"));
                ps.add(ckp);
            }
            if (cardService.addKindProducts(ck.getCardkindno(),ps)) {
                return new ExecResult(true, "卡种更新成功.").toString();
            } else {
                return new ExecResult(false, "卡种对应的产品设置错误.").toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ExecResult(false, "卡种更新发生异常.").toString();
        }
    }
    @RequestMapping(value = "/card/cards")
    public String getCards(HttpServletRequest request){
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject params = JSONObject.parseObject(jsonBody);
            Long cardkindno=params.getLong("cardkindno");
            int pageIndex=params.getInteger("pageindex");
            int pageSize=params.getInteger("pagesize");
            String keyword=params.getString("keyword");
            if(keyword==null) keyword="";
            Page<Card> cards=cardService.getCards(cardkindno,keyword,pageIndex,pageSize);
            ExecResult er=new ExecResult();
            er.setResult(true);
            er.setData((JSON)JSON.toJSON(cards));
            return er.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return new ExecResult(false, "获得卡片发生异常.").toString();
        }
    }
    @RequestMapping(value = "/card/newcardseq")
    public String newCardSeq(HttpServletRequest request){
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject params = JSONObject.parseObject(jsonBody);
            Long cardkindno= params.getLong("cardkindno");
            int no=cardService.getCardno(cardkindno);
            if(no==0){
                return new ExecResult(false, "获得卡号失败.").toString();
            }else{
                ExecResult er=new ExecResult();
                er.setResult(true);
                JSONObject jo=new JSONObject();
                jo.put("value",no);
                er.setData(jo);
                return er.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ExecResult(false, "获得卡号发生异常.").toString();
        }
    }
    @Autowired
    ConsumeService consumeService;
    @Autowired
    CardSalesRepository cardSalesRepository;
    @RequestMapping(value = "/card/newcard")
    public String newCard(HttpServletRequest request){
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject params = JSONObject.parseObject(jsonBody);
            //System.out.println(params.toJSONString());
            JSONObject cardkind=params.getJSONObject("cardkind");
            JSONObject custom=params.getJSONObject("custom");
            JSONObject user= params.getJSONObject("staff");
            JSONArray cards=params.getJSONArray("cards");
            int cardseq=params.getInteger("cardseq");
            CardKind kind=cardKindRepository.findOne(cardkind.getLong("cardkindno"));
            if(kind==null){
                return new ExecResult(false, "未找到关联的卡券种类.").toString();
            }
            if(!kind.isvalid()){
                return new ExecResult(false, "卡券种类不可用.").toString();
            }
            Custom cus=customRepository.findOne(custom.getLong("customno"));
            if(cus==null){
                return new ExecResult(false, "未找到关联的用户信息.").toString();
            }
            //逐个消费卡片扣钱
            List<CardUseDetail> successdetails=new ArrayList<CardUseDetail>();
            int cardcount=0;
            for(int i=0;cards!=null&&i<cards.size();i++){
                JSONObject card=cards.getJSONObject(i);
                if(card.containsKey("$isused")&&card.getBoolean("$isused")){
                    cardcount++;
                    try{
                        Card carditem=cardRepository.findCardByCardno(card.getString("cardno"));
                        CardUseDetail detail=consumeService.consumeCardForCard(carditem,kind,card.getFloat("dealprice"));
                        if(detail!=null){
                            successdetails.add(detail);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            //卡片扣款没有完全成功
            if(successdetails.size()!=cardcount){
                //回滚已扣款卡片
                for(int i=0;i<successdetails.size();i++){
                    consumeService.rollbackconsume(successdetails.get(i));
                }
                return new ExecResult(false, "卡片扣款失败.").toString();
            }
            //创建卡片
            Card c=new Card();
            c.setCardkind(kind);
            String cardno=kind.getCardnumberprefix()+"-"+String.valueOf(cardseq);
            c.setCardno(cardno);
            c.setCardseq(cardseq);
            c.setCardstatus(1);
            c.setCustom(cus);
            c.setSurplussales(kind.getCardmoney());
            c.setSurplustimes(kind.getCardtimes());
//            String cardduetime= params.getString("cardduetime");
//            c.setCardduetime(DateUtils.getDate(cardduetime.split("T")[0],"yyyy-MM-dd"));
            String date=params.getString("cardduetime");
            if(date.indexOf("T")>0){
                c.setCardduetime(TypeUtils.str2date(date.split("T")[0], "yyyy-MM-dd"));
            }else{
                Date d=new Date();
                d.setTime(params.getLong("cardduetime"));
                c.setCardduetime(d);
            }
            c=cardService.saveCard(c);//保存卡片
            ExecResult er=new ExecResult();
            er.setResult(true);
            er.setData((JSON)JSON.toJSON(c));
            er.setMessage("卡片保存成功");

            //保存卡片销售记录
            CardSalesRecord record=new CardSalesRecord();
            record.setCardno(c.getCardno());
            record.setCustomername(c.getCustom().getCustomname());
            record.setCustomerphone(c.getCustom().getCustomphone());
            record.setSalesmoney(params.getFloat("cash"));
            record.setSalesshop(user.getString("purview"));
            record.setSalesstaff(user.getString("username"));
            record.setSalestime(new Date());
            record=cardSalesRepository.save(record);
            return er.toString();
        }catch (Exception ex){
            ex.printStackTrace();
            return new ExecResult(false, "生成卡片发生异常.").toString();
        }
    }

    @RequestMapping(value = "/card/gencards")
    public String genCards(HttpServletRequest request){
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject params = JSONObject.parseObject(jsonBody);
            //System.out.println(params.toJSONString());
            JSONObject cardkind=params.getJSONObject("cardkind");
            JSONObject custom=params.getJSONObject("custom");
            int cardseqstart=params.getInteger("cardseqstart");
            int cardseqend=params.getInteger("cardseqend");
            CardKind kind=cardKindRepository.findOne(cardkind.getLong("cardkindno"));
            if(kind==null){
                return new ExecResult(false, "未找到关联的卡券种类.").toString();
            }
            if(!kind.isvalid()){
                return new ExecResult(false, "卡券种类不可用.").toString();
            }
            List<Card> exists=cardRepository.findCardsByCardkindAndCardseqBetween(kind,cardseqstart,cardseqend);
            if(exists.size()>0){
                return new ExecResult(false, "批量生成的卡号中有"+exists.size()+"张卡已存在.").toString();
            }
            for(int i=cardseqstart;i<=cardseqend;i++){
                Card c=new Card();
                c.setCardkind(kind);
                String cardno=kind.getCardnumberprefix()+"-"+String.valueOf(i);
                c.setCardno(cardno);
                c.setCardseq(i);
                c.setCardstatus(1);
                c.setSurplussales(kind.getCardmoney());
                c.setSurplustimes(kind.getCardtimes());
                Long cardduetime= params.getLong("cardduetime");
                Date d=new Date();
                d.setTime(cardduetime);
                c.setCardduetime(d);
                c=cardService.saveCard(c);
            }
            ExecResult er=new ExecResult();
            er.setResult(true);
            JSONObject jo=new JSONObject();
            jo.put("value",(cardseqend-cardseqstart)+1);
            er.setData(jo);
            er.setMessage("卡片保存成功");
            return er.toString();
        }catch (Exception ex){
            ex.printStackTrace();
            return new ExecResult(false, "生成卡片发生异常.").toString();
        }
    }
    @RequestMapping(value = "/card/loadcustomcard")
    public String loadCustomerCards(HttpServletRequest request){
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject params = JSONObject.parseObject(jsonBody);
            Long cusno=params.getLong("customno");
            List<Card> cards=cardService.loadCustomCards(cusno);
            for(int i=0;i<cards.size();i++){
                Card card=cards.get(i);
                List<CardKindProduct> products=cardKindProductRepository.findCardKindProductsByCardkindno(card.getCardkind().getCardkindno());
                card.getCardkind().setProducts(products);
            }
            ExecResult er=new ExecResult();
            er.setResult(true);
            er.setData((JSON)JSON.toJSON(cards));
            return er.toString();
        }catch (Exception ex){
            ex.printStackTrace();
            return new ExecResult(false, "加载客户卡券发生异常.").toString();
        }
    }
    @RequestMapping(value = "/card/findcard")
    public String findCard(HttpServletRequest request){
        try {
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject params = JSONObject.parseObject(jsonBody);
            String cardno=params.getString("cardno");
            Card c=cardRepository.findCardByCardno(cardno);
            List<CardKindProduct> products=cardKindProductRepository.findCardKindProductsByCardkindno(c.getCardkind().getCardkindno());
            c.getCardkind().setProducts(products);
            if(c!=null){
                ExecResult er=new ExecResult();
                er.setResult(true);
                er.setData((JSON)JSON.toJSON(c));
                return er.toString();
            }else{
                return new ExecResult(false, "未找到卡号为 "+cardno+" 的卡券.").toString();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return new ExecResult(false, "查找卡券发生异常.").toString();
        }
    }
    
    @RequestMapping(value="/card/delete/{cardid}",method=RequestMethod.DELETE)
    public String delCard(@PathVariable("cardid") Long id,HttpServletRequest request, HttpServletResponse response) {
    	try {
    		if(cardService.delCard(id)) {
    			return new ExecResult(true,"此卡片删除成功").toString();
    		} else {
    			return new ExecResult(false,"此卡片删除失败").toString();			
    		}
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    		return new ExecResult(false,"此卡片删除过程中发生异常").toString();
    	}
    }
    
    @RequestMapping(value="/card/updatecardstatus/{flag}/{cardid}")
    public String updCardstatus(@PathVariable("flag") String flag,@PathVariable("cardid") Long id) {
    	try {
    		if(cardService.updcardstatus(flag,id)) {
    			return new ExecResult(true,"此卡片修改状态成功").toString();
    		} else {
    			return new ExecResult(false,"此卡片修改状态失败").toString();			
    		}
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    		return new ExecResult(false,"此卡片修改状态过程中发生异常").toString();
    	}
    }

    @RequestMapping(value="/card/bindcard")
    public String bindCard(HttpServletRequest request){
        try{
            String jsonBody = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject params = JSONObject.parseObject(jsonBody);
            String cardno=params.getString("cardno");
            Long customId=params.getLong("customId");
            Card c=cardRepository.findCardByCardno(cardno);
            if(c!=null){
                Custom cus=customRepository.findOne(customId);
                c.setCustom(cus);
                c=cardRepository.save(c);
                ExecResult er=new ExecResult();
                er.setResult(true);
                er.setData((JSON) JSONObject.toJSON(c));
                return er.toString();
            }else{
                return new ExecResult(false,"查询不到卡片信息").toString();
            }


        }catch(Exception e) {
            e.printStackTrace();
            return new ExecResult(false,"此卡片绑定用户过程中发生异常").toString();
        }

    }
}
