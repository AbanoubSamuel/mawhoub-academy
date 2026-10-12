package com.ficklebytes.request.user;

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
public class UpdateTeacherRequest {

  @JsonProperty(value = "userId", required = true)
  private int userId;

  @JsonProperty(value = "fullName", required = true)
  private String fullName;

  @JsonProperty(value = "biography", required = true)
  private String biography;

  @Override
  public String toString() {
    return Utils.toString(this);
  }
}
