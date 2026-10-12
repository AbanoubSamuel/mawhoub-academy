package com.ficklebytes.request.lecture;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateLectureRequest {

  @JsonProperty(value = "name", required = true)
  private String name;
  
  @JsonProperty(value = "studyId", required = true)
  private int studyId;

}
