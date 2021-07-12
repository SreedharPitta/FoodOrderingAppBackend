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

    public ItemEntity getItemByUuid(String uuid) {
        try {
            return this.entityManager.createNamedQuery("itemByUuid", ItemEntity.class).setParameter("uuid", uuid).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }
}
