package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantId, String categoryId){
        return null;
    }

    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity){
        return null;
    }
}
