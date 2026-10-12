package com.ficklebytes.reply;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacebookAuthReply {

  @JsonProperty(value = "email", required = true)
  public String email;
  @JsonProperty(value = "name", required = true)
  public String name;
  @JsonProperty(value = "id", required = true)
  public String id;

}
