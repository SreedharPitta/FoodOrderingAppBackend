package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDAO;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDAO;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDAO restaurantDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    //This will get all restaurants in order of their rating
    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> restaurantsByRating() {
        return restaurantDAO.getAllRestaurantsByRating();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> restaurantsByName(String restaurantName) throws RestaurantNotFoundException {
        if (restaurantName == null || restaurantName.isEmpty()) {
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }
        return restaurantDAO.restaurantsByName(restaurantName);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> restaurantByCategory(String categoryUuid) throws CategoryNotFoundException {
        if (categoryUuid == null || categoryUuid.isEmpty()) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        CategoryEntity categoryEntity = categoryDAO.getCategoryById(categoryUuid);
        if (categoryEntity == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        List<RestaurantEntity> restaurantEntities = restaurantDAO.restaurantByCategory(categoryUuid);
        return restaurantEntities;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity restaurantByUUID(String uuid) throws RestaurantNotFoundException {
        if (uuid == null || uuid.isEmpty()) {
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }
        RestaurantEntity restaurantEntity = restaurantDAO.getRestaurantByUUID(uuid);
        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        return restaurantEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(final RestaurantEntity restaurantEntity, final Double customerRating) throws InvalidRatingException {
        if (!customerRating.isNaN() && !(customerRating < 1) && !(customerRating > 5)) {
            Double oldRating = restaurantEntity.getCustomerRating();
            Integer numberCustomersRated = restaurantEntity.getNumberCustomersRated();
            Double newRating = ((oldRating * numberCustomersRated) + customerRating) / (numberCustomersRated + 1);
            restaurantEntity.setCustomerRating(newRating);
            restaurantEntity.setNumberCustomersRated(restaurantEntity.getNumberCustomersRated() + 1);
        } else {
            throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
        }
        return restaurantDAO.updateRestaurant(restaurantEntity);
    }
}
