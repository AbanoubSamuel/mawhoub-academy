package com.ficklebytes.dto;


import com.DtoMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.ficklebytes.config.JacksonConfig;
import com.ficklebytes.entity.Purchase;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseDto {

  private CourseDto course;
  private int userId;

  @JsonFormat(pattern = JacksonConfig.dateTimeFormat, timezone = JacksonConfig.timeZone, shape = Shape.STRING)
  private LocalDateTime activationTimeDate;

  public PurchaseDto(Purchase purchase) {
    this.course = DtoMapper.ToDto(purchase.getCourse());
    this.userId = purchase.getUserId();
    this.activationTimeDate = purchase.getActivationTimeDate();
  }
}
