package com.app.habit.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.habit.filter.JwtFilter;
import com.app.habit.service.MyUserDetailsService;
import com.app.habit.service.OAuth2LoginSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

  @Autowired
  private MyUserDetailsService userDetailsService;

  @Autowired
  private JwtFilter jwtFilter;

  @Autowired
  private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http = http.csrf(customzer -> customzer.disable())
        .cors(customizer -> customizer.disable())
        .httpBasic(customizer -> customizer.disable())
        .authorizeHttpRequests(customizer -> customizer
            .requestMatchers("/api/hello/**", "/api/auth/**", "/api/register/**", "/h2-console/**", "/api/user/welcome")
            .permitAll()
            .requestMatchers("/api/admin/**").hasRole("ADMINISTRATOR")
            .requestMatchers("/api/user/**").hasRole("USER"))
        .headers(customizer -> customizer.frameOptions(customizer1 -> customizer1.sameOrigin())) // for h2
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(new AuthenticationEntryPoint() {
              @Override
              public void commence(HttpServletRequest request, HttpServletResponse response,
                  AuthenticationException authException) throws IOException, ServletException {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter()
                    .write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
              }
            }))
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/oauth2/authorization/google") // Ensure this matches your setup
            .successHandler(oAuth2LoginSuccessHandler)); // Success handler

    return http.build();
  }

  @Bean
  public AuthenticationProvider authenticator() {
    DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
    dao.setUserDetailsService(userDetailsService);
    dao.setPasswordEncoder(new BCryptPasswordEncoder());
    return dao;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

}
