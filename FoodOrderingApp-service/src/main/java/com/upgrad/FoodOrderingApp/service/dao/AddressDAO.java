package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Repository
public class AddressDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public AddressEntity saveAddress(AddressEntity addressEntity) {
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
        return addressEntity;
    }

    public AddressEntity getAddressByUuid(String uuid) {
        try{
           return this.entityManager.createNamedQuery("addressByUuid", AddressEntity.class).setParameter("uuid", uuid).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public AddressEntity updateAddress(AddressEntity addressEntity) {
        return entityManager.merge(addressEntity);
    }
}
