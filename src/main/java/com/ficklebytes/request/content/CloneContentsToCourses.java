package com.ficklebytes.request.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloneContentsToCourses {

  @JsonProperty(value = "contentIdList", required = true)
  private List<Integer> contentIdList;

  @JsonProperty(value = "courseIdList", required = true)
  private List<Integer> courseIdList;

}
