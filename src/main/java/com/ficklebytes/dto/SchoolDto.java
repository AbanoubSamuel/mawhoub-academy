package com.ficklebytes.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SchoolDto implements Serializable {

	private Integer id;
    private String name;
}
