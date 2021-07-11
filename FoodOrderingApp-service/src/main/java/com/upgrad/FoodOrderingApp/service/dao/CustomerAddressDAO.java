package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAddressDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveCustomerAddress(CustomerAddressEntity customerAddressEntity) {
    }
}
