package com.ficklebytes.controller;

import com.DtoMapper;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Course;
import com.ficklebytes.enums.Role;
import com.ficklebytes.enums.Semester;
import com.ficklebytes.enums.Visibility;
import com.ficklebytes.exception.CourseNotFoundException;
import com.ficklebytes.exception.UnAuthenticationException;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.request.course.CreateCourseRequest;
import com.ficklebytes.request.course.EditCourseRequest;
import com.ficklebytes.service.CourseService;
import com.ficklebytes.utils.AuthUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping(value = "/get")
    public ResponseEntity get(@RequestParam("courseId") int courseId) throws CourseNotFoundException {
        try {
            return ResponseEntity.ok(DtoMapper.ToDto(courseService.getCourseById(courseId)));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Mapping error: " + ex.getMessage());
        }
    }

    @GetMapping(value = "/getBySchoolId")
    public ResponseEntity getBySchoolId(@RequestParam("schoolId") int schoolId, @RequestParam(value = "fieldId", required = false, defaultValue =
            "-1") int fieldId) throws CourseNotFoundException, UnAuthenticationException {

        return ResponseEntity.ok(courseService.GetAllCoursesBySchoolId(schoolId, fieldId));
    }

    @GetMapping(value = "/author/{authorId}")
    public ResponseEntity getTeacherCourses(
            @AuthenticationPrincipal AcademyUser academyUser,
            @PathVariable int authorId)
            throws UnAuthorizedException {
        AuthUtil.requiresAdminOrTeacherRole(academyUser);
        if (academyUser.getRole() == Role.TEACHER && academyUser.getUserId() != authorId) {
            throw new UnAuthorizedException();
        }
        return ResponseEntity.ok(DtoMapper.CourseWithoutAuthorListDto(courseService.GetTeacherCourses(authorId)));
    }

    @GetMapping(value = "/list-all")
    public ResponseEntity listAll(@AuthenticationPrincipal AcademyUser academyUser)
            throws UnAuthorizedException {
        AuthUtil.requiresAdminRole(academyUser);
        return ResponseEntity.ok(courseService.GetAllCourses());
    }

    @GetMapping(value = "/list-public")
    public ResponseEntity listPublic(
            @RequestParam(value = "fieldId", required = false, defaultValue =
                    "-1") int fieldId) {
        return ResponseEntity.ok(DtoMapper.CourseListDto(courseService.GetPublicCourses(fieldId)));
    }


    @PostMapping(value = "/create")
    @CacheEvict(value = "coursesBySchoolAndField", allEntries = true)
    public ResponseEntity create(@AuthenticationPrincipal AcademyUser academyUser
            , @RequestBody CreateCourseRequest request) throws UnAuthorizedException {
        AuthUtil.requiresAdminRole(academyUser);

        Course course = Course.builder()
                .creationTimeDate(LocalDateTime.now(ZoneOffset.UTC))
                .name(request.getName())
                .schoolId(request.getSchoolId())
                .authorId(academyUser.getUserId())
                .author(academyUser)
                .createdBy(academyUser.getUserId())
                .creator(academyUser)
                .description("")
                .visibility(Visibility.PRIVATE)
                .category("")
                .grade("")
                .price(0)
                .originalPrice(0)
                .fieldId(-1)
                .studyId(-1)
                .semester(Semester.FIRST)
                .build();
        course = courseService.save(course);
        return ResponseEntity.ok(DtoMapper.ToDto(course));
    }

    @CacheEvict(value = "coursesBySchoolAndField", allEntries = true)
    @PostMapping(value = "/edit")
    public ResponseEntity edit(@AuthenticationPrincipal AcademyUser academyUser,
                               @RequestBody EditCourseRequest request)
            throws UnAuthorizedException, CourseNotFoundException {
        AuthUtil.requiresAdminRole(academyUser);
        Course course = courseService.getCourseById(request.getCourseId());
        course.setPrice(request.getPrice());
        course.setSemester(request.getSemester());
        course.setOriginalPrice(request.getOriginalPrice());
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setAuthorId(request.getAuthorId());
        course.setCategory(request.getCategory());
        course.setGrade(request.getGrade());
        course.setVisibility(request.getVisibility());
        course.setFieldId(request.getFieldId());
        course.setStudyId(-1);
        courseService.save(course);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/delete")
    @CacheEvict(value = "coursesBySchoolAndField", allEntries = true)
    public ResponseEntity delete(@AuthenticationPrincipal AcademyUser academyUser,
                                 @RequestParam("courseId") int courseId)
            throws UnAuthorizedException, CourseNotFoundException {
        AuthUtil.requiresAdminRole(academyUser);
        Course course = courseService.getCourseById(courseId);
        course.setVisibility(Visibility.DELETED);
        courseService.save(course);
        return ResponseEntity.ok().build();
    }


}
