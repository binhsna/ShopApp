package com.binhnc.shopapp.service;

import com.binhnc.shopapp.dto.OrderDTO;
import com.binhnc.shopapp.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;

    Order getOrderById(Long id);

    Order updateOrder(Long id, OrderDTO orderDTO) throws Exception;

    void deleteOrder(Long id);

    List<Order> findByUserId(Long userId);

    Page<Order> getOrdersByKeyword(String keyword, Pageable pageable);
}
