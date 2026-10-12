package com.ficklebytes.controller;

import com.DtoMapper;
import com.ficklebytes.dto.SchoolDto;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Course;
import com.ficklebytes.entity.School;
import com.ficklebytes.enums.Role;
import com.ficklebytes.enums.Semester;
import com.ficklebytes.enums.Visibility;
import com.ficklebytes.exception.CourseNotFoundException;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.request.course.CreateCourseRequest;
import com.ficklebytes.request.course.EditCourseRequest;
import com.ficklebytes.response.APIResponse;
import com.ficklebytes.service.CourseService;
import com.ficklebytes.service.SchoolService;
import com.ficklebytes.utils.AuthUtil;
import com.google.api.gax.rpc.NotFoundException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/school")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    @GetMapping(value = "/list-all")
    public ResponseEntity get() throws NotFoundException {
    	
 		APIResponse apiResponse = new APIResponse();
 		try {
 			List<SchoolDto> schools = DtoMapper.SchoolsListDto(schoolService.getSchools());
    		apiResponse.setAll(200, "Schools retrieved successfully", schools);
    	}
    	catch (Exception e) {
			apiResponse.setAll(404, e.getMessage(), new ArrayList<>());
		}
    	return ResponseEntity.ok().body(apiResponse);
    }
    
    
    @PostMapping(value = "/add")
    public ResponseEntity add(@AuthenticationPrincipal AcademyUser academyUser, @RequestBody School school) throws NotFoundException, UnAuthorizedException {
    	AuthUtil.requiresAdminRole(academyUser);
 		APIResponse apiResponse = new APIResponse();
    	try {
			school.setVisibility(1);
 			School newSchool = schoolService.addSchool(school);
    		apiResponse.setAll(200, "School added successfully", newSchool);
    	}
    	catch (Exception e) {
			apiResponse.setAll(404, e.getMessage(), new ArrayList<>());
		}
    	return ResponseEntity.ok().body(apiResponse);
    }

    
    @DeleteMapping(value = "/delete")
    public ResponseEntity delete(@AuthenticationPrincipal AcademyUser academyUser, @RequestParam int id) throws NotFoundException, UnAuthorizedException {
    	AuthUtil.requiresAdminRole(academyUser);
    	try {
    		return ResponseEntity.ok().body(schoolService.deleteSchool(id));
    	}
    	catch (Exception e) {
    		APIResponse apiResponse = new APIResponse();
			apiResponse.setAll(404, e.getMessage(), null);
			return ResponseEntity.ok().body(apiResponse);
		}
    	
    }


}
