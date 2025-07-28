package com.example.user_service.business.service;


import com.example.user_service.model.CreateUserRequest;
import com.example.user_service.model.LoginRequest;
import com.example.user_service.model.UpdateUserRequest;
import com.example.user_service.model.User;

import java.util.List;

public interface UserService {
    User createUser(CreateUserRequest request);

    List<User> getAllUsers();

    User getUserById(Long id);
    User updateUser(Long id, UpdateUserRequest request);
    void deactivateUser(Long id);
    User login(LoginRequest request);

}
