package com.my.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayStatusEnum implements CodeEnum{

	WAIT(0,"等待支付"),
	SUCCESS(1,"支付成功"),
	;
	private Integer code;
	private String message;

}
