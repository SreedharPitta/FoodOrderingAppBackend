package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDAO;
import com.upgrad.FoodOrderingApp.service.dao.OrderDAO;
import com.upgrad.FoodOrderingApp.service.dao.OrderItemDAO;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CouponDAO couponDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderEntity saveOrder(OrderEntity orderEntity) {
        return orderDAO.saveOrder(orderEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {
        return orderItemDAO.saveOrderItem(orderItemEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByCouponId(String couponId) throws CouponNotFoundException {
        CouponEntity couponEntity = couponDAO.getCouponByUUID(couponId);
        if (couponEntity == null) {
            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        }
        return couponEntity;
    }

    //To get Coupon by Name
    @Transactional(propagation = Propagation.REQUIRED)
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
    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrderEntity> getOrdersByCustomers(String customerUuid) {
        CustomerEntity customerEntity = customerService.getCustomerByUuid(customerUuid);
        return orderDAO.getOrdersByCustomers(customerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrderEntity> getOrdersByAddress(AddressEntity addressEntity) {
        return orderDAO.getOrdersByAddress(addressEntity);
    }
}
