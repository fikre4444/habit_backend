package com.app.habit;

import com.app.habit.domain.User;
import com.app.habit.dto.RegisterDto;
import com.app.habit.enums.GenderEnum;
import com.app.habit.enums.RoleEnum;
import com.app.habit.repository.UserRepository;
import com.app.habit.service.RegisterService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private RegisterService registerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRegisterUser_Success() {
    // Arrange
    RegisterDto registerDto = RegisterDto.builder()
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .email("john.doe@example.com")
        .gender(GenderEnum.MALE)
        .password("password123")
        .build();

    User user = User.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .email("john.doe@example.com")
        .gender(GenderEnum.MALE)
        .password("encodedPassword123")
        .role(RoleEnum.ROLE_USER)
        .created_at(LocalDateTime.now())
        .build();

    when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword123");
    when(userRepository.save(any(User.class))).thenReturn(user);

    // Act
    Map<String, Object> response = registerService.registerUser(registerDto);

    // Assert
    assertEquals("success", response.get("result"));
    assertEquals("Successfully Registered!", response.get("message"));
    assertNotNull(response.get("registeredUser"));

    User registeredUser = (User) response.get("registeredUser");
    assertEquals("John", registeredUser.getFirstName());
    assertEquals("johndoe", registeredUser.getUsername());
    assertEquals("encodedPassword123", registeredUser.getPassword());

    verify(passwordEncoder, times(1)).encode(registerDto.getPassword());
    verify(userRepository, times(1)).save(any(User.class));
  }
}
