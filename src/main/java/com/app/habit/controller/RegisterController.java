package com.app.habit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.habit.dto.RegisterDto;
import com.app.habit.service.RegisterService;

@RestController
@RequestMapping("/api/register/")
public class RegisterController {

  @Autowired
  private RegisterService registerService;

  @PostMapping("/registerUser")
  public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto) {
    return ResponseEntity.ok(registerService.registerUser(registerDto));
  }

  @GetMapping("/hello")
  public String sayHello() {
    return "hello from register";
  }

  @PostMapping("/registerAdmin")
  public ResponseEntity<?> registerAdmin() {
    return ResponseEntity.ok(registerService.registerAdmin());
  }

}
