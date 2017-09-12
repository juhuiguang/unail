package com.unail.mvc.service.card;

import com.alienlab.db.DAO;
import com.alienlab.response.SqlService;
import com.unail.repositories.entity.Card;
import com.unail.repositories.entity.CardKind;
import com.unail.repositories.entity.CardKindProduct;
import com.unail.repositories.entity.Custom;
import com.unail.repositories.entity.logicentity.CardKindItem;
import com.unail.repositories.entity.logicentity.LCardKindProduct;
import com.unail.repositories.inter.CardKindProductRepository;
import com.unail.repositories.inter.CardKindRepository;
import com.unail.repositories.inter.CardRepository;
import com.unail.repositories.inter.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by 橘 on 2016/7/4.
 */
@Service
public class CardService {
    @Autowired
    CardKindRepository cardKindRepository;
    @Autowired
    CardKindProductRepository cardKindProductRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    CustomRepository customRepository;
    public List<CardKindItem> getCardkinds(String shop,String type){
        if(shop.equalsIgnoreCase("ALL")){
            shop="";
        }
        String sql="SELECT a.card_kind_no,card_kind_type,card_kind_name,card_kind_desc,card_kind_duetime,card_kind_sales,a.card_money,a.card_times,a.balance_type,a.`card_number_end`,a.`card_kind_useshop`,a.`cardkind_status`,a.card_number_prefix,a.if_calculate_sales,COUNT(DISTINCT b.`card_no`) cardcount " +
                " FROM `card_kind` a LEFT JOIN card b ON a.`card_kind_no`=b.`card_kind_no`" +
                " WHERE a.`card_kind_useshop` LIKE '%"+shop+"%' and " +(type.equals("全部")?"1=1":"a.card_kind_type='"+type+"'")+
                " GROUP BY a.card_kind_no,card_kind_type,card_kind_name,card_kind_desc,card_kind_duetime,card_kind_sales,a.card_money,a.card_times,a.balance_type,a.`card_number_end`,a.`card_kind_useshop`,a.`cardkind_status`,a.card_number_prefix";
        SqlService sqlService=new SqlService();
        List<CardKindItem> result= DAO.list2T(sqlService.selectResult(sql,null),CardKindItem.class);
        return result;
    }

    public List<LCardKindProduct> loadCardkindProduct(String cardkindno){
        String sql="select b.*,a.card_kind_no,a.id,a.serve_discount,a.serve_times,a.serve_cycle from card_kind_product a,product_info b where a.product_no=b.product_no and a.card_kind_no="+cardkindno;
        SqlService sqlService=new SqlService();
        List<LCardKindProduct> result= DAO.list2T(sqlService.selectResult(sql,null),LCardKindProduct.class);
        return result;
    }
    public CardKind addCardkind(CardKind kind){
        return cardKindRepository.save(kind);
    }

    public CardKind getCardKind(Long cardkindno){return cardKindRepository.findOne(cardkindno);}


    public boolean addKindProducts(Long cardkindno,List<CardKindProduct> products){
        try{
            cardKindProductRepository.deleteByCardkindno(cardkindno);
            for(int i=0;i<products.size();i++){
                cardKindProductRepository.save(products.get(i));
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }


    }

    public boolean delKind(Long kindno){
        String sql="select * from card where card_kind_no="+kindno+" limit 1,5";
        SqlService s=new SqlService();
        List result=s.selectResult(sql,null);
        if(result.size()>0){
            return false;
        }else{
            cardKindRepository.delete(kindno);
            cardKindProductRepository.deleteByCardkindno(kindno);
            return true;
        }
    }

    public Card saveCard(Card card){
        return cardRepository.save(card);
    }

    public boolean delCard(Long cardid){
        try{
            cardRepository.delete(cardid);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }


    }

    public int getCardno(Long kindno){
        CardKind ck=getCardKind(kindno);
        if(ck!=null){
            Card latestcard=cardRepository.findFirstCardByCardkindOrderByCardseqDesc(ck);
            if(latestcard==null){
                return 1;
            }else{
                return latestcard.getCardseq()+1;
            }
        }else{
            return 0;
        }
    }

    public Page<Card> getCards(Long cardkindno,String keyword,int pageIndex,int pageSize){
        return cardRepository.findCardsByQuery(cardKindRepository.findOne(cardkindno),keyword,new PageRequest(pageIndex , pageSize, new Sort(Sort.Direction.ASC, "cardno")));
    }

    public List<Card> loadCustomCards(Long cusno){
        Custom c=customRepository.findOne(cusno);
        return cardRepository.findCardsByCustomAndCardstatusAndCardduetimeGreaterThan(c,1,new Date());
    }

	public boolean updcardstatus(String flag, Long id) {
		// TODO Auto-generated method stub
		String sql = "";
		if(flag.equals("disable")) {
			sql = "UPDATE card SET card_status='0' WHERE card_id='"+id+"'";
		} else if(flag.equals("enable")) {
			sql = "UPDATE card SET card_status='1' WHERE card_id='"+id+"'";
		}
		SqlService s=new SqlService();
		return s.executeResult(sql, null);   
	}

}
