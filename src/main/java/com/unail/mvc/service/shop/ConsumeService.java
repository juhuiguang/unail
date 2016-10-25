package com.unail.mvc.service.shop;

import com.alienlab.db.DAO;
import com.alienlab.response.SqlService;
import com.unail.repositories.entity.*;
import com.unail.repositories.entity.logicentity.CardSaledItem;
import com.unail.repositories.entity.logicentity.ConsumeItem;
import com.unail.repositories.inter.*;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by 橘 on 2016/7/30.
 */
@Service
public class ConsumeService {
    private static Logger logger = Logger.getLogger(ConsumeService.class);

    @Autowired
    CardRepository cardRepository;
    @Autowired
    CardDetailRepository cardDetailRepository;
    @Autowired
    CardKindProductRepository cardKindProductRepository;
    @Autowired
    BalanceCardRepository balanceCardRepository;
    @Autowired
    BalanceRepository balanceRepository;
    @Autowired
    CustomRepository customRepository;

    /**
     * 验证卡片是否可以用于指定产品
     * @param card
     * @param product
     * @return
     */
    public boolean validateCard(Card card, ProductInfo product){
        CardKind cardKind=card.getCardkind();
        CardKindProduct cardproduct=cardKindProductRepository.findCardKindProductByCardkindnoAndProductno(cardKind.getCardkindno(),product.getProductno());
        if(cardproduct==null){
            logger.error("卡片:"+card.getCardno()+",项目:"+product.getProductname()+" 未能匹配");
            return false;
        }
        //判断卡片使用时间与周期，有三种情况：1卡片无周期限制，有次数限制，仅判断次数限制；2卡片有周期限制有次数限制，判断卡片在当前日期前一个周期范围内使用次数；卡片无周期限制，无次数限制，无需判断。
        if(cardproduct.getServetimes()>0){//如果有次数限制
            if(cardproduct.getServecycle()==0){//无周期限制
                List<CardUseDetail> details=cardDetailRepository.findCardUserDetailsByConsumepro(product.getProductname());
                if(details!=null&&details.size()>=cardproduct.getServetimes()){
                    logger.error("卡片:"+card.getCardno()+",在项目:"+product.getProductname()+" 已使用了"+details.size()+"次，超出限制");
                    return false;
                }
            }else if(cardproduct.getServecycle()>0){//有周期限制
                Date end=new Date();
                Date start=new Date(end.getTime()-1000*60*60*24*cardproduct.getServecycle());
                List<CardUseDetail> details=cardDetailRepository.findCardUserDetailsByConsumeproAndConsumetimeBetween(product.getProductname(),start,end);
                if(details!=null&&details.size()>=cardproduct.getServetimes()){
                    logger.error("卡片:"+card.getCardno()+",在项目:"+product.getProductname()+" 已使用了"+details.size()+"次，超出限制");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 使用充值卡片购买其他促销卡片
     * @param card
     * @param cardkind
     * @param money
     * @return
     * @throws Exception
     */
    public CardUseDetail consumeCardForCard(Card card,CardKind cardkind,Float money)throws Exception{
        if(card.getSurplussales()<money){//验证余额
            logger.error("卡片 "+card.getCardno()+"余额不足:"+card.getSurplussales()+"<"+money);
            throw new Exception("卡片 "+card.getCardno()+"余额不足:"+card.getSurplussales()+"<"+money);

        }
        if(money>0){//如果需要扣钱
            card.setSurplussales(card.getSurplussales()-money);
        }
        card.setLastconsumepro(cardkind.getCardkindname());
        card.setLastconsumesales(money);
        card.setLastconsumetime(new Date());
        cardRepository.save(card);//更新card信息
        //记录消费明细
        CardUseDetail detail=new CardUseDetail();
        detail.setConsumetime(new Date());
        detail.setCardconsumesales(money);
        detail.setCardconsumetimes(0);
        detail.setCardno(card.getCardid());
        detail.setCardsurplussales(card.getSurplussales());
        detail.setCardsurplustimes(card.getSurplustimes());
        detail.setConsumepro(cardkind.getCardkindname());
        detail=cardDetailRepository.save(detail);
        if(detail.getDetailno()!=null){
            return detail;
        }else{
            return null;
        }
    }

    /**
     * 卡片消费
     * @param card 需消费的卡片
     * @param product 消费的项目
     * @param money 消费的金额
     * @param times 消费的次数
     * @return
     */
    public CardUseDetail consumeCard(Card card,ProductInfo product,Float money,int times) throws Exception{
        if(!validateCard(card,product)){//验证是否在使用周期内
            logger.error(card.getCardno()+" 卡片不可用于 "+product.getProductname()+"抵扣");
            throw new Exception(card.getCardno()+" 卡片不可用于 "+product.getProductname()+"抵扣");
        }
        if(card.getSurplussales()<money){//验证余额
            logger.error("卡片 "+card.getCardno()+"余额不足:"+card.getSurplussales()+"<"+money);
            throw new Exception("卡片 "+card.getCardno()+"余额不足:"+card.getSurplussales()+"<"+money);

        }
        if(card.getSurplustimes()<times){//验证次数
            logger.error("卡片 "+card.getCardno()+"剩余次数不足:"+card.getSurplustimes()+"<"+times);
            throw new Exception("卡片 "+card.getCardno()+"剩余次数不足:"+card.getSurplustimes()+"<"+times);
        }
        if(money>0){//如果需要扣钱
            card.setSurplussales(card.getSurplussales()-money);
        }
        if(times>0){//如果需要扣次数
            card.setSurplustimes(card.getSurplustimes()-times);
        }

        card.setLastconsumepro(product.getProductname());
        card.setLastconsumesales(money);
        card.setLastconsumetime(new Date());
        cardRepository.save(card);//更新card信息
        //记录消费明细
        CardUseDetail detail=new CardUseDetail();
        detail.setConsumetime(new Date());
        detail.setCardconsumesales(money);
        detail.setCardconsumetimes(times);
        detail.setCardno(card.getCardid());
        detail.setCardsurplussales(card.getSurplussales());
        detail.setCardsurplustimes(card.getSurplustimes());
        detail.setConsumepro(product.getProductname());
        detail=cardDetailRepository.save(detail);
        if(detail.getDetailno()!=null){
            return detail;
        }else{
            return null;
        }

    }

    /**
     * 记录消费日志
     * @param balance 项目结算详情
     * @param cardlogs 项目结算中各卡片的使用
     * @return
     */
    public boolean logconsume(ProductBalance balance,List<CardUseDetail> cardlogs){
        try{
            balance=balanceRepository.save(balance);
            for(int i=0;i<cardlogs.size();i++){
                BalanceCard balanceCard=new BalanceCard();
                balanceCard.setBalance(balance);
                balanceCard.setCarddetail(cardlogs.get(i));
                balanceCardRepository.save(balanceCard);
            }
            //如果消费存在客户信息，更新客户最后一次消费时间
            if(balance.getCustom()!=null){
                Custom cus=balance.getCustom();
                cus.setLatelyconsumetime(balance.getConsumetime());
                if(cus.getFirstconsumetime()==null){
                    cus.setFirstconsumetime(balance.getConsumetime());
                }
                customRepository.save(cus);
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    //当结算失败时回滚卡消费记录
    public boolean rollbackconsume(CardUseDetail detail){
        Card c=cardRepository.findOne(detail.getCardno());
        //将卡片的消费数据加回卡中
        c.setSurplustimes(c.getSurplustimes()+detail.getCardconsumetimes());
        c.setSurplussales(c.getSurplussales()+detail.getCardconsumesales());
        c.setLastconsumesales(0f);
        cardRepository.save(c);
        cardDetailRepository.delete(detail);//删除消费明细
        return true;
    }

    public List<ConsumeItem> getConsumeDetail(Long shopid,String date){
        String sql="SELECT a.*,c.`shop_name`,e.* FROM product_balance a,shop c,custom e " +
                "WHERE " +
                "a.`consume_shop`=c.`shop_id` " +
                " AND c.`shop_id`=" +shopid+
                " AND a.`custom_no`=e.`custom_no` " +
                " AND a.consume_time>='"+date+" 00:00:00' " +
                " AND a.consume_time<'"+date+" 23:59:59' " +
                "ORDER BY consume_time";
        SqlService s=new SqlService();
        List<Map<String,Object>>result=s.selectResult(sql,null);
        return DAO.list2T(result,ConsumeItem.class);
    }

    public List<CardSaledItem> getCardSaleDetail(Long shopid,String date){
        String sql="SELECT a.*,c.`shop_name`,d.`card_kind_sales`,d.card_kind_name FROM card_sales_record a,shop c,card_kind d,card e " +
                " WHERE a.sales_shop=c.`shop_id` AND c.`shop_id`="+shopid+" AND a.`card_no`=e.`card_no` " +
                " AND e.`card_kind_no`=d.`card_kind_no` AND a.sales_time>='"+date+" 00:00:00' " +
                " AND a.sales_time<'"+date+" 23:59:59' ORDER BY sales_time ";
        SqlService s=new SqlService();
        List<Map<String,Object>>result=s.selectResult(sql,null);
        return DAO.list2T(result,CardSaledItem.class);
    }
}
