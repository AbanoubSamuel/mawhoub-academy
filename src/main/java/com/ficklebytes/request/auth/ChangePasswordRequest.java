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
public class ChangePasswordRequest {

  @JsonProperty(value = "email", required = true)
  private String email;
  @JsonProperty(value = "token", required = true)
  private String token;
  @JsonProperty(value = "newPassword", required = true)
  private String newPassword;

  @Override
  public String toString() {
    return Utils.toString(this);
  }
}
