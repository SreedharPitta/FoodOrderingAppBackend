package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.common.ItemType;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
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
        List<CategoryListResponse> categoryListResponses = null;
        if (!categoryEntities.isEmpty()) {
            categoryListResponses = new ArrayList<CategoryListResponse>();
            for (CategoryEntity categoryEntity : categoryEntities) {
                CategoryListResponse categoryListResponse = new CategoryListResponse().id(UUID.fromString(categoryEntity.getUuid()))
                        .categoryName(categoryEntity.getCategoryName());
                categoryListResponses.add(categoryListResponse);
            }
        }
        CategoriesListResponse categoriesListResponse = new CategoriesListResponse().categories(categoryListResponses);
        return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getAllCategoryById(@PathVariable("category_id") final String categoryUuid) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryService.getCategoryById(categoryUuid);
        List<ItemList> itemLists = new ArrayList<ItemList>();
        for (ItemEntity itemEntity : categoryEntity.getItems()) {
            ItemList itemList = new ItemList().id(UUID.fromString(itemEntity.getUuid())).itemName(itemEntity.getItemName())
                    .price(itemEntity.getPrice());
            if (ItemType.VEG.equals(itemEntity.getType())) {
                itemList.setItemType(ItemList.ItemTypeEnum.VEG);
            } else if (ItemType.NON_VEG.equals(itemEntity.getType())) {
                itemList.setItemType(ItemList.ItemTypeEnum.NON_VEG);
            }
            itemLists.add(itemList);
        }
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse().id(UUID.fromString(categoryEntity.getUuid()))
                .categoryName(categoryEntity.getCategoryName()).itemList(itemLists);
        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);
    }
}
