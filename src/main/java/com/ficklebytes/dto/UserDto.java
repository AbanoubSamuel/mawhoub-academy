package com.ficklebytes.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ficklebytes.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

  private int userId;
  private String fullName;
  private String email;
  private String pictureUrl;
  private String biography;
  private Role role;

  @JsonIgnore
  public boolean isAdmin() {
    return role == Role.ADMIN;
  }

}
