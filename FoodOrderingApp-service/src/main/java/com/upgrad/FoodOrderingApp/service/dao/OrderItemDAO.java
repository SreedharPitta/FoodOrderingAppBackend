package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class OrderItemDAO {
    @Autowired
    private EntityManager entityManager;


    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {
        try {
            entityManager.persist(orderItemEntity);
            return orderItemEntity;
        } catch (Exception e) {
            return null;
        }
    }
}
