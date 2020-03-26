package com.my.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.pojo.ProductCategory;

public interface ProductCategoryDao extends JpaRepository<ProductCategory, Integer> {

	List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
