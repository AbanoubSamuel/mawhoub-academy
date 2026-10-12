package com.ficklebytes.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.ficklebytes.config.JacksonConfig;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "comment")
public class Comment implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private int id;

  @Column(name = "content_id")
  private int contentId;

  @Column(name = "author_id")
  private int authorId;

  @Column(name = "parent_comment_id")
  private Integer parentCommentId;

  @Column(name = "description")
  private String description;

  @Column(name = "deleted")
  private boolean deleted;

  @Column(name = "creation_time_date")
  @JsonFormat(pattern = JacksonConfig.dateTimeFormat, timezone = JacksonConfig.timeZone, shape = Shape.STRING)
  private LocalDateTime creationTimestamp;

  @Column(name = "anonymous")
  private boolean anonymous;

  @Column(name = "seen_by_teacher")
  private boolean seenByTeacher;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "content_id", referencedColumnName = "content_id",
      nullable = false, insertable = false, updatable = false)
  private Content content;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "author_id", referencedColumnName = "user_id",
      nullable = false, insertable = false, updatable = false)
  private AcademyUser author;

//Not used
  @OneToMany(mappedBy = "parentCommentId", fetch = FetchType.LAZY)
  private Set<Comment> comments = new HashSet<>();


  @Transient
  private List<Comment> childComments=new ArrayList<>();
}
