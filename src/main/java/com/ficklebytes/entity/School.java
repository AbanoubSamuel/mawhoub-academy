package com.ficklebytes.entity;

import java.io.Serializable;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ficklebytes.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "school")
public class School  implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@JsonIgnore
	@Column(name = "visibility")
	private int visibility;

	@Override
	public String toString() {
		return "School [id=" + id + ", name=" + name + "]";
	}

}
