package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDAO;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDAO;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private RestaurantCategoryDAO restaurantCategoryDAO;

    @Autowired
    private RestaurantService restaurantService;

    public List<CategoryEntity> getCategoriesByRestaurant(String restaurantId) throws RestaurantNotFoundException {
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDAO.getCategoriesByRestaurant(restaurantEntity);
        List<CategoryEntity> categoryEntities = new ArrayList<CategoryEntity>();
        for(RestaurantCategoryEntity restaurantCategoryEntity : restaurantCategoryEntities){
                categoryEntities.add(restaurantCategoryEntity.getCategory());
        }
        //To Do sort this in the order of Category Name
        return categoryEntities;
    }

    public CategoryEntity getCategoryById(String categoryUuid) throws CategoryNotFoundException {
        if (categoryUuid == null || categoryUuid.isEmpty()) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        CategoryEntity categoryEntity = categoryDAO.getCategoryById(categoryUuid);
        if (categoryEntity == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        return categoryEntity;
    }

    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        return categoryDAO.getAllCategoriesOrderedByName();
    }
}
