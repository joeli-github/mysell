package com.my.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.my.common.ServerResponse;
import com.my.dto.OrderDTO;
import com.my.service.OrderMasterService;
@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	private OrderMasterService orderMasterService;
    @RequestMapping("/")
    public ModelAndView test(){
    	ServerResponse<Page<OrderDTO>> serverResponse = orderMasterService.findOrderAll(1, 10);
          ModelAndView modelAndView=new ModelAndView("/index");
          modelAndView.addObject("orderDTOList",serverResponse.getData().getContent());
        return modelAndView;
    }
}
