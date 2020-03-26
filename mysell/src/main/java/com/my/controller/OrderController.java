package com.my.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.my.common.ServerResponse;
import com.my.dto.OrderDTO;
import com.my.service.OrderMasterService;

@RestController
@RequestMapping("/manager/order")
public class OrderController {

	@Autowired
	private OrderMasterService orderMasterService;
	/**
	 * 后台订单列表
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("/findorderall")
	public ServerResponse<Page<OrderDTO>> findOrderAll(@RequestParam(value = "page",defaultValue = "1")Integer page,
															@RequestParam(value = "size",defaultValue = "10")Integer size){
		return orderMasterService.findOrderAll(page, size);
	}
}
