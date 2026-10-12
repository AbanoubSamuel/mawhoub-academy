package com.ficklebytes.request.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ficklebytes.enums.Semester;
import com.ficklebytes.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditCourseRequest {

  @JsonProperty(value = "courseId", required = true)
  private int courseId;

  @JsonProperty(value = "price", required = true)
  private int price;
  @JsonProperty(value = "originalPrice", required = true)
  private int originalPrice;


  @JsonProperty(value = "name", required = true)
  private String name;

  @JsonProperty(value = "description", required = true)
  private String description;

  @JsonProperty(value = "authorId", required = true)
  private int authorId;

  @JsonProperty(value = "category", required = true)
  private String category;

  @JsonProperty(value = "grade", required = true)
  private String grade;

  @JsonProperty(value = "visibility", required = true)
  private Visibility visibility;

  @JsonProperty(value = "fieldId", required = true)
  private int fieldId;

  @JsonProperty(value = "semester", required = true)
  private Semester semester;
  
  @JsonProperty(value = "school_id")
  private Integer schoolId;

  @JsonProperty(value = "study_id")
  private Integer studyId;

}
