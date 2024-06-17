package com.binhnc.shopapp.service;


import com.binhnc.shopapp.dto.UserDTO;
import com.binhnc.shopapp.model.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    // Trả về token
    String login(String phoneNumber, String password, Long roleId) throws Exception;
}
