package com.ficklebytes.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ficklebytes.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomUserDto {

  private int userId;
  private String fullName;
  private Role role;


}
