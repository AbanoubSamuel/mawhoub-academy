package com.ficklebytes.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ficklebytes.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

  @JsonProperty(value = "email", required = true)
  private String email;
  @JsonProperty(value = "password", required = true)
  private String password;

  @Override
  public String toString() {
    return Utils.toString(this);
  }
}
