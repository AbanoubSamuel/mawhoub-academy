package com.ficklebytes.reply.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReply {

  @JsonProperty(value = "emailExists", required = true)
  private boolean emailExists;

  @JsonProperty(value = "phoneExists", required = true)
  private boolean phoneExists;

  @JsonProperty(value = "userId", required = true)
  private String userId;


}
