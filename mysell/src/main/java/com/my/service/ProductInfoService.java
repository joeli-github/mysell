package com.my.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.my.common.ServerResponse;
import com.my.dto.CartDTO;
import com.my.pojo.ProductInfo;
import com.my.vo.ProductCategoryVO;

public interface ProductInfoService {
	ProductInfo findById(String productId);
	
	Page<ProductInfo> findAll(Pageable pageable);
	/**
	 * 查询所有上架商品
	 * @return
	 */
	ServerResponse<List<ProductCategoryVO>> findUpAll();

	ProductInfo save(ProductInfo productInfo);
	
	List<ProductInfo> findByProductStatus(Integer productStatus);
	
	//减库存
	void decreaseStock(List<CartDTO> cartDTOList);
	
	//加库存
	void increaseStock(List<CartDTO> cartDTOList);
}
