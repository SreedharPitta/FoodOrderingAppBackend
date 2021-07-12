package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.common.ItemType;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
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
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;


    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() throws RestaurantNotFoundException {
        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantsByRating();
        RestaurantListResponse restaurantListResponse = getRestaurantListResponse(restaurantEntities);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByName(@PathVariable("restaurant_name") final String restaurantName) throws RestaurantNotFoundException {
        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantsByName(restaurantName);
        RestaurantListResponse restaurantListResponse = getRestaurantListResponse(restaurantEntities);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/category/{category_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByCategoryId(@PathVariable("category_id") final String categoryId) throws RestaurantNotFoundException, CategoryNotFoundException {
        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantByCategory(categoryId);
        RestaurantListResponse restaurantListResponse = getRestaurantListResponse(restaurantEntities);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/{restaurant_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantByRestaurantId(@PathVariable("restaurant_id") final String restaurantId) throws RestaurantNotFoundException {
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);
        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse()
                .id(UUID.fromString(restaurantEntity.getUuid()))
                .restaurantName(restaurantEntity.getRestaurantName())
                .photoURL(restaurantEntity.getPhotoUrl())
                .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                .averagePrice(restaurantEntity.getAvgPrice())
                .numberCustomersRated(restaurantEntity.getNumberCustomersRated())
                .address(getRestaurantDetailsResponseAddress(restaurantEntity.getAddress()))
                .categories(getRestaurantResponseDetailsCategories(restaurantEntity));
        return new ResponseEntity<RestaurantDetailsResponse>(restaurantDetailsResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, path = "/restaurant/{restaurant_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> updateRestaurantDetails(@PathVariable("restaurant_id") final String restaurantId) {
        return null;
    }


    private RestaurantListResponse getRestaurantListResponse(List<RestaurantEntity> restaurantEntities) throws RestaurantNotFoundException {
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();
        List<RestaurantList> restaurantLists = new ArrayList<RestaurantList>();
        for (RestaurantEntity restaurantEntity : restaurantEntities) {
            RestaurantList restaurantList = getRestaurantListItem(restaurantEntity);
            restaurantLists.add(restaurantList);
        }
        restaurantListResponse.restaurants(restaurantLists);
        return restaurantListResponse;
    }


    private RestaurantList getRestaurantListItem(RestaurantEntity restaurantEntity) throws RestaurantNotFoundException {
        RestaurantList restaurantList = new RestaurantList().id(UUID.fromString(restaurantEntity.getUuid()))
                .restaurantName(restaurantEntity.getRestaurantName())
                .photoURL(restaurantEntity.getPhotoUrl())
                .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                .averagePrice(restaurantEntity.getAvgPrice())
                .numberCustomersRated(restaurantEntity.getNumberCustomersRated());
        AddressEntity addressEntity = restaurantEntity.getAddress();
        RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = getRestaurantDetailsResponseAddress(restaurantEntity.getAddress());
        restaurantList.setAddress(restaurantDetailsResponseAddress);
        List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());
        StringBuilder categoriesBuilder = new StringBuilder();
        for (int i = 0; i < categoryEntities.size(); i++) {
            CategoryEntity categoryEntity = categoryEntities.get(i);
            if (i == categoryEntities.size() - 1) {
                categoriesBuilder.append(categoryEntity.getCategoryName());
            } else {
                categoriesBuilder.append(categoryEntity.getCategoryName()).append(", ");
            }
        }
        String categories = categoriesBuilder.toString();
        restaurantList.setCategories(categories);
        return restaurantList;
    }

    private List<CategoryList> getRestaurantResponseDetailsCategories(RestaurantEntity restaurantEntity) throws RestaurantNotFoundException {
        List<CategoryList> categoryLists = new ArrayList<CategoryList>();
        List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());
        for(CategoryEntity categoryEntity : categoryEntities){
            List<ItemList> itemLists = new ArrayList<ItemList>();
            for(ItemEntity item : categoryEntity.getItems()){
                ItemList itemList = new ItemList().id(UUID.fromString(item.getUuid()))
                        .itemName(item.getItemName())
                        .price(item.getPrice());
                if(ItemType.VEG.equals(item.getType())){
                    itemList.setItemType(ItemList.ItemTypeEnum.VEG);
                }else if(ItemType.NON_VEG.equals(item.getType())){
                    itemList.setItemType(ItemList.ItemTypeEnum.NON_VEG);
                }
                itemLists.add(itemList);
            }
            //TODO sort item list based on the Name
            CategoryList categoryList = new CategoryList().id(UUID.fromString(categoryEntity.getUuid()))
                    .categoryName(categoryEntity.getCategoryName()).itemList(itemLists);
            categoryLists.add(categoryList);
        }
        return categoryLists;
    }

    private RestaurantDetailsResponseAddress getRestaurantDetailsResponseAddress(AddressEntity addressEntity) {
        RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                .id(UUID.fromString(addressEntity.getUuid()))
                .flatBuildingName(addressEntity.getFlatBuilNo())
                .locality(addressEntity.getLocality())
                .city(addressEntity.getCity())
                .pincode(addressEntity.getPincode());
        StateEntity stateEntity = addressEntity.getState();
        RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState().id(UUID.fromString(stateEntity.getUuid()))
                .stateName(stateEntity.getStateName());
        restaurantDetailsResponseAddress.setState(restaurantDetailsResponseAddressState);
        return restaurantDetailsResponseAddress;
    }

}
