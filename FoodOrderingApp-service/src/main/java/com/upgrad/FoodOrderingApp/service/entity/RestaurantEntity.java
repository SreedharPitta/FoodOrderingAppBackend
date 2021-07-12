package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "restaurant")
@NamedQueries({
        @NamedQuery(name = "restaurantByUUID", query = "select r from RestaurantEntity r where r.uuid=:uuid"),
        @NamedQuery(name = "allRestaurantsByRating", query = "select r from RestaurantEntity r order by r.customerRating desc"),
        @NamedQuery(name = "restaurantsByName", query = "select r from RestaurantEntity r where lower(r.restaurantName) like lower(:searchName) order by r.restaurantName asc"),
        @NamedQuery(name = "restaurantByCategory", query = "select r from RestaurantEntity r where r.id in (select rc.restaurantId from RestaurantCategoryEntity rc where rc.categoryId = (select c.id from CategoryEntity c where c.uuid=:categoryUuid) ) order by r.restaurantName")
})
public class RestaurantEntity implements Serializable {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "restaurant_name")
    @NotNull
    @Size(max = 50)
    private String restaurantName;


    @Column(name = "photo_url")
    @NotNull
    @Size(max = 255)
    private String photoUrl;


    @Column(name = "customer_rating")
    @NotNull
    private Double customerRating;


    @Column(name = "average_price_for_two")
    @NotNull
    private Integer avgPrice;


    @Column(name = "number_of_customers_rated")
    @NotNull
    private Integer numberCustomersRated;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private AddressEntity address;

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

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Double getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Double customerRating) {
        this.customerRating = customerRating;
    }

    public Integer getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Integer avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Integer getNumberCustomersRated() {
        return numberCustomersRated;
    }

    public void setNumberCustomersRated(Integer numberCustomersRated) {
        this.numberCustomersRated = numberCustomersRated;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "RestaurantEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", customerRating=" + customerRating +
                ", avgPrice=" + avgPrice +
                ", numberCustomersRated=" + numberCustomersRated +
                ", address=" + address +
                '}';
    }
}
