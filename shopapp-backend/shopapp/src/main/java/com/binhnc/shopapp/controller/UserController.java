package com.binhnc.shopapp.controller;

import com.binhnc.shopapp.dto.UserDTO;
import com.binhnc.shopapp.dto.UserLoginDTO;
import com.binhnc.shopapp.model.User;
import com.binhnc.shopapp.response.ListMessageResponse;
import com.binhnc.shopapp.response.LoginResponse;
import com.binhnc.shopapp.response.MessageResponse;
import com.binhnc.shopapp.response.RegisterResponse;
import com.binhnc.shopapp.service.IUserService;
import com.binhnc.shopapp.component.LocalizationUtils;
import com.binhnc.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> UserLoginDTO(
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            String token = userService.login(
                    userLoginDTO.getPhoneNumber(),
                    userLoginDTO.getPassword(),
                    userLoginDTO.getRoleId()
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
}
