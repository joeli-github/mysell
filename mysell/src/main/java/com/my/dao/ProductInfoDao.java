package com.my.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.pojo.ProductInfo;

public interface ProductInfoDao extends JpaRepository<ProductInfo, String> {
	List<ProductInfo> findByProductStatus(Integer productStatus);

}
