package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {


    public OrderEntity saveOrder(OrderEntity orderEntity){
        return null;
    }

    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity){
        return null;
    }

    public List<OrderEntity> getOrdersByCustomers(String customerId){
        return null;
    }

    public CouponEntity getCouponByCouponId(String couponId){
        return null;
    }

    public CouponEntity getCouponByCouponName(String couponName){
        return null;
    }
}
