package com.ficklebytes.request.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessInfoRequest {
  @JsonProperty(value = "id", required = true)
  private Integer id;

  @JsonProperty(value = "home_video_url", required = true)
  private String home_video_url;
  
  @JsonProperty(value = "user_guide_video_url", required = true)
  private String user_guide_video_url;
  
  @JsonProperty(value = "user_guide_image_url", required = true)
  private String user_guide_image_url;
  
  @JsonProperty(value = "times_of_work", required = true)
  private String times_of_work;
  
  @JsonProperty(value = "telNumber", required = true)
  private String telNumber;
  
  @JsonProperty(value = "whatsappNumber", required = true)
  private String whatsappNumber;
  
  @JsonProperty(value = "viberNumber", required = true)
  private String viberNumber;
  
  @JsonProperty(value = "mobileNumber", required = true)
  private String mobileNumber;
  
  @JsonProperty(value = "email", required = true)
  private String email;
}
