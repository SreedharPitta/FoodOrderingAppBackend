package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Repository
public class RestaurantCategoryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantCategoryEntity> getCategoriesByRestaurant(RestaurantEntity restaurant) {
        try {
            return this.entityManager.createNamedQuery("categoriesByRestaurant", RestaurantCategoryEntity.class).setParameter("restaurant", restaurant).getResultList();
        } catch (NoResultException nre) {
            return Collections.emptyList();
        }

    }
}
