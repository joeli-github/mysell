package com.my.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.common.ServerResponse;
import com.my.common.enums.ProductStatusEnum;
import com.my.common.enums.ResultEnum;
import com.my.dao.ProductCategoryDao;
import com.my.dao.ProductInfoDao;
import com.my.dto.CartDTO;
import com.my.exception.MySellException;
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

	/**查询所有上架商品及分类	 */
	@Override
	public ServerResponse<List<ProductCategoryVO>> findUpAll() {
		
		List<ProductCategoryVO> productCategoryVOList = new ArrayList<ProductCategoryVO>();
		List<Integer> categoryTypeList = new ArrayList<Integer>();
		List<ProductInfo> productInfoList = productInfoDao.findByProductStatus(ProductStatusEnum.UP.getCode());
		for (ProductInfo productInfo : productInfoList) {
			categoryTypeList.add(productInfo.getCategoryType());
		}
		//精简方法java8,lambda
//		List<Integer> categoryTypeList = productInfoList.stream()
//				.map(e -> e.getCategoryType())
//				.collect(Collectors.toList());
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
			}
			productCategoryVO.setProductVOList(productVOList);
			productCategoryVOList.add(productCategoryVO);
		}
		
		
		
		return ServerResponse.createBySuccess("查询所有成功", productCategoryVOList);
		
		
		
		
		
	}

	@Override
	@Transactional
	public void decreaseStock(List<CartDTO> cartDTOList) {

		for (CartDTO cartDTO : cartDTOList) {
			ProductInfo productInfo = productInfoDao.findById(cartDTO.getProductId()).get();
			if (productInfo==null) {
				throw new MySellException(ResultEnum.PRODUCT_NOT_EXIST);
				
			}
			Integer result = productInfo.getProductStock()-cartDTO.getProductQuantity();
			if (result<0) {
				throw new MySellException(ResultEnum.PRODUCT_STOCK_ERROR);
				
			}
			productInfo.setProductStock(result);
			productInfoDao.save(productInfo);
		}
	}

	@Override
	@Transactional
	public void increaseStock(List<CartDTO> cartDTOList) {
		for (CartDTO cartDTO : cartDTOList) {
			ProductInfo productInfo = productInfoDao.findById(cartDTO.getProductId()).get();
			if (productInfo==null) {
				throw new MySellException(ResultEnum.PRODUCT_NOT_EXIST);
				
			}
			Integer result = productInfo.getProductStock()+cartDTO.getProductQuantity();
			productInfo.setProductStock(result);
			productInfoDao.save(productInfo);
		}
	}

	
	
	
	
	
	
	
	
	
	
	
}
