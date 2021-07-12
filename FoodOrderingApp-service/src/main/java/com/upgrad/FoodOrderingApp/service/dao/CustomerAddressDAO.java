package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Repository
public class CustomerAddressDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveCustomerAddress(CustomerAddressEntity customerAddressEntity) {
        entityManager.persist(customerAddressEntity);
    }

        public List<CustomerAddressEntity> getAllAddress(CustomerEntity customerEntity) {
            try {
                return this.entityManager.createNamedQuery("allCustomerAddresses", CustomerAddressEntity.class).setParameter("customer", customerEntity).getResultList();
            }catch (NoResultException nre){
                return Collections.EMPTY_LIST;
            }
        }
}
