package com.ficklebytes.request.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactUsRequest {

  @JsonProperty(value = "name", required = true)
  private String name;
  @JsonProperty(value = "message", required = true)
  private String message;
  @JsonProperty(value = "email", required = true)
  private String email;

}
