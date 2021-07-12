package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDAO;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDAO;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDAO;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDAO restaurantDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private RestaurantCategoryDAO restaurantCategoryDAO;

    //This will get all restaurants in order of their rating
    public List<RestaurantEntity> restaurantsByRating() {
        return  restaurantDAO.getAllRestaurantsByRating();
    }

    public List<RestaurantEntity> restaurantsByName(String restaurantName) throws RestaurantNotFoundException {
        if(restaurantName == null || restaurantName.isEmpty()){
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }
        return restaurantDAO.restaurantsByName(restaurantName);
    }

    public List<RestaurantEntity> restaurantByCategory(String categoryUuid) throws CategoryNotFoundException {
        List<RestaurantEntity> restaurantEntities = new ArrayList<RestaurantEntity>();
        if(categoryUuid == null || categoryUuid.isEmpty()){
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        CategoryEntity categoryEntity = categoryDAO.getCategoryById(categoryUuid);
        if(categoryEntity == null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDAO.getRestaurantByCategory(categoryEntity);
        for(RestaurantCategoryEntity restaurantCategoryEntity : restaurantCategoryEntities){
            restaurantEntities.add(restaurantCategoryEntity.getRestaurant());
        }
        //TODO sort this according to the Name in ascending order
        return restaurantEntities;
    }

    public RestaurantEntity restaurantByUUID(String uuid) throws RestaurantNotFoundException {
        if(uuid == null || uuid.isEmpty()){
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }
        RestaurantEntity restaurantEntity = restaurantDAO.getRestaurantByUUID(uuid);
        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        return restaurantEntity;
    }

    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, double rating) {
        return null;
    }

}
