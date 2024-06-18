package com.binhnc.shopapp.service;

import com.binhnc.shopapp.dto.CartItemDTO;
import com.binhnc.shopapp.dto.OrderDTO;
import com.binhnc.shopapp.exception.DataNotFoundException;
import com.binhnc.shopapp.model.*;
import com.binhnc.shopapp.repository.OrderDetailRepository;
import com.binhnc.shopapp.repository.OrderRepository;
import com.binhnc.shopapp.repository.ProductRepository;
import com.binhnc.shopapp.repository.UserRepository;
import com.binhnc.shopapp.response.OrderResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) throws Exception {
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
        order.setTotalMoney(orderDTO.getTotalMoney());
        orderRepository.save(order);
        // Tạo danh sách các đối tượng OrderDetail từ cartItems
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            // Tạo một đối tượng OrderDetail từ CartItemDTO
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            // Lấy thông tin sản phẩm từ cartItemDTO
            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();
            // Tìm thông tin sản phẩm từ database (hoặc sử dụng cache nếu cần)
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));
            // Đặt lại thông tin cho OrderDetail
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            // Các trường khác của OrderDetail (Nếu cần)
            orderDetail.setPrice(product.getPrice());
            // Thêm OrderDetail vào danh sách
            orderDetails.add(orderDetail);
        }
        // Lưu danh sách OrderDetail vào database
        orderDetailRepository.saveAll(orderDetails);
        // Map lại
        modelMapper.typeMap(Order.class, OrderResponse.class);
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.map(order, orderResponse);
        return order;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(null);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, OrderDTO orderDTO) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + id));
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + id));
        // Tạo một luồng bằng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // Cập nhật các trường của đơn hàng từ orderDTO
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        // No hard-delete => please soft-delete
        if (optionalOrder.isPresent()) {
            // orderRepository.delete(optionalOrder.get());
            optionalOrder.get().setActive(false);
            orderRepository.save(optionalOrder.get());
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
