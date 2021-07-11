package com.upgrad.FoodOrderingApp.service.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RestaurantCategoryDAO {

    @PersistenceContext
    private EntityManager entityManager;
}
