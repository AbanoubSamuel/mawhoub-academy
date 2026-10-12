package com.ficklebytes.service;


import com.DtoMapper;
import com.ficklebytes.dto.CourseDto;
import com.ficklebytes.dto.UserDto;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Course;
import com.ficklebytes.enums.Visibility;
import com.ficklebytes.exception.CourseNotFoundException;
import com.ficklebytes.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ficklebytes.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class CourseService {


    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;

    public Course getCourseById(int courseId) throws CourseNotFoundException {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Invalid course Id"));
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public List<CourseDto> GetAllCourses(){

        return courseRepository.getAllCoursesRaw().stream().map(obj -> {
            Course c = (Course) obj[0];
            AcademyUser author = (AcademyUser) obj[1];
            AcademyUser creator = (AcademyUser) obj[2];
            long purchaseCount = (Long) obj[3];
            c.setAuthor(author);
            c.setCreator(creator);
            c.setPurchasesCount(purchaseCount);
            return DtoMapper.ToDto(c);
        }).collect(Collectors.toList());
    }

    public List<CourseDto> GetAllCoursesBySchoolId(int studyId, int fieldId) {
        return DtoMapper.CoursesListDto(courseRepository.findBySchoolIdAndFieldWithRelations(studyId, fieldId));
    }

    public List<Course> GetPublicCourses(int fieldId) {
        ArrayList<Course> courses = new ArrayList<>();
        for (Course course : courseRepository.getAllCourses()) {
            if (course.getVisibility() == Visibility.PUBLIC && (course.getSchoolId() != null && course.getSchoolId() != -1)
                    && (fieldId == -1 || course.getFieldId() == fieldId)) {
                courses.add(course);
            }
        }
        return courses;
    }

    public List<Course> GetPublicStudies(int fieldId) {
        ArrayList<Course> courses = new ArrayList<>();
        for (Course course : courseRepository.getAllCourses()) {
            if (course.getVisibility() == Visibility.PUBLIC && (course.getSchoolId() != null && course.getSchoolId() != -1)
                    && (fieldId == -1 || course.getFieldId() == fieldId)) {
                courses.add(course);
            }
        }
        return courses;
    }

    public List<Course> GetTeacherCourses(int userId) {
        ArrayList<Course> courses = new ArrayList<>();
        for (Course course : courseRepository.getAllByAuthorId(userId)) {
            course.setPurchasesCount(purchaseRepository.countAllByCourseId(course.getCourseId()));
            courses.add(course);
        }
        return courses;
    }

}
