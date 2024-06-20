package com.binhnc.shopapp.controller;

import com.binhnc.shopapp.dto.UpdateUserDTO;
import com.binhnc.shopapp.dto.UserDTO;
import com.binhnc.shopapp.dto.UserLoginDTO;
import com.binhnc.shopapp.model.Role;
import com.binhnc.shopapp.model.User;
import com.binhnc.shopapp.response.*;
import com.binhnc.shopapp.service.IUserService;
import com.binhnc.shopapp.component.LocalizationUtils;
import com.binhnc.shopapp.utils.MessageKeys;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;

    // Đăng ký
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody UserDTO userDTO,
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
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body(
                        MessageResponse.builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                                .build()
                );
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(RegisterResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                    .user(user)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    MessageResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            String token = userService.login(
                    userLoginDTO.getPhoneNumber(),
                    userLoginDTO.getPassword(),
                    userLoginDTO.getRoleId() == null ? Long.valueOf(Role.USER) : userLoginDTO.getRoleId()
            );
            // Trả về token trong response
            return ResponseEntity.ok().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                            .token(token)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    // Update user
    @PutMapping("/details/{userId}")
    public ResponseEntity<UserResponse> updateUserDetails(
            @PathVariable Long userId,
            @RequestBody UpdateUserDTO updateUserDTO,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            String extractedToken = authorizationHeader.substring(7); // Loại bỏ "Bearer " từ chuỗi token
            User user = userService.getUserDetailsFromToken(extractedToken);
            if (user.getId() != userId) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            User updateUser = userService.updateUser(userId, updateUserDTO);
            return ResponseEntity.ok(UserResponse.fromUser(updateUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get user with token
    @PostMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String extractedToken = authorizationHeader.substring(7); // Loại bỏ "Bearer " từ chuỗi token
            User user = userService.getUserDetailsFromToken(extractedToken);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/reset-password")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> resetPassword() {
        // Continue
        return ResponseEntity.ok().build();
    }

    // Get All User (ADMIN)
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUser(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit
    ) {
        try {
            // Tạo PageRequest từ thông tin trang và giới hạn
            PageRequest pageRequest = PageRequest.of(
                    page - 1, limit,
                    Sort.by("id").ascending());
            Page<UserResponse> userPage = userService.findAll(keyword, pageRequest)
                    .map(UserResponse::fromUser);
            int totalPages = userPage.getTotalPages();
            List<UserResponse> userResponses = userPage.getContent();
            return ResponseEntity.ok(UserListResponse.builder()
                    .users(userResponses)
                    .totalPages(totalPages)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
