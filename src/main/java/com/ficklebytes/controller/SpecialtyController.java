package com.ficklebytes.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.BusinessInfo;
import com.ficklebytes.entity.Specialty;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.response.APIResponse;
import com.ficklebytes.service.SpecialtyService;
import com.ficklebytes.utils.AuthUtil;
import com.google.api.gax.rpc.NotFoundException;

@RestController
@CrossOrigin
@RequestMapping("/api/specialty")
public class SpecialtyController {

	@Autowired
	private SpecialtyService specialtyService;

	@GetMapping(value = "/list-all")
	public ResponseEntity get() throws NotFoundException {
		APIResponse apiResponse = new APIResponse();
		try {
			List<Specialty> specialties = specialtyService.getAll();
			apiResponse.setAll(200, "Specialties retrieved successfully", specialties);
		} catch (Exception e) {
			apiResponse.setAll(404, e.getMessage(), new ArrayList<>());
		}
		return ResponseEntity.ok().body(apiResponse);

	}

	@PostMapping(value = "/add")
	public ResponseEntity add(@AuthenticationPrincipal AcademyUser academyUser,
			@RequestBody Specialty specialty) throws NotFoundException, UnAuthorizedException {
		AuthUtil.requiresAdminRole(academyUser);
		APIResponse apiResponse = new APIResponse();
		try {
			Specialty updatedSpecialty = specialtyService.add(specialty);
			apiResponse.setAll(200, "Specialty added successfully", updatedSpecialty);
		} catch (Exception e) {
			apiResponse.setAll(404, e.getMessage(), null);
		}
		return ResponseEntity.ok().body(apiResponse);
	}

	@DeleteMapping(value = "/delete")
	public ResponseEntity delete(@AuthenticationPrincipal AcademyUser academyUser, @RequestParam int id)
			throws NotFoundException, UnAuthorizedException {
		AuthUtil.requiresAdminRole(academyUser);
		try {
			return ResponseEntity.ok().body(specialtyService.deleteSpecialty(id));
		} catch (Exception e) {
			APIResponse apiResponse = new APIResponse();
			apiResponse.setAll(404, e.getMessage(), null);
			return ResponseEntity.ok().body(apiResponse);
		}

	}

}
