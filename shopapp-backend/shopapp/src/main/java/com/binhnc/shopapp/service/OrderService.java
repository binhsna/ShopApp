package com.binhnc.shopapp.service;

import com.binhnc.shopapp.dto.OrderDTO;
import com.binhnc.shopapp.exception.DataNotFoundException;
import com.binhnc.shopapp.model.Order;
import com.binhnc.shopapp.model.OrderStatus;
import com.binhnc.shopapp.model.User;
import com.binhnc.shopapp.repository.OrderRepository;
import com.binhnc.shopapp.repository.UserRepository;
import com.binhnc.shopapp.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        // Tìm xem user_id có tồn tại không
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));
        // Convert orderDTO -> Order
        // Dùng thư viện Model Mapper
        // Tạo một luồng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // Cập nhật các trường của đơn hàng từ orderDTO
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now()); // Lấy giờ hiện tại
        order.setStatus(OrderStatus.PENDING);
        // Kiểm tra shipping date phải >= ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today!");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        // Map lại
        modelMapper.typeMap(Order.class, OrderResponse.class);
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.map(order, orderResponse);
        return orderResponse;
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return null;
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {

    }

    @Override
    public List<OrderResponse> getAllOrdersByUserId(Long userId) {
        return List.of();
    }
}
