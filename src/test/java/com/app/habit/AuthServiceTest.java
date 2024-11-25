package com.app.habit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.app.habit.dto.LoginDto;
import com.app.habit.service.AuthService;
import com.app.habit.service.JwtService;

public class AuthServiceTest {
    
    @Mock
    private AuthenticationManager authManager;
    
    @Mock
    private JwtService jwtService;
    
    @Mock
    private Authentication authentication;
    
    @InjectMocks
    private AuthService authService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testAuthenticateAccount_Success() {
        // Arrange
        LoginDto loginDto = LoginDto.builder()
            .username("johndoe")
            .password("password123")
            .build();
            
        String expectedToken = "jwt.token.here";
        
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(loginDto.getUsername())).thenReturn(expectedToken);
        
        // Act
        String result = authService.authenticateAccount(loginDto);
        
        // Assert
        assertEquals(expectedToken, result);
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(loginDto.getUsername());
    }
    
    @Test
    void testAuthenticateAccount_Failed() {
        // Arrange
        LoginDto loginDto = LoginDto.builder()
            .username("johndoe")
            .password("wrongpassword")
            .build();
            
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        
        // Act
        String result = authService.authenticateAccount(loginDto);
        
        // Assert
        assertEquals("failed Authentication", result);
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(anyString());
    }
} 