package com.ficklebytes.dto;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.ficklebytes.config.JacksonConfig;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PurchaseWithoutCourseDto {

  private String userFullName;
  private String userEmail;
  private int userId;

  @JsonFormat(pattern = JacksonConfig.dateTimeFormat, timezone = JacksonConfig.timeZone, shape = Shape.STRING)
  private LocalDateTime activationTimeDate;

  public PurchaseWithoutCourseDto() {

  }
}
