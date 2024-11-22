package com.app.habit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/")
public class UserController {

  @GetMapping("/say-hello")
  public String sayHello() {
    return "Hello User";
  }

}
