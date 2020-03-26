package com.my.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.my.common.enums.OrderStatusEnum;
import com.my.common.enums.PayStatusEnum;
import com.my.pojo.OrderDetail;
import com.my.utils.EnumUtil;

import lombok.Data;
@Data
public class OrderDTO {

	private String orderId;

    private String buyerName;

    private String buyerPhone;

    private String buyerAddress;

    private String buyerOpenid;

    private BigDecimal orderAmount;

    private Integer orderStatus;

    private Integer payStatus;

    private Date createTime;

    private Date updateTime;

    private List<OrderDetail> orderDetailList;
    
    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
		return EnumUtil.getMsgByCode(orderStatus, OrderStatusEnum.class);
	}
    @JsonIgnore
    public PayStatusEnum getPayStatusEnum() {
    	return EnumUtil.getMsgByCode(payStatus, PayStatusEnum.class);
    }
}
