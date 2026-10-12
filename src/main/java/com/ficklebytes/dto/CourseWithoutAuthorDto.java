package com.ficklebytes.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.ficklebytes.config.JacksonConfig;
import com.ficklebytes.enums.Semester;
import com.ficklebytes.enums.Visibility;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CourseWithoutAuthorDto {

  private int courseId;
  private int authorId;
  private int schoolId;
  private int fieldId;
  private int studyId;
  private int price;
  private int createdBy;
  private String name;
  private String description;
  private String category;
  private String grade;
  @JsonFormat(pattern = JacksonConfig.dateTimeFormat, timezone = JacksonConfig.timeZone, shape = Shape.STRING)
  private LocalDateTime creationTimeDate;
  private Visibility visibility;
  private long purchasesCount;
  private Semester semester;

}
