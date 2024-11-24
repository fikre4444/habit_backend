package com.app.habit.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.app.habit.domain.User;
import com.app.habit.dto.RegisterDto;
import com.app.habit.enums.GenderEnum;
import com.app.habit.enums.RoleEnum;
import com.app.habit.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Component
public class ProfileChecker {

  @Autowired
  Environment environment;

  @Autowired
  RegisterService registerService;

  @PostConstruct
  public void checkAndAdd() {
    // checks the current being used profile and database and adds some users if its
    // h2
    String[] activeProfiles = environment.getActiveProfiles();
    if (activeProfiles.length > 0) {
      String activeProfile = activeProfiles[0];
      if ("h2".equalsIgnoreCase(activeProfile)) {
        System.out.println("Working in H2");
        RegisterDto user = RegisterDto.builder()
            .firstName("fikre")
            .lastName("tesfay")
            .username("fikre11")
            .email("fikrefikre@gmail.com")
            .gender(GenderEnum.MALE)
            .password("1234")
            .build();
        Map<String, Object> result = registerService.registerUser(user);
        System.out.println("The testing registered user is ");
        System.out.println(result.get("registeredUser"));

        Map<String, Object> result2 = registerService.registerAdmin();
        System.out.println("The testing registered admin is ");
        System.out.println(result2.get("registeredUser"));

      } else if ("postgres".equalsIgnoreCase(activeProfile)) {
        System.out.println("Working in PostgreSQL");
      } else {
        System.out.println("Working in an unknown profile: " + activeProfile);
      }
    } else {
      System.out.println("No active profile set!");
    }

  }

}
