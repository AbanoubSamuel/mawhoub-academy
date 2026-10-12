package com.ficklebytes.request.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUnitRequest {

  @JsonProperty(value = "courseId", required = true)
  private int courseId;

  @JsonProperty(value = "title", required = true)
  private String title;

}
