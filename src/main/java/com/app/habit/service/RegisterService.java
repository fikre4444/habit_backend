package com.app.habit.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.habit.domain.User;
import com.app.habit.dto.RegisterDto;
import com.app.habit.enums.GenderEnum;
import com.app.habit.enums.RoleEnum;
import com.app.habit.repository.UserRepository;

@Service
public class RegisterService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Map<String, Object> registerUser(RegisterDto registerDto) {
    User user = User.builder()
        .firstName(registerDto.getFirstName())
        .lastName(registerDto.getLastName())
        .username(registerDto.getUsername())
        .email(registerDto.getEmail())
        .gender(registerDto.getGender())
        .password(passwordEncoder.encode(registerDto.getPassword()))
        .role(RoleEnum.ROLE_USER)
        .created_at(LocalDateTime.now())
        .build();
    User savedUser = userRepository.save(user);
    return Map.of("result", "success", "message", "Successfully Registered!", "registeredUser", savedUser);
  }

  public Map<String, Object> registerAdmin() {
    User user = User.builder()
        .firstName("Jacob")
        .lastName("Issiah")
        .username("jacob11")
        .email("jacob2@gmail.com")
        .gender(GenderEnum.MALE)
        .password(passwordEncoder.encode("1234"))
        .role(RoleEnum.ROLE_ADMINISTRATOR)
        .created_at(LocalDateTime.now())
        .build();
    User savedUser = userRepository.save(user);
    return Map.of("result", "success", "message", "Successfully Registered!", "registeredUser", savedUser);
  }

}
