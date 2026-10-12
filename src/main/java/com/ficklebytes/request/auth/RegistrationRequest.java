package com.ficklebytes.request.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ficklebytes.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {

  @JsonProperty(value = "fullName", required = true)
  private String fullName;
  @JsonProperty(value = "email", required = true)
  private String email;
  @JsonProperty(value = "password", required = true)
  private String password;
  @JsonProperty(value = "phoneNumber", required = true)
  private String phoneNumber;

  @JsonIgnore
  public boolean validate() {
    //TODO add phone validation and output correct errors
    if (getEmail().length() == 0 || !Utils.IsValidEmailAddress(getEmail())) {
      return false;
    }
    if (getFullName().length() == 0 || getFullName().length() > 64) {
      return false;
    }
    return getPassword().length() >= 6 && getPassword().length() <= 32;
  }

  @Override
  public String toString() {
    return Utils.toString(this);
  }
}
