package com.ficklebytes.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.ficklebytes.config.JacksonConfig;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchase")
@Where(clause = "is_deleted=false")
public class Purchase implements Serializable {

  @Column(name = "purchase_id", unique = true, nullable = false)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int purchaseId;

  @Column(name = "card_id")
  private int cardId;

  @Column(name = "course_id")
  private int courseId;

  @Column(name = "user_id")
  private int userId;

  @Column(name = "activation_time_date")
  @JsonFormat(pattern = JacksonConfig.dateTimeFormat, timezone = JacksonConfig.timeZone, shape = Shape.STRING)
  private LocalDateTime activationTimeDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private AcademyUser user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", insertable = false, updatable = false)
  private Course course;

  @Column(name = "is_deleted")
  private boolean deleted;

}
