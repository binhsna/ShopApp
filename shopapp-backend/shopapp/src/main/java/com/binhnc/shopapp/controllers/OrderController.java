package com.binhnc.shopapp.controllers;

import com.binhnc.shopapp.components.LocalizationUtils;
import com.binhnc.shopapp.dtos.OrderDTO;
import com.binhnc.shopapp.models.Order;
import com.binhnc.shopapp.responses.ListMessageResponse;
import com.binhnc.shopapp.responses.MessageResponse;
import com.binhnc.shopapp.responses.ResponseObject;
import com.binhnc.shopapp.responses.order.OrderListResponse;
import com.binhnc.shopapp.responses.order.OrderResponse;
import com.binhnc.shopapp.services.order.IOrderService;
import com.binhnc.shopapp.utils.MessageKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
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
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .status(HttpStatus.OK)
                            .message("Create new order successfully!")
                            .data(orderResponse)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    MessageResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_ORDER_FAILED, e.getMessage()))
                            .build());
        }
    }

    @GetMapping("/user/{user_id}")
    // Get http://localhost:8080/api/v1/orders/4
    public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") Long userId) {
        try {
            List<Order> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message(String.format("Get orders with userId: %d", userId))
                            .data(orders)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId) {
        try {
            Order existingOrder = orderService.getOrderById(orderId);
            OrderResponse orderResponse = OrderResponse.fromOrder(existingOrder);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .status(HttpStatus.OK)
                            .message(String.format("Get order with orderId: %d", orderId))
                            .data(orderResponse)
                            .build());
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
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.OK)
                    .message(String.format("Update order with orderId: %d successfully!", id))
                    .data(orderUpdate)
                    .build());
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
    public ResponseEntity<?> getOrdersByKeyword(
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
        OrderListResponse orderListResponse = OrderListResponse.builder()
                .orders(orderResponses)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message(String.format("Get orders with page: %d, limit: %d", page, limit))
                        .data(orderListResponse)
                        .build());
    }
}
