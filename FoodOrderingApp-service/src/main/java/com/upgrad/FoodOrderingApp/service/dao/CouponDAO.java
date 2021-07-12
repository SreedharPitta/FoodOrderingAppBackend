package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CouponDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public CouponEntity getCouponByName(final String couponName) {
        try {
            return this.entityManager.createNamedQuery("couponByName", CouponEntity.class).setParameter("couponName", couponName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CouponEntity getCouponByUUID(String uuid) {
        try {
            return this.entityManager.createNamedQuery("couponByUuid", CouponEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
