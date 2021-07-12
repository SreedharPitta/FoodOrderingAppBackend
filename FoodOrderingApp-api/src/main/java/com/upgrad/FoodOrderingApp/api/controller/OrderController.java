package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.common.ItemType;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
        final String accessToken = authorizationToken.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        CouponEntity couponEntity = orderService.getCouponByCouponName(couponName);
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse().couponName(couponEntity.getCouponName()).id(UUID.fromString(couponEntity.getUuid())).percent(couponEntity.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CustomerOrderResponse> getUserPastOrders(@RequestHeader("authorization") final String authorizationToken) throws AuthorizationFailedException {
        final String accessToken = authorizationToken.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        List<OrderEntity> orderEntities = orderService.getOrdersByCustomers(customerEntity.getUuid());
        List<OrderList> orders = new ArrayList<OrderList>();
        for(OrderEntity orderEntity : orderEntities){
            OrderList order = new OrderList().id(UUID.fromString(orderEntity.getUuid()))
                    .date(orderEntity.getDate().toString()).bill(BigDecimal.valueOf(orderEntity.getBill()))
                    .discount(BigDecimal.valueOf(orderEntity.getDiscount()))
                    .customer(getOrderCustomer(orderEntity.getCustomer()))
                    .coupon(getOrderCoupon(orderEntity.getCoupon()))
                    .address(getOrderAddress(orderEntity.getAddress()))
                    .payment(getOrderPayment(orderEntity.getPayment()))
                    .itemQuantities(getOrderItemQuantities(orderEntity.getOrderItems()));
            orders.add(order);
        }
        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse().orders(orders);
        return new ResponseEntity<CustomerOrderResponse>(customerOrderResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> saveOrder() {
        return null;
    }


    private List<ItemQuantityResponse> getOrderItemQuantities(List<OrderItemEntity> orderItems) {
        List<ItemQuantityResponse> itemQuantityResponses = new ArrayList<ItemQuantityResponse>();
        for(OrderItemEntity orderItem : orderItems){
            ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem()
                    .id(UUID.fromString(orderItem.getItem().getUuid()))
                    .itemName(orderItem.getItem().getItemName())
                    .itemPrice(orderItem.getItem().getPrice());
            if(ItemType.VEG.equals(orderItem.getItem().getType())){
               itemQuantityResponseItem.setType(ItemQuantityResponseItem.TypeEnum.VEG);
            }else if(ItemType.NON_VEG.equals(orderItem.getItem().getType())){
                itemQuantityResponseItem.setType(ItemQuantityResponseItem.TypeEnum.NON_VEG);
            }
            ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse().item(itemQuantityResponseItem)
                    .quantity(orderItem.getQuantity())
                    .price(orderItem.getPrice());
        itemQuantityResponses.add(itemQuantityResponse);
        }
        return itemQuantityResponses;
    }

    private OrderListPayment getOrderPayment(PaymentEntity payment) {
        OrderListPayment orderListPayment = new OrderListPayment().id(UUID.fromString(payment.getUuid()))
                .paymentName(payment.getPaymentName());
        return orderListPayment;
    }

    private OrderListAddress getOrderAddress(AddressEntity address) {
        OrderListAddress orderListAddress = new OrderListAddress()
                .id(UUID.fromString(address.getUuid()))
                .flatBuildingName(address.getFlatBuilNo())
                .locality(address.getLocality())
                .city(address.getCity())
                .pincode(address.getPincode());
        OrderListAddressState orderListAddressState = new OrderListAddressState().id(UUID.fromString(address.getState().getUuid()))
                .stateName(address.getState().getStateName());
        orderListAddress.setState(orderListAddressState);
        return orderListAddress;
    }

    private OrderListCoupon getOrderCoupon(CouponEntity coupon) {
        OrderListCoupon orderListCoupon = new OrderListCoupon().id(UUID.fromString(coupon.getUuid())).couponName(coupon.getCouponName())
                .percent(coupon.getPercent());
        return orderListCoupon;
    }

    private OrderListCustomer getOrderCustomer(CustomerEntity customer) {
        OrderListCustomer orderListCustomer = new OrderListCustomer().id(UUID.fromString(customer.getUuid()))
                .firstName(customer.getFirstName()).lastName(customer.getLastName()).emailAddress(customer.getEmail())
                .contactNumber(customer.getContactNumber());
                

                return orderListCustomer;
    }
}
