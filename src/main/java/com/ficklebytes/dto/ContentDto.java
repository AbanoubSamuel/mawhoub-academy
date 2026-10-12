package com.ficklebytes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.ficklebytes.config.JacksonConfig;
import com.ficklebytes.enums.ContentType;
import com.ficklebytes.enums.Visibility;
import com.ficklebytes.utils.Utils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentDto implements Serializable {

  private int contentId;

  private String title;

  private String description;

  private int length;

  private String videoId;

  private boolean free;

  @JsonFormat(pattern = JacksonConfig.dateTimeFormat, timezone = JacksonConfig.timeZone, shape = Shape.STRING)
  private LocalDateTime creationTimeDate;

  private Visibility visibility;

  private ContentType type;

  private int order;

  private int courseId;

  private int courseAuthorId;

  private Set<DocumentDto> documents;

  @Override
  public String toString() {
    return Utils.ClassToString(this);
  }
}
