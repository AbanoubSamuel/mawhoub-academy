package com.ficklebytes.dto;

import java.io.Serializable;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudyDto implements Serializable {

	private Integer id;
    private String name;
}
