package com.ficklebytes.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.ficklebytes.config.JacksonConfig;

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
@Entity()
@Table(name = "specialty")
public class Specialty implements Serializable {

		@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  @Column(name = "id")
	  private int id;

	  @Column(name = "title")
	  private String title;

	  @Column(name = "specialty_order")
	  private int order;

	@Override
	public String toString() {
		return "Specialty [id=" + id + ", title=" + title + ", order=" + order + "]";
	}
	  
	  
}