package com.binhnc.shopapp.services.order;

import com.binhnc.shopapp.dtos.OrderDTO;
import com.binhnc.shopapp.models.Order;
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
