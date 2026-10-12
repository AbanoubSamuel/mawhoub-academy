package com.ficklebytes.controller;

import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.BusinessInfo;
import com.ficklebytes.entity.Comment;
import com.ficklebytes.entity.Content;
import com.ficklebytes.entity.Course;
import com.ficklebytes.enums.Role;
import com.ficklebytes.exception.CommentNotFoundException;
import com.ficklebytes.exception.ContentNotFoundException;
import com.ficklebytes.exception.CourseNotFoundException;
import com.ficklebytes.exception.UnAuthenticationException;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.request.comment.AddCommentRequest;
import com.ficklebytes.request.configuration.BusinessInfoRequest;
import com.ficklebytes.request.course.EditCourseRequest;
import com.ficklebytes.response.APIResponse;
import com.ficklebytes.service.BusinessInfoService;
import com.ficklebytes.service.CommentService;
import com.ficklebytes.service.ContentService;
import com.ficklebytes.service.CourseService;
import com.ficklebytes.service.PurchaseService;
import com.ficklebytes.service.UserService;
import com.ficklebytes.utils.AuthUtil;

import java.sql.Array;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("**")
@RequestMapping("/api/configuration")
@Log4j2
public class BusinessInfoController {

    @Autowired
    private BusinessInfoService businessInfoService;

    @GetMapping(value = "/list")
    public ResponseEntity getConfiguration()
            {
				log.info("getConfiguration Request");

				APIResponse apiResponse = new APIResponse();
    	try {
    		BusinessInfo businessInfo = businessInfoService.getBusinessInfo();
    		apiResponse.setAll(200, "Business basic data retrieved successfully", businessInfo);
    	}
    	catch (Exception e) {
    		apiResponse.setAll(404, e.getMessage(), null);
		}
    	return ResponseEntity.ok().body(apiResponse);
    }
    
    @PostMapping(value = "/update")
    public ResponseEntity updateConfiguration(@AuthenticationPrincipal AcademyUser academyUser, @RequestBody BusinessInfo request)
            throws UnAuthorizedException {
    	AuthUtil.requiresAdminRole(academyUser);
    	APIResponse apiResponse = new APIResponse();
    	try {
    		BusinessInfo businessInfo = businessInfoService.updateBusinessInfo(request);
    		apiResponse.setAll(200, "Business basic data updated successfully", businessInfo);
    	}
    	catch (Exception e) {
    		apiResponse.setAll(404, e.getMessage(), null);
		}
        return ResponseEntity.ok().body(apiResponse);
    }

}
