package com.upgrad.FoodOrderingApp.service.entity;

import com.upgrad.FoodOrderingApp.service.common.ItemType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "item")
@NamedQueries({
        @NamedQuery(name = "itemByUuid", query = "select i from ItemEntity i where i.uuid=:uuid"),
        @NamedQuery(name = "allItemsByCategoryAndRestaurant", query = "select i from ItemEntity i where i.id in (select ri.itemId from RestaurantItemEntity ri inner join CategoryItemEntity ci on ri.itemId = ci.itemId where ri.restaurantId = (select r.id from RestaurantEntity r where r.uuid=:restaurantId) and ci.categoryId = (select c.id from CategoryEntity c where c.uuid=:categoryId )) order by i.itemName asc"),
})
public class ItemEntity implements Serializable {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "item_name")
    @NotNull
    @Size(max = 30)
    private String itemName;

    @Column(name = "price")
    @NotNull
    private Integer price;

    @Column(name = "type")
    @NotNull
    @Size(max = 10)
    private ItemType type;

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ItemEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                '}';
    }
}
