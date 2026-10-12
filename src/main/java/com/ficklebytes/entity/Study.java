package com.ficklebytes.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "study")
public class Study  implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String lecturerName;
    private LocalDateTime creationDateTime;
    private boolean visibility;
    private double price;
    private double originalPrice;
    private Integer purchasesCount;

    @Override
    public String toString() {
        return "Study [id=" + id + 
               ", title=" + title + 
               ", lecturerName=" + lecturerName + 
               ", creationDateTime=" + creationDateTime + 
               ", visibility=" + visibility + 
               ", price=" + price + 
               ", originalPrice=" + originalPrice + 
               ", purchasesCount=" + purchasesCount + "]";
    }

}
