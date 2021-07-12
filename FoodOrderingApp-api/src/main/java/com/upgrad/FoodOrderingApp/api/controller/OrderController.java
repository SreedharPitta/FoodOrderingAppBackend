package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.common.ItemType;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
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

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RestaurantService restaurantService;

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
        for (OrderEntity orderEntity : orderEntities) {
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
    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestHeader("authorization") final String authorizationToken, @RequestBody(required = false) final SaveOrderRequest saveOrderRequest) throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {
        final String accessToken = authorizationToken.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        CouponEntity couponEntity = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());
        PaymentEntity paymentEntity = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());
        AddressEntity addressEntity = addressService.getAddressByUUID(saveOrderRequest.getAddressId(), customerEntity);
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(saveOrderRequest.getRestaurantId().toString());

        List<OrderItemEntity> orderItemEntities = new ArrayList<OrderItemEntity>();
        for (ItemQuantity itemQuantity : saveOrderRequest.getItemQuantities()) {
            ItemEntity itemEntity = itemService.getItemByUuid(itemQuantity.getItemId().toString());
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setItem(itemEntity);
            orderItemEntity.setPrice(itemQuantity.getPrice());
            orderItemEntity.setQuantity(itemQuantity.getQuantity());
            orderItemEntities.add(orderItemEntity);
        }
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderItems(orderItemEntities);
        orderEntity.setUuid(UUID.randomUUID().toString());
        orderEntity.setBill(saveOrderRequest.getBill().doubleValue());
        orderEntity.setDiscount(saveOrderRequest.getDiscount().doubleValue());
        orderEntity.setAddress(addressEntity);
        orderEntity.setCoupon(couponEntity);
        orderEntity.setDate(ZonedDateTime.now());
        orderEntity.setPayment(paymentEntity);
        orderEntity.setCustomer(customerEntity);
        orderEntity.setRestaurant(restaurantEntity);
        OrderEntity updatedOrderEntity = orderService.saveOrder(orderEntity);
        //To save the Order Item Entities
        if (updatedOrderEntity != null) {
            for (OrderItemEntity orderItemEntity : orderItemEntities) {
                orderItemEntity.setOrder(updatedOrderEntity);
                orderService.saveOrderItem(orderItemEntity);
            }
        }
        SaveOrderResponse saveOrderResponse = new SaveOrderResponse().id(updatedOrderEntity.getUuid()).status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse, HttpStatus.CREATED);
    }


    private List<ItemQuantityResponse> getOrderItemQuantities(List<OrderItemEntity> orderItems) {
        List<ItemQuantityResponse> itemQuantityResponses = new ArrayList<ItemQuantityResponse>();
        for (OrderItemEntity orderItem : orderItems) {
            ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem()
                    .id(UUID.fromString(orderItem.getItem().getUuid()))
                    .itemName(orderItem.getItem().getItemName())
                    .itemPrice(orderItem.getItem().getPrice());
            if (ItemType.VEG.equals(orderItem.getItem().getType())) {
                itemQuantityResponseItem.setType(ItemQuantityResponseItem.TypeEnum.VEG);
            } else if (ItemType.NON_VEG.equals(orderItem.getItem().getType())) {
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
