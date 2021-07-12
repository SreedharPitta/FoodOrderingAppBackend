package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "restaurant_item")
public class RestaurantItemEntity implements Serializable {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id")
    @NotNull
    private Integer itemId;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "RestaurantItemEntity{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
