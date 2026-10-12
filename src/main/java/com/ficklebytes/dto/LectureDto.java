package com.ficklebytes.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.ficklebytes.config.JacksonConfig;
import com.ficklebytes.enums.Semester;
import com.ficklebytes.enums.Visibility;
import com.google.firebase.database.PropertyName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureDto {

	@PropertyName("lectureId")
	  private int courseId;
  private int studyId;
  private int price;
  private int originalPrice;
  private int fieldId;
  private UserDto author;
  private UserDto creator;
  private int authorId;
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
