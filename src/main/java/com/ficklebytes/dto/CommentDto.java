package com.ficklebytes.dto;


import com.DtoMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.ficklebytes.config.JacksonConfig;
import com.ficklebytes.entity.Comment;
import com.ficklebytes.service.CommentService;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto implements Serializable {

  private int id;
  private Integer parentCommentId;
  private String description;
  private boolean deleted;
  @JsonFormat(pattern = JacksonConfig.dateTimeFormat, timezone = JacksonConfig.timeZone, shape = Shape.STRING)
  private LocalDateTime creationTimestamp;
  private UserDto author;
  private boolean anonymous;
  private List<CommentDto> childComments;
  private boolean seenByTeacher;
  public CommentDto(Comment comment) {
    this.id = comment.getId();
    this.deleted = comment.isDeleted();
    this.description = comment.getDescription();
    this.creationTimestamp = comment.getCreationTimestamp();
    this.anonymous = comment.isAnonymous();
    this.seenByTeacher=comment.isSeenByTeacher();
    if (!this.anonymous) {
      this.author = DtoMapper.ToDto(comment.getAuthor());
    }
    this.childComments = new ArrayList<>();
    for (Comment childComment : comment.getChildComments()) {
      childComments.add(CommentService.ToDto(childComment));
    }
  }

}
