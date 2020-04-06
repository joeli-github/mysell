package com.my.exception;

import com.my.common.enums.ResultEnum;

import lombok.Getter;
@Getter
public class MySellException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer code;
	public MySellException(ResultEnum resultEnum) {
		super(resultEnum.getMessage());
		this.code = resultEnum.getCode();
	}

}
