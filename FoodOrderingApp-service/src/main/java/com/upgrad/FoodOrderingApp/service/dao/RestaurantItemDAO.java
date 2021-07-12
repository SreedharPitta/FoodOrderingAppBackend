package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantItemDAO {


    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantItemEntity> getRestaurantAllItems(RestaurantEntity restaurantEntity) {
        return null;
    }
}
