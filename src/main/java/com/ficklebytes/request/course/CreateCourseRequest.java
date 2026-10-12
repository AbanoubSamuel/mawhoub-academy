package com.ficklebytes.request.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCourseRequest {

  @JsonProperty(value = "name", required = true)
  private String name;
  
  @JsonProperty(value = "schoolId", required = true)
  private int schoolId;


}
