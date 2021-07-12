package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Repository
public class ItemDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ItemEntity> getPopularItemsByRestaurant(RestaurantEntity restaurantEntity) {
        try{
            return null;
        }catch (NoResultException nre){
            return Collections.emptyList();
        }
    }
}
