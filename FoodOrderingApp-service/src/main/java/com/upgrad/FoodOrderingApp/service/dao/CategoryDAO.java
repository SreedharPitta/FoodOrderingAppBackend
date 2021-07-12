package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Repository
public class CategoryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        try {
            return this.entityManager.createNamedQuery("allCategoriesOrderedByName", CategoryEntity.class).getResultList();
        } catch (NoResultException nre) {
            return Collections.EMPTY_LIST;
        }
    }

    public CategoryEntity getCategoryById(String uuid) {
        try {
            return this.entityManager.createNamedQuery("categoryByUuid", CategoryEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
