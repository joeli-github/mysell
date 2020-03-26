package com.my.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.pojo.OrderMaster;

public interface OrderMasterDao extends JpaRepository<OrderMaster, String> {

}
