package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/category", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryService.getAllCategoriesOrderedByName();
        List<CategoryListResponse> categoryListResponses = new ArrayList<CategoryListResponse>();
        for(CategoryEntity categoryEntity : categoryEntities){
            CategoryListResponse categoryListResponse = new CategoryListResponse().id(UUID.fromString(categoryEntity.getUuid()))
            .categoryName(categoryEntity.getCategoryName());
            categoryListResponses.add(categoryListResponse);
        }
        CategoriesListResponse categoriesListResponse = new CategoriesListResponse().categories(categoryListResponses);
        return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryListResponse> getAllCategoryById(@PathVariable("category_id") final String categoryId){
        return null;
    }

}
