package com.app.habit.dto;

import com.app.habit.enums.GenderEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {

  private String firstName;

  private String lastName;

  private String username;

  private String email;

  private GenderEnum gender;

  private String password;

}
