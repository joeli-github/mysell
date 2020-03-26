package com.my.utils;

import com.my.common.enums.CodeEnum;

public class EnumUtil {

	public static <T extends CodeEnum> T getMsgByCode(Integer code,Class<T> enumClass) {
		
		for (T enumT : enumClass.getEnumConstants()) {
			if (code.equals(enumT.getCode())) {
				return enumT;
			}
		}
		return null;
	}
}
