package com.binhnc.shopapp.controller;

import com.binhnc.shopapp.model.Category;
import com.binhnc.shopapp.response.MessageResponse;
import com.binhnc.shopapp.service.ICategoryService;
import com.binhnc.shopapp.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/health-check")
@RequiredArgsConstructor
public class HealthCheckController {
    private final ICategoryService categoryService;

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            String computerName = InetAddress.getLocalHost().getHostName();
            MessageResponse response = MessageResponse.builder()
                    .message("Ok, Computer Name: " + computerName)
                    .build();
            return ResponseEntity.ok(ObjectMapperUtils.toJson(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");
        }
    }
}
