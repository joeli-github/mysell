package com.my.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
 
@Data
public class ProductCategoryVO {

	@JsonProperty("name")
	private String categoryName;
	
	@JsonProperty("type")
	private Integer categoryType;
	
	@JsonProperty("shop")
	private List<ProductVO> productVOList;
}