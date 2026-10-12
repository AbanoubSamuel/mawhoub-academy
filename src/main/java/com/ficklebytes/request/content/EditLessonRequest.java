package com.ficklebytes.request.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ficklebytes.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditLessonRequest {

  @JsonProperty(value = "contentId", required = true)
  private int contentId;

  @JsonProperty(value = "title", required = true)
  private String title;

  @JsonProperty(value = "videoId", required = false)
  private String videoId;

  @JsonProperty(value = "visibility", required = true)
  private Visibility visibility;

  @JsonProperty(value = "free", required = true)
  private boolean free;

}
