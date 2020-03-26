package com.my.service;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.common.ServerResponse;
import com.my.dto.OrderDTO;

public interface OrderMasterService {

	public ServerResponse<Page<OrderDTO>> findOrderAll(@RequestParam(value = "page",defaultValue = "1")Integer page,
			@RequestParam(value = "size",defaultValue = "10")Integer size);
}
