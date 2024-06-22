package com.binhnc.shopapp.services.orderdetail;

import com.binhnc.shopapp.dtos.OrderDetailDTO;
import com.binhnc.shopapp.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;

    OrderDetail findById(Long id) throws Exception;

    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception;

    void deleteById(Long id);

    List<OrderDetail> findByOrderId(Long orderId);
}
