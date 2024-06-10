package com.binhnc.shopapp.service;

import com.binhnc.shopapp.dto.OrderDTO;
import com.binhnc.shopapp.exception.DataNotFoundException;
import com.binhnc.shopapp.response.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;

    OrderResponse getOrderById(Long id);

    OrderResponse updateOrder(Long id, OrderDTO orderDTO);

    void deleteOrder(Long id);

    List<OrderResponse> getAllOrdersByUserId(Long userId);
}
