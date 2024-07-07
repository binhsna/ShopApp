package com.binhnc.shopapp.controllers;

import com.binhnc.shopapp.models.Role;
import com.binhnc.shopapp.responses.ResponseObject;
import com.binhnc.shopapp.services.role.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("")
    public ResponseEntity<?> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Get roles successfully!")
                        .data(roles)
                        .build());
    }
}
