package com.app.habit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.habit.filter.JwtFilter;
import com.app.habit.service.MyUserDetailsService;

@Configuration
public class SecurityConfig {

  @Autowired
  private MyUserDetailsService userDetailsService;

  @Autowired
  private JwtFilter jwtFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http = http.csrf(customzer -> customzer.disable())
        .cors(customizer -> customizer.disable())
        .httpBasic(customizer -> customizer.disable())
        .authorizeHttpRequests(customizer -> customizer
            .requestMatchers("/api/hello/**", "/api/auth/**", "/api/register/**", "/h2-console/**").permitAll()
            .requestMatchers("/api/admin/**").hasRole("ADMINISTRATOR")
            .requestMatchers("/api/user/**").hasRole("USER"))
        .headers(customizer -> customizer.frameOptions(customizer1 -> customizer1.sameOrigin()))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

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
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

}
