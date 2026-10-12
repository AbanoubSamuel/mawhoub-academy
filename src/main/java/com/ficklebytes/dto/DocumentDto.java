package com.ficklebytes.dto;


import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentDto implements Serializable {

  private int documentId;

  private int contentId;

  private String documentName;

}
