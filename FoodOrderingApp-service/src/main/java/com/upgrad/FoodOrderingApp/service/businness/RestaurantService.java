package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    public RestaurantEntity restaurantByUUID(String uuid){
        return null;
    }

    public List<RestaurantEntity> restaurantsByName(String restaurantName){
        return null;
    }

    public List<RestaurantEntity> restaurantByCategory(String categoryId){
        return null;
    }

    public List<RestaurantEntity> restaurantsByRating(){
        return null;
    }

    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, double rating){
        return null;
    }

}
