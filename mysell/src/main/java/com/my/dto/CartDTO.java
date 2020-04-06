package com.my.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartDTO {

	/**商品ID*/
	private String productId;
	/** 数量 */
	private Integer productQuantity;
	
}
