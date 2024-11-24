package com.app.habit;

import com.app.habit.dto.RegisterDto;
import com.app.habit.enums.GenderEnum;
import com.app.habit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class RegisterControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Test
  void testRegisterUser_Success() throws Exception {
    // Arrange
    RegisterDto registerDto = RegisterDto.builder()
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .email("john.doe@example.com")
        .gender(GenderEnum.MALE)
        .password("password123")
        .build();

    String requestBody = """
        {
            "firstName": "John",
            "lastName": "Doe",
            "username": "johndoe",
            "email": "john.doe@example.com",
            "gender": "MALE",
            "password": "password123"
        }
        """;

    // Act & Assert
    mockMvc.perform(post("/api/register/registerUser")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("success")))
        .andExpect(jsonPath("$.message", is("Successfully Registered!")))
        .andExpect(jsonPath("$.registeredUser.username", is("johndoe")))
        .andExpect(jsonPath("$.registeredUser.email", is("john.doe@example.com")));

    // Verify the user is stored in the database
    assertTrue(userRepository.findByUsername("johndoe").isPresent());
  }
}
