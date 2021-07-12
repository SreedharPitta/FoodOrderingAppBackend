package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDAO;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantItemDAO;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ItemService {

    @Autowired
    private ItemDAO itemDAO;

    @Autowired
    private RestaurantItemDAO restaurantItemDAO;

    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantId, String categoryId){
        return null;
    }

    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity){
        List<RestaurantItemEntity> restaurantItemEntities = restaurantItemDAO.getRestaurantAllItems(restaurantEntity);
        return itemDAO.getPopularItemsByRestaurant(restaurantEntity);
    }

    public ItemEntity getItemByUuid(String itemId) throws ItemNotFoundException {
        ItemEntity itemEntity = itemDAO.getItemByUuid(itemId);
        if(itemEntity == null){
            throw new ItemNotFoundException("INF-003", "No item by this id exist");
        }
        return itemEntity;
    }
}
