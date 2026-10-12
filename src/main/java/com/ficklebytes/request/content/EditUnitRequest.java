package com.ficklebytes.request.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUnitRequest {

  @JsonProperty(value = "contentId", required = true)
  private int contentId;

  @JsonProperty(value = "title", required = true)
  private String title;
}
