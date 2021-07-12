package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDAO;
import com.upgrad.FoodOrderingApp.service.dao.OrderDAO;
import com.upgrad.FoodOrderingApp.service.dao.OrderItemDAO;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantItemDAO;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ItemService {

    @Autowired
    private ItemDAO itemDAO;

    @Autowired
    private RestaurantItemDAO restaurantItemDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantId, String categoryId) {
        return itemDAO.getItemsByCategoryAndRestaurant(restaurantId, categoryId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) {
        List<ItemEntity> itemEntityList = new ArrayList<>();
        //To get All Orders from a Restaurant
        for (OrderEntity orderEntity : orderDAO.getOrdersByRestaurant(restaurantEntity)) {
            //To get all Items from that Orders
            for (OrderItemEntity orderItemEntity : orderItemDAO.getItemsByOrder(orderEntity)) {
                itemEntityList.add(orderItemEntity.getItem());
            }
        }

        //Storing that items in a Map with count of Items
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (ItemEntity itemEntity : itemEntityList) {
            Integer count = map.get(itemEntity.getUuid());
            map.put(itemEntity.getUuid(), (count == null) ? 1 : count + 1);
        }

        Map<String, Integer> treeMap = new TreeMap<String, Integer>(map);
        List<ItemEntity> sortedItemEntityList = new ArrayList<ItemEntity>();
        for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
            sortedItemEntityList.add(itemDAO.getItemByUuid(entry.getKey()));
        }
        Collections.reverse(sortedItemEntityList);

        //This is to ensure that only 5 items are returned
        List<ItemEntity> itemEntities = new ArrayList<ItemEntity>();
        if (sortedItemEntityList.size() > 5) {
            for (int i = 0; i < 5; i++) {
                itemEntities.add(sortedItemEntityList.get(i));
            }
        } else {
            itemEntities = sortedItemEntityList;
        }
        return itemEntities;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ItemEntity getItemByUuid(String itemId) throws ItemNotFoundException {
        ItemEntity itemEntity = itemDAO.getItemByUuid(itemId);
        if (itemEntity == null) {
            throw new ItemNotFoundException("INF-003", "No item by this id exist");
        }
        return itemEntity;
    }
}
