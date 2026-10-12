package com.ficklebytes.request.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCommentRequest {

  @JsonProperty(value = "parentCommentId", required = true)
  private Integer parentCommentId;

  @JsonProperty(value = "contentId", required = true)
  private int contentId;

  @JsonProperty(value = "anonymous", required = true)
  private boolean anonymous;

  @JsonProperty(value = "description", required = true)
  private String description;

}
