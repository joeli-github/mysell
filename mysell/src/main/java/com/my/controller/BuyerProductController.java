package com.my.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.common.ServerResponse;
import com.my.service.ProductInfoService;
import com.my.vo.ProductCategoryVO;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

	@Autowired
	private ProductInfoService productInfoService;
	@GetMapping("/list")
	public ServerResponse<List<ProductCategoryVO>> list() {
		
		return productInfoService.findUpAll();
	}
}
