package com.app.habit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.habit.dto.LoginDto;
import com.app.habit.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
    System.out.println("printing out stuff.");
    return ResponseEntity.ok(authService.authenticateAccount(loginDto));
  }

  @GetMapping("/hello")
  public String sayHello() {
    return "hello from auth";
  }

}
