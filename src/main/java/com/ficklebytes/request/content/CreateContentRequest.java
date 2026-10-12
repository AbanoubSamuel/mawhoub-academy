package com.ficklebytes.request.content;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.ficklebytes.config.JacksonConfig;
import com.ficklebytes.entity.Course;
import com.ficklebytes.entity.Document;
import com.ficklebytes.enums.ContentType;
import com.ficklebytes.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateContentRequest {

  @JsonProperty(value = "title", required = true)
  private String title;

  @JsonProperty(value = "description", required = false)
  private String description;

  
  @JsonProperty(value = "video_id", required = false)
  private String videoId;
  
  @JsonProperty(value = "video_url", required = false)
  private String videoUrl;

  
  @JsonProperty(value = "is_free", required = true)
  private int free;
  
  @JsonProperty(value = "length", required = true)
  private int length;
  
  
  @JsonProperty(value = "creation_time_date", required = true)
  @JsonFormat(pattern = JacksonConfig.dateTimeFormat, timezone = JacksonConfig.timeZone, shape = Shape.STRING)
  private LocalDateTime creationTimeDate;

  @JsonProperty(value = "visibility", required = true)
  private Visibility visibility;
  
  @Enumerated(EnumType.ORDINAL)
  @JsonProperty(value = "type", required = true)
  private ContentType type;

  
  @JsonProperty(value = "courseId", required = true)
  private int courseId;
  
  
  @JsonProperty(value = "order_index", required = true)
  private int order;
 
}