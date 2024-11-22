package com.app.habit.enums;

public enum RoleEnum {
  ROLE_USER("User"),
  ROLE_ADMINISTRATOR("Administrator");

  private String name;

  private RoleEnum(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static RoleEnum fromName(String name) {
    for (RoleEnum roleEnum : RoleEnum.values()) {
      if (roleEnum.getName().equalsIgnoreCase(name)) { // Case insensitive comparison
        return roleEnum;
      }
    }
    throw new IllegalArgumentException("No enum constant with Role name " + name);
  }
}
