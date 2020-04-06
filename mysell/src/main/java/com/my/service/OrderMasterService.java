package com.my.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.common.ServerResponse;
import com.my.dto.OrderDTO;

public interface OrderMasterService {

	public ServerResponse<Page<OrderDTO>> findOrderAll(@RequestParam(value = "page",defaultValue = "1")Integer page,
			@RequestParam(value = "size",defaultValue = "10")Integer size);
	
	/** 创建订单. */
	ServerResponse<OrderDTO> create(OrderDTO orderDTO);
	
	/** 查询单个订单 */
	ServerResponse<OrderDTO> findOne(String orderId);
	
	/** 查询订单列表 */
	ServerResponse<Page<OrderDTO>> findList(String buyerOpenid,Pageable pageable);
	
	/** 取消订单 */
	ServerResponse<OrderDTO> cancel(OrderDTO orderDTO);
	
	/** 完结订单 */
	ServerResponse<OrderDTO> finish(OrderDTO orderDTO);
	
	/** 支付订单 */
	ServerResponse<OrderDTO> paid(OrderDTO orderDTO);
	
	
	
}
