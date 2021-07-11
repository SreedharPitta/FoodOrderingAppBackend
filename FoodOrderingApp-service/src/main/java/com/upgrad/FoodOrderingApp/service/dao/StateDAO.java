package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class StateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public StateEntity getStateByUUID(String uuid) {
        return null;
    }
}
