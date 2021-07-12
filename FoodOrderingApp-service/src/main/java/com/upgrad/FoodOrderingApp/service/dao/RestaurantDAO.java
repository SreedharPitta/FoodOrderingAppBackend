package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Repository
public class RestaurantDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public RestaurantEntity getRestaurantByUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("restaurantByUUID", RestaurantEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }

    public List<RestaurantEntity> restaurantByCategory(String categoryUuid) {
        try{
            return entityManager
                    .createNamedQuery("restaurantByCategory", RestaurantEntity.class)
                    .setParameter("categoryUuid", categoryUuid)
                    .getResultList();
        }catch (NoResultException nre){
            return Collections.emptyList();
        }
    }

    public List<RestaurantEntity> getAllRestaurantsByRating() {
        try {
            return entityManager.createNamedQuery("allRestaurantsByRating", RestaurantEntity.class).getResultList();
        }catch (NoResultException nre){
            return Collections.EMPTY_LIST;
        }
    }

    public List<RestaurantEntity> restaurantsByName(String searchName) {
        try{
            return entityManager.createNamedQuery("restaurantsByName", RestaurantEntity.class).setParameter("searchName", "%" + searchName + "%").getResultList();
        }catch (NoResultException nre){
            return Collections.emptyList();
        }
    }

    public RestaurantEntity updateRestaurant(final RestaurantEntity restaurantEntity) {
        RestaurantEntity updatedRestaurantEntity = entityManager.merge(restaurantEntity);
        return updatedRestaurantEntity;
    }
}
