package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDAO;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    public List<CategoryEntity> getCategoriesByRestaurant(String restaurantId){
        return null;
    }

    public CategoryEntity getCategoryById(String categoryId){
        return null;
    }

    public List<CategoryEntity> getAllCategoriesOrderedByName(){
        return categoryDAO.getAllCategoriesOrderedByName();
    }
}
