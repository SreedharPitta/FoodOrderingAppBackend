package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDAO;
import com.upgrad.FoodOrderingApp.service.dao.OrderDAO;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CouponDAO couponDAO;

    @Autowired
    private OrderDAO orderDAO;

    public OrderEntity saveOrder(OrderEntity orderEntity) {
        return null;
    }

    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {
        return null;
    }

    public CouponEntity getCouponByCouponId(String couponId) {
        return null;
    }

    //To get Coupon by Name
    public CouponEntity getCouponByCouponName(String couponName) throws CouponNotFoundException {
        if (couponName == null || couponName.isEmpty()) {
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
        }
        CouponEntity couponEntity = couponDAO.getCouponByName(couponName);
        if (couponEntity == null) {
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }
        return couponEntity;
    }

    //To get Customer Orders
    public List<OrderEntity> getOrdersByCustomers(String customerUuid) {
        CustomerEntity customerEntity = customerService.getCustomerByUuid(customerUuid);
        return orderDAO.getOrdersByCustomers(customerEntity);
    }
}
