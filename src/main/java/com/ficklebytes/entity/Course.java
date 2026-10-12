package com.ficklebytes.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ficklebytes.config.JacksonConfig;
import com.ficklebytes.enums.Semester;
import com.ficklebytes.enums.Visibility;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "course")
public class Course implements Serializable {


  @Column(name = "course_id", unique = true, nullable = false)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int courseId;

  @Column(name = "author_id")
  private int authorId;

  @Column(name = "created_by")
  private int createdBy;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "creation_time_date")
  @JsonFormat(pattern = JacksonConfig.dateTimeFormat, timezone = JacksonConfig.timeZone, shape = Shape.STRING)
  private LocalDateTime creationTimeDate;

  @Column(name = "visibility")
  @Enumerated(EnumType.ORDINAL)
  private Visibility visibility;

  @Column(name = "semester")
  @Enumerated(EnumType.ORDINAL)
  private Semester semester;

  @Column(name = "price")
  private int price;

  @Column(name = "original_price")
  private Integer originalPrice;

  @Column(name = "category")
  private String category;

  @Column(name = "field_id")
  private int fieldId;

  @Column(name = "grade")
  private String grade;

  @Column(name = "school_id")
  private Integer schoolId;
  
  @Column(name = "study_id")
  private Integer studyId;
  
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", referencedColumnName = "course_id")
  private Set<Content> contentSet;

  @OneToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "author_id", referencedColumnName = "user_id",
      nullable = false, insertable = false, updatable = false)
  private AcademyUser author;
  @OneToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "created_by", referencedColumnName = "user_id",
      nullable = false, insertable = false, updatable = false)
  private AcademyUser creator;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "course")
  @LazyCollection(LazyCollectionOption.EXTRA)
  private Set<Purchase> purchases;

  @Transient
  private long purchasesCount;

}
