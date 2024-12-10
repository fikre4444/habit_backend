package com.app.habit.service;

import com.app.habit.service.JwtService;
import com.app.habit.repository.UserRepository;
import com.app.habit.domain.User;
import com.app.habit.enums.RoleEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Autowired
  private JwtService jwtService; // To generate JWT tokens

  @Autowired
  private UserRepository userRepository; // To retrieve user details

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Value("${after.login.redirect}")
  private String afterLoginRedirect;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    // Extract OAuth2 user information
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    // Extract user information from Google
    String email = oAuth2User.getAttribute("email");
    String name = oAuth2User.getAttribute("name");

    // Check if the user exists in the database
    User user = userRepository.findByEmail(email).orElse(null);

    if (user == null) {
      // If the user doesn't exist, create and save a new user
      System.out.println("\n\n\nCreating the user since they don't exist!");
      user = User.builder()
          .email(email)
          .firstName(name.split(" ")[0]) // Assuming first name is the first part of the name
          .lastName(name.split(" ").length > 1 ? name.split(" ")[1] : "")
          .username(email) // Use email as username
          .password(passwordEncoder.encode(email))
          .role(RoleEnum.ROLE_USER) // Default role
          .created_at(LocalDateTime.now())
          .build();
      userRepository.save(user);
    }

    // Add logic for JWT token generation
    String token = jwtService.generateToken(user.getUsername());
    System.out.println("the token is " + token);

    response.sendRedirect(afterLoginRedirect + "?token=" + token);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("{\"token\":\"" + token + "\"}");
    response.getWriter().flush();

  }
}

// String email = oAuth2User.getAttribute("email");
// System.out.println("THe oauth user is " + oAuth2User);

// // Fetch user from the database
// User user = userRepository.findByEmail(email)
// .orElseThrow(() -> new RuntimeException("User not found"));

// // Generate JWT token for the user
// String jwtToken = jwtService.generateToken(user.getUsername());

// // Send the token in the response body
// response.setContentType("application/json");
// response.getWriter().write("{\"token\": \"" + jwtToken + "\"}");
// response.getWriter().flush();
