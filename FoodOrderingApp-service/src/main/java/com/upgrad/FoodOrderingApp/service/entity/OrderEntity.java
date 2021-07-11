package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "orders")
@NamedQueries({

})
public class OrderEntity {
    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "bill")
    @NotNull
    private Double bill;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    @NotNull
    private CouponEntity coupon;

    @Column(name = "discount")
    @NotNull
    private Double discount;

    @Column(name = "date")
    @NotNull
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    @NotNull
    private PaymentEntity payment;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @NotNull
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @NotNull
    private AddressEntity address;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private RestaurantEntity restaurant;

    public OrderEntity() {
        super();
    }

    public OrderEntity(String uuid, Double bill, CouponEntity coupon, Double discount, Date date, PaymentEntity payment, CustomerEntity customer, AddressEntity address, RestaurantEntity restaurant) {
        super();
        this.uuid = uuid;
        this.bill = bill;
        this.coupon = coupon;
        this.discount = discount;
        this.date = ZonedDateTime.ofInstant(date.toInstant(),
                ZoneId.systemDefault());
        this.payment = payment;
        this.customer = customer;
        this.address = address;
        this.restaurant = restaurant;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Double getBill() {
        return bill;
    }

    public void setBill(Double bill) {
        this.bill = bill;
    }

    public CouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", bill=" + bill +
                ", coupon=" + coupon +
                ", discount=" + discount +
                ", date=" + date +
                ", payment=" + payment +
                ", customer=" + customer +
                ", address=" + address +
                ", restaurant=" + restaurant +
                '}';
    }
}
