package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDAO;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDAO restaurantDAO;

    //This will get all restaurants in order of their rating
    public List<RestaurantEntity> getAllRestaurantsByRating() {
        return  restaurantDAO.getAllRestaurantsByRating();
    }

    public RestaurantEntity restaurantByUUID(String uuid) throws RestaurantNotFoundException {
        RestaurantEntity restaurantEntity = restaurantDAO.getRestaurantByUUID(uuid);
        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        return restaurantEntity;
    }

    public List<RestaurantEntity> restaurantsByName(String restaurantName) {
        return null;
    }

    public List<RestaurantEntity> restaurantByCategory(String categoryId) {
        return null;
    }

    public List<RestaurantEntity> restaurantsByRating() {
        return null;
    }

    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, double rating) {
        return null;
    }

}
