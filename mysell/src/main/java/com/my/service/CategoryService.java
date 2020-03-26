package com.my.service;

import java.util.List;

import com.my.pojo.ProductCategory;

public interface CategoryService {

	ProductCategory findById(Integer categoryId);
	
	List<ProductCategory> findAll();
	
	List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
	
	ProductCategory save(ProductCategory productCategory);
	
	
}
