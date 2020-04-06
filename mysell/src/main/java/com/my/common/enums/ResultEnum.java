package com.my.common.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

	PRODUCT_NOT_EXIST(10,"商品不存在"),
	PRODUCT_STOCK_ERROR(11,"商品库存错误"),
	ORDER_NOT_EXIST(12,"订单不存在"),
	ORDERDETAIL_NOT_EXIST(13,"订单详情不存在"),
	ORDER_STATUS_ERROR(14,"订单状态错误"),
	ORDER_UPDATE_FAIL(15,"更新订单失败"),
	
	;
	private Integer code;
	private String message;
	private ResultEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	
}
