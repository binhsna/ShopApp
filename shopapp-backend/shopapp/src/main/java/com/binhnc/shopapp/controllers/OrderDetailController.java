package com.binhnc.shopapp.controllers;

import com.binhnc.shopapp.components.LocalizationUtils;
import com.binhnc.shopapp.dtos.OrderDetailDTO;
import com.binhnc.shopapp.exceptions.DataNotFoundException;
import com.binhnc.shopapp.models.OrderDetail;
import com.binhnc.shopapp.responses.ListMessageResponse;
import com.binhnc.shopapp.responses.MessageResponse;
import com.binhnc.shopapp.responses.ResponseObject;
import com.binhnc.shopapp.responses.order.OrderDetailResponse;
import com.binhnc.shopapp.services.orderdetail.IOrderDetailService;
import com.binhnc.shopapp.utils.MessageKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;
    private final LocalizationUtils localizationUtils;

    // Thêm mới 1 order detail
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
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
                                .build());
            }
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseObject.builder()
                            .status(HttpStatus.CREATED)
                            .message(String.format("Create order with orderId: %d, productId: %d", orderDetailDTO.getOrderId(), orderDetailDTO.getProductId()))
                            .data(OrderDetailResponse.fromOrderDetail(newOrderDetail))
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
        try {
            OrderDetail orderDetail = orderDetailService.findById(id);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .status(HttpStatus.OK)
                            .message(String.format("Get order with orderId: %d", id))
                            .data(OrderDetailResponse.fromOrderDetail(orderDetail))
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Lấy ra danh sách các order_details của 1 order nào đó
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId) {
        try {
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
            List<OrderDetailResponse> orderDetailResponses = orderDetails
                    .stream()
                    .map(orderDetail -> OrderDetailResponse.fromOrderDetail(orderDetail))
                    .toList();
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .status(HttpStatus.OK)
                            .message(String.format("Get list order detail with orderId: %d", orderId))
                            .data(orderDetailResponses)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cập nhật order_detail nào đó
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody OrderDetailDTO orderDetailDTO) throws DataNotFoundException, Exception {
        OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Update order detail successfully")
                        .data(OrderDetailResponse.fromOrderDetail(orderDetail))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id) {
        try {
            orderDetailService.deleteById(id);
            return ResponseEntity.ok().body(
                    MessageResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_ORDER_DETAIL_SUCCESSFULLY, id))
                            .build());
            // return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
