package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;

@Repository
public class OrderDAO {

    @Autowired
    private EntityManager entityManager;


    public List<OrderEntity> getOrdersByCustomers(CustomerEntity customer) {
        try {
            return this.entityManager.createNamedQuery("ordersByCustomers", OrderEntity.class).setParameter("customer", customer).getResultList();
        } catch (NoResultException nre) {
            return Collections.emptyList();
        }

    }

    public List<OrderEntity> getOrdersByAddress(AddressEntity addressEntity) {
        try {
            return this.entityManager.createNamedQuery("ordersByAddress", OrderEntity.class).setParameter("address", addressEntity).getResultList();
        } catch (NoResultException nre) {
            return Collections.emptyList();
        }
    }

    public OrderEntity saveOrder(OrderEntity orderEntity) {
        try {
            entityManager.persist(orderEntity);
            return orderEntity;
        } catch (Exception e) {
            return null;
        }
    }
}
