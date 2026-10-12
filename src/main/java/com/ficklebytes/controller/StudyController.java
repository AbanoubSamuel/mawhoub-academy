package com.ficklebytes.controller;

import com.DtoMapper;
import com.ficklebytes.dto.StudyDto;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Study;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.response.APIResponse;
import com.ficklebytes.service.StudyService;
import com.ficklebytes.utils.AuthUtil;
import com.google.api.gax.rpc.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/study")
public class StudyController {

	 @Autowired
	    private StudyService studyService;

	    @GetMapping(value = "/list-all")
	    public ResponseEntity get() throws NotFoundException {
	    	
	 		APIResponse apiResponse = new APIResponse();
	 		try {
	 			List<StudyDto> schools = DtoMapper.StudiesListDto(studyService.getStudies());
	    		apiResponse.setAll(200, "Studies retrieved successfully", schools);
	    	}
	    	catch (Exception e) {
				apiResponse.setAll(404, e.getMessage(), new ArrayList<>());
			}
	    	return ResponseEntity.ok().body(apiResponse);
	    }
	    
	    
	    @PostMapping(value = "/add")
	    public ResponseEntity add(@AuthenticationPrincipal AcademyUser academyUser, @RequestBody Study study) throws NotFoundException, UnAuthorizedException {
	    	AuthUtil.requiresAdminRole(academyUser);
	 		APIResponse apiResponse = new APIResponse();
	    	try {
	 			Study newStudy = studyService.addStudy(study);
	    		apiResponse.setAll(200, "Study added successfully", newStudy);
	    	}
	    	catch (Exception e) {
				apiResponse.setAll(404, e.getMessage(), new ArrayList<>());
			}
	    	return ResponseEntity.ok().body(apiResponse);
	    }

	    
	    @DeleteMapping(value = "/delete")
	    public ResponseEntity delete(@AuthenticationPrincipal AcademyUser academyUser, @RequestParam(name = "id") int id) throws NotFoundException, UnAuthorizedException {
	    	try {
	    		return ResponseEntity.ok().body(studyService.deleteStudy(id));
	    	}
	    	catch (Exception e) {
	    		APIResponse apiResponse = new APIResponse();
				apiResponse.setAll(404, e.getMessage(), null);
				return ResponseEntity.ok().body(apiResponse);
			}
	    	
	    }


}
