package com.my.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.my.pojo.OrderMaster;

public interface OrderMasterDao extends JpaRepository<OrderMaster, String> {

	Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}
