package com.ficklebytes.entity;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "document")
public class Document implements Serializable, Cloneable {

  @Column(name = "document_id")
  @Id
  private int documentId;

  @Column(name = "content_id")
  private int contentId;

  @Column(name = "document_name")
  private String documentName;

}
