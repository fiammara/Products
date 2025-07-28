package com.example.user_service.business.service.impl;


import com.example.user_service.business.handlers.AuthenticationFailedException;
import com.example.user_service.business.handlers.UserNotFoundException;
import com.example.user_service.business.mappers.UserMapStructMapper;
import com.example.user_service.business.repository.UserRepository;
import com.example.user_service.business.repository.model.UserDAO;
import com.example.user_service.business.service.UserService;
import com.example.user_service.model.CreateUserRequest;
import com.example.user_service.model.LoginRequest;
import com.example.user_service.model.UpdateUserRequest;
import com.example.user_service.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapStructMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        UserDAO user = UserDAO.builder()
            .email(request.getEmail())
            .password(hashedPassword)
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .phone(request.getPhone())
            .role("CUSTOMER")
            .active(true)
            .build();

        UserDAO savedUser = userRepository.save(user);
        return userMapper.userDAOToUser(savedUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
            .map(userMapper::userDAOToUser)
            .collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .map(userMapper::userDAOToUser)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User updateUser(Long id, UpdateUserRequest request) {
        UserDAO userDAO = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        userDAO.setFirstName(request.getFirstName());
        userDAO.setLastName(request.getLastName());
        userDAO.setPhone(request.getPhone());

        UserDAO updated = userRepository.save(userDAO);
        return userMapper.userDAOToUser(updated);
    }

    @Override
    public void deactivateUser(Long id) {
        UserDAO userDAO = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        userDAO.setActive(false);
        userRepository.save(userDAO);
    }

    @Override
    public User login(LoginRequest request) {
        UserDAO userDAO = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AuthenticationFailedException("Invalid email or password"));

        if (!userDAO.isActive()) {
            throw new AuthenticationFailedException("User is not active");
        }

        if (!passwordEncoder.matches(request.getPassword(), userDAO.getPassword())) {
            throw new AuthenticationFailedException("Invalid email or password");
        }

        return userMapper.userDAOToUser(userDAO);
    }
}
