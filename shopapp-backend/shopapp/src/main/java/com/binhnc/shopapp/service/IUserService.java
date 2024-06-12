package com.binhnc.shopapp.service;


import com.binhnc.shopapp.dto.UserDTO;
import com.binhnc.shopapp.exception.DataNotFoundException;
import com.binhnc.shopapp.model.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;

    // Trả về token
    String login(String phoneNumber, String password) throws Exception;
}
