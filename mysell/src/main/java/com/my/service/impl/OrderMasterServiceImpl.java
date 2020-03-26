package com.my.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.common.ServerResponse;
import com.my.dao.OrderDetailDao;
import com.my.dao.OrderMasterDao;
import com.my.dto.OrderDTO;
import com.my.pojo.OrderDetail;
import com.my.pojo.OrderMaster;
import com.my.service.OrderMasterService;
@Service
public class OrderMasterServiceImpl implements OrderMasterService {

	@Autowired
	private OrderMasterDao orderMasterDao;
	@Autowired
	private OrderDetailDao orderDetailDao;
	public ServerResponse<Page<OrderDTO>> findOrderAll(@RequestParam(value = "page",defaultValue = "1")Integer page,
															@RequestParam(value = "size",defaultValue = "10")Integer size) {
		
		
		PageRequest request = PageRequest.of(page-1, size);
		Page<OrderMaster> orderMasterList=orderMasterDao.findAll(request);
		if (orderMasterList.isEmpty()||orderMasterList.getSize()==0) {
			return ServerResponse.createByErrorMessage("当前还没有订单");
		}
		List<OrderDTO> orderDtoList = this.masterListToDTO(orderMasterList);
		Page<OrderDTO> orderListPage = new PageImpl<>(orderDtoList, request, orderMasterList.getTotalElements());
		return ServerResponse.createBySuccess(orderListPage);
		
	}
	
	
	private List<OrderDTO> masterListToDTO(Page<OrderMaster> orderMasterList) {
		List<OrderDTO> orderDtoList = new ArrayList<OrderDTO>();
		for (OrderMaster orderMaster : orderMasterList) {
			orderDtoList.add(this.masterToDTO(orderMaster));
		}
		return orderDtoList;
	}
	private OrderDTO masterToDTO(OrderMaster orderMaster) {
		OrderDTO orderDTO = new OrderDTO();
		BeanUtils.copyProperties(orderMaster, orderDTO);
		List<OrderDetail> orderDetailList = orderDetailDao.findByOrderId(orderMaster.getOrderId());
		orderDTO.setOrderDetailList(orderDetailList);
		return orderDTO;
	}
}
