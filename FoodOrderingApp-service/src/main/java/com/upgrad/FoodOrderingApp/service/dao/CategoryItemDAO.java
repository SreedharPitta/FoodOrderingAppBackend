package com.upgrad.FoodOrderingApp.service.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CategoryItemDAO {


    @PersistenceContext
    private EntityManager entityManager;
}
