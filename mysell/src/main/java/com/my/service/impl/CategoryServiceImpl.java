package com.my.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.dao.ProductCategoryDao;
import com.my.pojo.ProductCategory;
import com.my.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Override
	public ProductCategory findById(Integer categoryId) {
		return productCategoryDao.findById(categoryId).get();
	}

	@Override
	public List<ProductCategory> findAll() {
		return productCategoryDao.findAll();
	}

	@Override
	public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
		return productCategoryDao.findByCategoryTypeIn(categoryTypeList);
	}

	@Override
	public ProductCategory save(ProductCategory productCategory) {
		return productCategoryDao.save(productCategory);
	}

}
