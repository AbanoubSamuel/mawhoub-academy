package com.ficklebytes.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.ficklebytes.config.JacksonConfig;
import com.ficklebytes.enums.Semester;
import com.ficklebytes.enums.Visibility;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDto {

    private int courseId;
    private int schoolId;
    private int price;
    private int originalPrice;
    private int fieldId;
    private UserDto author;
    private UserDto creator;
    private int authorId;
    private int createdBy;
    private String name;
    private String description;
    private String category;
    private String grade;
    @JsonFormat(pattern = JacksonConfig.dateTimeFormat, timezone = JacksonConfig.timeZone, shape = Shape.STRING)
    private LocalDateTime creationTimeDate;
    private Visibility visibility;
    private long purchasesCount;
    private Semester semester;

    public CourseDto(){}
    public CourseDto(int courseId, int schoolId, int price, int originalPrice, int fieldId,
                     UserDto author, UserDto creator, int authorId, int createdBy, String name,
                     String description, String category, String grade, LocalDateTime creationTimeDate,
                     Visibility visibility, long purchasesCount, Semester semester) {
        this.courseId = courseId;
        this.schoolId = schoolId;
        this.price = price;
        this.originalPrice = originalPrice;
        this.fieldId = fieldId;
        this.author = author;
        this.creator = creator;
        this.authorId = authorId;
        this.createdBy = createdBy;
        this.name = name;
        this.description = description;
        this.category = category;
        this.grade = grade;
        this.creationTimeDate = creationTimeDate;
        this.visibility = visibility;
        this.purchasesCount = purchasesCount;
        this.semester = semester;
    }


}
