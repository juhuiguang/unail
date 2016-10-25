package com.unail.mvc.service.product;

import java.util.List;

import com.alienlab.db.DAO;
import com.alienlab.response.SqlService;
import com.unail.repositories.entity.ShopProduct;
import com.unail.repositories.inter.ShopProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unail.repositories.entity.ProductInfo;
import com.unail.repositories.inter.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ShopProductRepository spr;

    public ProductInfo addProduct(ProductInfo p) {
        try{
            p=repository.save(p);
            return p;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public List<ProductInfo> getProducts() {
        return repository.findAll();
    }

    public List<ProductInfo> getProductsByType(String type1){
        return repository.findProductsByProducttype1(type1);
    }

    public List<ProductInfo> getProductsByTypeAndName(String type1,String name){
        return repository.findProductsByProducttype1AndProductnameLike(type1,"%"+name+"%");
    }

    public ProductInfo updateProduct(ProductInfo product){
        try{
            product=repository.save(product);
            return product;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public boolean deleteProducts(Long id){
        try{
            repository.delete(id);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public List<ShopProduct> loadshops(Long productno){
        return spr.findByProductno(productno);
    }

    public List<ProductInfo> getAllProducts(String special){
        if(special.equalsIgnoreCase("ALL")){
            return repository.findAll();
        }else{
            String sql="SELECT a.product_no,a.product_name,a.product_letter,a.product_type1,a.product_type2," +
                    "b.`product_price1`,b.`product_price2`," +
                    "a.`product_unit`,a.`product_count`,a.`product_decs`,a.`product_cttime`,a.`product_user` " +
                    "FROM product_info a,shop_product b WHERE a.`product_no`=b.`product_no` AND b.`shop_no`="+special+" ORDER BY a.product_type1,a.product_no";
            SqlService s=new SqlService();
            List l=s.selectResult(sql,null);
            return DAO.list2T(l,ProductInfo.class);
        }
    }
    public boolean saveShops(Long productno,ShopProduct[] shops){
        spr.deleteByProductno(productno);
        for(int i=0;i<shops.length;i++){
            spr.save(shops[i]);
        }
        return true;
    }

    public static void main(String[] args) {

    }
}
