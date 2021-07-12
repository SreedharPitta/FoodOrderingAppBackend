package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Repository
public class StateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public StateEntity getStateByUUID(String uuid) {
        return null;
    }

    public List<StateEntity> getAllStates() {
        try {
            return this.entityManager.createNamedQuery("allStates", StateEntity.class).getResultList();
        } catch (NoResultException nre) {
            return Collections.emptyList();
        }
    }
}
