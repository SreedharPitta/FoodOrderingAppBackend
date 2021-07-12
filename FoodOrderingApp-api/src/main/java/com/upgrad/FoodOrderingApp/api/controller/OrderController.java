package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByCouponName(@RequestHeader("authorization") final String authorizationToken, @PathVariable("coupon_name") final String couponName) throws AuthorizationFailedException, CouponNotFoundException {
        String accessToken = authorizationToken.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        CouponEntity couponEntity = orderService.getCouponByCouponName(couponName);
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse().couponName(couponEntity.getCouponName()).id(UUID.fromString(couponEntity.getUuid())).percent(couponEntity.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getUserPastOrders() {
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> saveOrder() {
        return null;
    }
}
