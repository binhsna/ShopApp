package com.binhnc.shopapp.controller;

import com.binhnc.shopapp.component.LocalizationUtils;
import com.binhnc.shopapp.dto.OrderDTO;
import com.binhnc.shopapp.model.Order;
import com.binhnc.shopapp.response.ListMessageResponse;
import com.binhnc.shopapp.response.MessageResponse;
import com.binhnc.shopapp.response.OrderListResponse;
import com.binhnc.shopapp.response.OrderResponse;
import com.binhnc.shopapp.service.IOrderService;
import com.binhnc.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")// ${api.prefix} -> Biến môi trường lưu trong application.yml
//@Validated -> Nếu đặt ở đây thì sẽ kiểm tra điều kiện đầu vào luôn và không vào logic code kiểm tra mình mong muốn
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        ListMessageResponse.builder()
                                .messages(errorMessages)
                                .build()
                );
            }
            Order existingOrder = orderService.createOrder(orderDTO);
            OrderResponse orderResponse = OrderResponse.fromOrder(existingOrder);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    MessageResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @GetMapping("/user/{user_id}")
    // Get http://localhost:8080/api/v1/orders/4
    public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") Long userId) {
        try {
            List<Order> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId) {
        try {
            Order existingOrder = orderService.getOrderById(orderId);
            OrderResponse orderResponse = OrderResponse.fromOrder(existingOrder);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    // Put http://localhost:8080/api/v1/orders/1
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody OrderDTO orderDTO) {
        try {
            Order orderUpdate = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(orderUpdate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable("id") Long id) {
        try {
            // Xóa mềm => Cập nhật trường active = false
            orderService.deleteOrder(id);
            return ResponseEntity.ok(
                    MessageResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_ORDER_SUCCESSFULLY, id))
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-orders-by-keyword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderListResponse> getOrdersByKeyword(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        // Tạo Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page - 1, limit,
                Sort.by("id").ascending()
        );
        Page<OrderResponse> orderPage = orderService
                .getOrdersByKeyword(keyword, pageRequest)
                .map(OrderResponse::fromOrder);
        // Lấy thông số trang
        int totalPages = orderPage.getTotalPages();
        List<OrderResponse> orderResponses = orderPage.getContent();
        return ResponseEntity.ok(OrderListResponse.builder()
                .orders(orderResponses)
                .totalPages(totalPages)
                .build());
    }
}
