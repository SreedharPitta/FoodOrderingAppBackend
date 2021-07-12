package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Repository
public class RestaurantDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public RestaurantEntity getRestaurantByUUID(String uuid) {
        try {
            return this.entityManager.createNamedQuery("restaurantByUUID", RestaurantEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }

    public List<RestaurantEntity> getAllRestaurantsByRating() {
        try {
            return this.entityManager.createNamedQuery("allRestaurantsByRating", RestaurantEntity.class).getResultList();
        }catch (NoResultException nre){
            return Collections.EMPTY_LIST;
        }
    }

    public List<RestaurantEntity> restaurantsByName(String searchName) {
        try{
            return this.entityManager.createNamedQuery("restaurantsByName", RestaurantEntity.class).setParameter("searchName", "%" + searchName + "%").getResultList();
        }catch (NoResultException nre){
            return Collections.emptyList();
        }
    }
}
