package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;

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

    public List<OrderItemEntity> getItemsByOrder(OrderEntity orderEntity) {
        try {
            return this.entityManager.createNamedQuery("itemsByOrder", OrderItemEntity.class).setParameter("order", orderEntity).getResultList();
        } catch (NoResultException nre) {
            return Collections.emptyList();
        }
    }
}
