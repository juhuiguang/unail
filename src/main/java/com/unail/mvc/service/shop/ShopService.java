package com.unail.mvc.service.shop;

import com.unail.repositories.entity.Shop;
import com.unail.repositories.inter.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * Created by 橘 on 2016/6/20.
 */
@Service
public class ShopService {
    @Autowired
    ShopRepository repository;

    public List<Shop> getShops() {
        return repository.findAll();
    }

    public Shop getShop(long shopid) {
        return repository.findOne(shopid);
    }

    //删除某一门店数据
	public boolean deleteShop(Long id) {
		// TODO Auto-generated method stub
		try {
			repository.delete(id);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Shop addShop(Shop shop) {
		// TODO Auto-generated method stub
		try {
			shop = repository.save(shop);
			return shop;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Shop> getSearchShop(String key) {
		// TODO Auto-generated method stub
		return repository.findShopsByShopNameLike("%"+key+"%");
	}

	public Shop updateShop(Shop shop) {
		// TODO Auto-generated method stub
		try{
			shop = repository.save(shop);
			return shop;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
