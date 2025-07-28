package com.example.user_service;

import com.example.user_service.business.handlers.UserNotFoundException;
import com.example.user_service.business.mappers.UserMapStructMapper;
import com.example.user_service.business.repository.UserRepository;
import com.example.user_service.business.repository.model.UserDAO;
import com.example.user_service.business.service.impl.UserServiceImpl;
import com.example.user_service.model.CreateUserRequest;
import com.example.user_service.model.LoginRequest;
import com.example.user_service.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapStructMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    @InjectMocks
    private UserServiceImpl userService;

    private CreateUserRequest createUserRequest;
    private UserDAO userDAO;
    private User user;

    @BeforeEach
    void setUp() {
        createUserRequest = CreateUserRequest.builder()
            .email("test@example.com")
            .password("password123")
            .firstName("John")
            .lastName("Doe")
            .phone("1234567890")
            .build();

        userDAO = UserDAO.builder()
            .email(createUserRequest.getEmail())
            .password(createUserRequest.getPassword())
            .firstName(createUserRequest.getFirstName())
            .lastName(createUserRequest.getLastName())
            .phone(createUserRequest.getPhone())
            .role("CUSTOMER")
            .active(true)
            .build();

        user = User.builder()
            .id(1L)
            .email(createUserRequest.getEmail())
            .firstName(createUserRequest.getFirstName())
            .lastName(createUserRequest.getLastName())
            .phone(createUserRequest.getPhone())
            .role("CUSTOMER")
            .active(true)
            .build();
    }

    @Test
    void createUser_ShouldReturnUser_WhenEmailIsNew() {
        when(userRepository.existsByEmail(createUserRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(UserDAO.class))).thenReturn(userDAO);
        when(userMapper.userDAOToUser(userDAO)).thenReturn(user);

        User result = userService.createUser(createUserRequest);

        assertNotNull(result);
        assertEquals(createUserRequest.getEmail(), result.getEmail());

        verify(userRepository).existsByEmail(createUserRequest.getEmail());
        verify(userRepository).save(any(UserDAO.class));
        verify(userMapper).userDAOToUser(userDAO);
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailExists() {
        when(userRepository.existsByEmail(createUserRequest.getEmail())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.createUser(createUserRequest);
        });

        assertEquals("Email already in use", ex.getMessage());

        verify(userRepository).existsByEmail(createUserRequest.getEmail());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).userDAOToUser(any());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        Long id = 1L;
        UserDAO userDAO = new UserDAO();
        userDAO.setId(1L);
        userDAO.setEmail("email@email.com");
        userDAO.setActive(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(userDAO));
        when(userMapper.userDAOToUser(userDAO)).thenReturn(user);

        User result = userService.getUserById(id);

        assertEquals(id, result.getId());
    }

    @Test
    void deactivateUser_ShouldSetActiveFalse_WhenUserExists() {
        UserDAO userDAO = new UserDAO();
        userDAO.setId(1L);
        userDAO.setActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userDAO));
        when(userRepository.save(any(UserDAO.class))).thenAnswer(i -> i.getArgument(0));

        userService.deactivateUser(1L);

        assertFalse(userDAO.isActive());
        verify(userRepository).save(userDAO);
    }

    @Test
    void deactivateUser_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deactivateUser(1L));
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");

        UserDAO userDAO = UserDAO.builder()
            .email(request.getEmail())
            .password("hashedPassword")
            .active(true)
            .build();

        User user = User.builder()
            .email(request.getEmail())
            .active(true)
            .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userDAO));
        when(passwordEncoder.matches(request.getPassword(), userDAO.getPassword())).thenReturn(true);
        when(userMapper.userDAOToUser(userDAO)).thenReturn(user);

        User result = userService.login(request);

        assertNotNull(result);
        assertEquals(request.getEmail(), result.getEmail());
    }

    @Test
    void login_ShouldThrow_WhenUserNotFound() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.login(request));
    }

    @Test
    void login_ShouldThrow_WhenPasswordDoesNotMatch() {
        LoginRequest request = new LoginRequest("test@example.com", "wrongPassword");

        UserDAO userDAO = UserDAO.builder()
            .email(request.getEmail())
            .password("hashedPassword")
            .active(true)
            .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userDAO));
        when(passwordEncoder.matches(request.getPassword(), userDAO.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.login(request));
    }

    @Test
    void login_ShouldThrow_WhenUserNotActive() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");

        UserDAO userDAO = UserDAO.builder()
            .email(request.getEmail())
            .password("hashedPassword")
            .active(false)
            .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userDAO));

        assertThrows(RuntimeException.class, () -> userService.login(request));
    }
}

