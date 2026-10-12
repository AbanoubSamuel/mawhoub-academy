package com.ficklebytes.controller;

import com.DtoMapper;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Lecture;
import com.ficklebytes.enums.Role;
import com.ficklebytes.enums.Semester;
import com.ficklebytes.enums.Visibility;
import com.ficklebytes.exception.LectureNotFoundException;
import com.ficklebytes.exception.UnAuthenticationException;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.request.lecture.CreateLectureRequest;
import com.ficklebytes.request.lecture.EditLectureRequest;
import com.ficklebytes.response.APIResponse;
import com.ficklebytes.service.LectureService;
import com.ficklebytes.utils.AuthUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/lecture")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @GetMapping(value = "/get")
    public ResponseEntity get(@RequestParam("lectureId") int lectureId) throws LectureNotFoundException {
        return ResponseEntity.ok(DtoMapper.ToDto(lectureService.GetLectureById(lectureId)));
    }
    
    @GetMapping(value = "/getByStudyId")
    public ResponseEntity getByStudyId(@RequestParam("studyId") int studyId,@RequestParam(value = "fieldId", required = false, defaultValue =
            "-1") int fieldId) throws UnAuthenticationException {
    	return ResponseEntity.ok(DtoMapper.LecturesListDto(lectureService.GetAllLecturesByStudyId(studyId,fieldId)));
    }

    @GetMapping(value = "/author/{authorId}")
    public ResponseEntity getLectureLectures(
            @AuthenticationPrincipal AcademyUser academyUser,
            @PathVariable int authorId)
            throws UnAuthorizedException {
        AuthUtil.requiresAdminOrLecturerRole(academyUser);
        if (academyUser.getRole() == Role.Lecturer && academyUser.getUserId() != authorId) {
            throw new UnAuthorizedException();
        }
        return ResponseEntity.ok(DtoMapper.LectureWithoutAuthorListDto(lectureService.GetLectureLectures(authorId)));
    }

    @GetMapping(value = "/list-all")
    public ResponseEntity listAll(@AuthenticationPrincipal AcademyUser academyUser)
            throws UnAuthorizedException {
        AuthUtil.requiresAdminRole(academyUser);
        return ResponseEntity.ok(DtoMapper.LecturesListDto(lectureService.GetAllLectures()));
    }

    @GetMapping(value = "/list-public")
    public ResponseEntity listPublic(
            @RequestParam(value = "fieldId", required = false, defaultValue =
                    "-1") int fieldId) {
        return ResponseEntity.ok(DtoMapper.LecturesListDto(lectureService.GetPublicLectures(fieldId)));
    }

    @PostMapping(value = "/create")
    public ResponseEntity create(@AuthenticationPrincipal AcademyUser academyUser
            , @RequestBody CreateLectureRequest request) throws UnAuthorizedException {
        AuthUtil.requiresAdminRole(academyUser);
        APIResponse apiResponse = new APIResponse();
        Lecture lecture = Lecture.builder()
                .creationTimeDate(LocalDateTime.now(ZoneOffset.UTC))
                .name(request.getName())
                .schoolId(-1)
                .studyId(request.getStudyId())
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
                .semester(Semester.FIRST)
                .build();
        lecture = lectureService.Save(lecture);
        apiResponse.setAll(200, "Lecture created successfully", DtoMapper.ToDto(lecture));
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(value = "/edit")
    public ResponseEntity edit(@AuthenticationPrincipal AcademyUser academyUser,
                               @RequestBody EditLectureRequest request)
            throws UnAuthorizedException, LectureNotFoundException {
        AuthUtil.requiresAdminRole(academyUser);
        APIResponse apiResponse = new APIResponse();
        Lecture lecture = lectureService.GetLectureById(request.getLectureId());
        lecture.setPrice(request.getPrice());
        lecture.setSemester(request.getSemester());
        lecture.setOriginalPrice(request.getOriginalPrice());
        lecture.setName(request.getName());
        lecture.setDescription(request.getDescription());
        lecture.setAuthorId(request.getAuthorId());
        lecture.setCategory(request.getCategory());
        lecture.setGrade(request.getGrade());
        lecture.setVisibility(request.getVisibility());
        lecture.setFieldId(request.getFieldId());
        lecture.setSchoolId(-1);
        
        lecture = lectureService.Save(lecture);
        apiResponse.setAll(200, "Lecture updated successfully", DtoMapper.ToDto(lecture));
        return ResponseEntity.ok(apiResponse);

    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity delete(@AuthenticationPrincipal AcademyUser academyUser,
                                 @RequestParam("lectureId") int lectureId)
            throws UnAuthorizedException, LectureNotFoundException {
        AuthUtil.requiresAdminRole(academyUser);
        Lecture lecture = lectureService.GetLectureById(lectureId);
        lecture.setVisibility(Visibility.DELETED);
        lectureService.Save(lecture);
        return ResponseEntity.ok().build();
    }


}
