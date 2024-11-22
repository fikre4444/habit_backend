package com.app.habit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.app.habit.dto.LoginDto;

@Service
public class AuthService {

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private JwtService jwtService;

  public String authenticateAccount(LoginDto loginDto) {
    Authentication authentication = authManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

    if (authentication.isAuthenticated())
      return jwtService.generateToken(loginDto.getUsername());
    return "failed Authentication";

  }

}
