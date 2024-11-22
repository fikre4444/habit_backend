package com.app.habit.enums;

public enum GenderEnum {
  MALE("Male"),
  FEMALE("Female");

  private String gender;

  private GenderEnum(String g) {
    this.gender = g;
  }

  public String getGender() {
    return this.gender;
  }

}
