package com.my.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.common.ServerResponse;
import com.my.common.converter.OrderMaster2OrderDTOConverter;
import com.my.common.enums.OrderStatusEnum;
import com.my.common.enums.PayStatusEnum;
import com.my.common.enums.ResultEnum;
import com.my.dao.OrderDetailDao;
import com.my.dao.OrderMasterDao;
import com.my.dto.CartDTO;
import com.my.dto.OrderDTO;
import com.my.exception.MySellException;
import com.my.pojo.OrderDetail;
import com.my.pojo.OrderMaster;
import com.my.pojo.ProductInfo;
import com.my.service.OrderMasterService;
import com.my.service.ProductInfoService;
import com.my.utils.BigDecimalUtil;
import com.my.utils.KeyUtil;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class OrderMasterServiceImpl implements OrderMasterService {

	@Autowired
	private OrderMasterDao orderMasterDao;
	@Autowired
	private OrderDetailDao orderDetailDao;
	@Autowired
	private ProductInfoService productInfoService;
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


	@Override
	@Transactional
	public ServerResponse<OrderDTO> create(OrderDTO orderDTO) {
		String orderId = KeyUtil.getUniqueKey();
		BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
		//1.查询商品数量和价格
		for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
			ProductInfo productInfo = productInfoService.findById(orderDetail.getProductId());
			if (productInfo == null) {
				throw new MySellException(ResultEnum.PRODUCT_NOT_EXIST);
			}
			//2.计算总价
			//商品总价
			BigDecimal orderTotol = BigDecimalUtil.mul(productInfo.getProductPrice().doubleValue(),
					orderDetail.getProductQuantity().doubleValue());
			//订单总价
			orderAmount =orderAmount.add(orderTotol);
			//订单详情入库
			orderDetail.setDetailId(KeyUtil.getUniqueKey());
			orderDetail.setOrderId(orderId);
			BeanUtils.copyProperties(productInfo, orderDetail);
			orderDetailDao.save(orderDetail);
		}
		//3.订单入库
		OrderMaster orderMaster = new OrderMaster();
		BeanUtils.copyProperties(orderDTO, orderMaster);
		orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
		orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
		orderMaster.setOrderId(orderId);
		orderMaster.setOrderAmount(orderAmount);
		orderMasterDao.save(orderMaster);
		//4.扣库存
		List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
				.map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()) )
				.collect(Collectors.toList());
		productInfoService.decreaseStock(cartDTOList);
		return ServerResponse.createBySuccess(orderDTO);
	}


	@Override
	public ServerResponse<OrderDTO> findOne(String orderId) {
		OrderMaster orderMaster = orderMasterDao.findById(orderId).get();
		if (orderMaster == null) {
			throw new MySellException(ResultEnum.ORDER_NOT_EXIST);
		}
		List<OrderDetail> orderDetailList = orderDetailDao.findByOrderId(orderId);
		if (CollectionUtils.isEmpty(orderDetailList)) {
			throw new MySellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
		}
		OrderDTO orderDTO = new OrderDTO();
		BeanUtils.copyProperties(orderMaster, orderDTO);
		orderDTO.setOrderDetailList(orderDetailList);
		return ServerResponse.createBySuccess(orderDTO);
	}


	@Override
	public ServerResponse<Page<OrderDTO>> findList(String buyerOpenid, Pageable pageable) {
		Page<OrderMaster> orderMasterPage = orderMasterDao.findByBuyerOpenid(buyerOpenid, pageable);
		List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
		Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
		return ServerResponse.createBySuccess(orderDTOPage);
	}


	@Override
	@Transactional
	public ServerResponse<OrderDTO> cancel(OrderDTO orderDTO) {
		//1.判断订单状态
		if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
			log.error("【取消订单】订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
			ServerResponse.createByErrorMessage("【取消订单】订单状态不正确");
//			throw new MySellException(ResultEnum.ORDER_STATUS_ERROR);
		}
		//2.修改订单状态
		OrderMaster orderMaster = new OrderMaster();
		orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
		BeanUtils.copyProperties(orderDTO, orderMaster);
		OrderMaster resultMaster = orderMasterDao.save(orderMaster);
		if (resultMaster == null) {
			log.error("【取消订单】修改订单失败");
			ServerResponse.createByErrorCodeMessage(ResultEnum.ORDER_UPDATE_FAIL.getCode(), "订单更新失败");
		}
		//3.返还库存
		if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
			log.error("【取消订单】 订单中没有商品详情,orderDTO={}",orderDTO);
			ServerResponse.createByErrorMessage("改订单没有订单详情");
		}
		List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
				.map(e ->new CartDTO(e.getProductId(), e.getProductQuantity()) )
				.collect(Collectors.toList());
		productInfoService.increaseStock(cartDTOList);
		//4.如果已支付，需要退款
		if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
			//TODO
		}
		return ServerResponse.createBySuccess("取消订单成功", orderDTO);
	}


	@Override
	public ServerResponse<OrderDTO> finish(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ServerResponse<OrderDTO> paid(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		return null;
	}
}
