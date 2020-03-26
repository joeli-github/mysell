package com.my.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.my.common.ServerResponse;
import com.my.common.enums.ProductStatusEnum;
import com.my.dao.ProductCategoryDao;
import com.my.dao.ProductInfoDao;
import com.my.pojo.ProductCategory;
import com.my.pojo.ProductInfo;
import com.my.service.ProductInfoService;
import com.my.vo.ProductCategoryVO;
import com.my.vo.ProductVO;
@Service
public class ProductInfoServiceImpl implements ProductInfoService {

	@Autowired
	private ProductInfoDao productInfoDao;
	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Override
	public ProductInfo save(ProductInfo productInfo) {
		
		return productInfoDao.save(productInfo);
	}

	@Override
	public List<ProductInfo> findByProductStatus(Integer productStatus) {
		return productInfoDao.findByProductStatus(productStatus);
	}

	@Override
	public ProductInfo findById(String productId) {
		return productInfoDao.findById(productId).get();
	}

	@Override
	public Page<ProductInfo> findAll(Pageable pageable) {
		return productInfoDao.findAll(pageable);
	}

	@Override
	public ServerResponse<List<ProductCategoryVO>> findUpAll() {
		
		List<ProductCategoryVO> productCategoryVOList = new ArrayList<ProductCategoryVO>();
		List<Integer> categoryTypeList = new ArrayList<Integer>();
		List<ProductInfo> productInfoList = productInfoDao.findByProductStatus(ProductStatusEnum.UP.getCode());
		for (ProductInfo productInfo : productInfoList) {
			categoryTypeList.add(productInfo.getCategoryType());
		}
		List<ProductCategory> productCategoryList = productCategoryDao.findByCategoryTypeIn(categoryTypeList);
		for (ProductCategory productCategory : productCategoryList) {
			ProductCategoryVO productCategoryVO = new ProductCategoryVO();
			List<ProductVO> productVOList = new ArrayList<ProductVO>();
			productCategoryVO.setCategoryName(productCategory.getCategoryName());
			productCategoryVO.setCategoryType(productCategory.getCategoryType());
			for (ProductInfo productInfo : productInfoList) {
				if (productCategory.getCategoryType() == productInfo.getCategoryType()) {
					ProductVO productVO = new ProductVO();
					BeanUtils.copyProperties(productInfo, productVO);
					productVOList.add(productVO);
				}
				productCategoryVO.setProductVOList(productVOList);
			}
			productCategoryVOList.add(productCategoryVO);
		}
		
		
		
		return ServerResponse.createBySuccess("查询所有成功", productCategoryVOList);
		
		
		
		
		
	}

	
	
	
	
	
	
	
	
	
	
	
}
