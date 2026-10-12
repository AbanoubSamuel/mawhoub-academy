package com.ficklebytes.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.ficklebytes.config.JacksonConfig;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Entity()
@Table(name = "card")
@Where(clause = "is_deleted=false")
public class Card implements Serializable {

  @Column(name = "card_id", unique = true, nullable = false)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int cardId;

  @Column(name = "card_key")
  private String key;

  @Column(name = "price")
  private int price;

  @JsonFormat(pattern = JacksonConfig.dateTimeFormat, timezone = JacksonConfig.timeZone, shape = Shape.STRING)
  private LocalDateTime creationTimeDate;

  @Column(name = "is_deleted")
  private boolean deleted;

}
