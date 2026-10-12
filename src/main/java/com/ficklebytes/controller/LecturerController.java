package com.ficklebytes.controller;

import com.DtoMapper;
import com.ficklebytes.dto.UserDto;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.enums.Role;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.exception.UserNotFoundException;
import com.ficklebytes.request.user.UpdateTeacherRequest;
import com.ficklebytes.response.APIResponse;
import com.ficklebytes.service.UserService;
import com.ficklebytes.utils.AuthUtil;
import com.ficklebytes.utils.CloudinaryUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/lecturer")
public class LecturerController {

  @Autowired
  private UserService userService;

  @PostMapping(value = "/update-lecturer-picture")
  @ResponseBody
  public ResponseEntity<APIResponse> updateLecturerPicture(@AuthenticationPrincipal AcademyUser academyUser,
      @RequestParam("picture") MultipartFile aFile,
      @RequestParam("lecturerId") int lecturerId)
      throws UnAuthorizedException, IOException, UserNotFoundException {
    AuthUtil.requiresAdminRole(academyUser);
    APIResponse apiResponse = new APIResponse();
    AcademyUser lecturerUser = userService.GetUserById(lecturerId);
    if(lecturerUser == null) {
        apiResponse.setAll(404, "No Users found", null);
    
  } else if (lecturerUser.getRole() != Role.Lecturer) {
    	apiResponse.setAll(404, "The user isn't lecturer", null);
    } 
    else {
    	File file = Files.createTempFile("temp",
    			aFile.getOriginalFilename()).toFile();
    	aFile.transferTo(file);
    	String pictureURL = CloudinaryUtil.UploadImage(file, "mawhoubacademy/lecturer/" + lecturerId);
    	lecturerUser.setPictureUrl(pictureURL);
    	 UserDto userDto = DtoMapper.ToDto(userService.Save(lecturerUser));
     	apiResponse.setAll(200, "Lecturer picture updated successfully", userDto);    	
    }
    return ResponseEntity.ok(apiResponse);
  }

  @PostMapping(value = "/update-lecturer-info")
  @ResponseBody
  public ResponseEntity<?> updateLecturerInfo(@AuthenticationPrincipal AcademyUser academyUser,
      @RequestBody UpdateTeacherRequest request)
      throws UnAuthorizedException, UserNotFoundException {
      APIResponse apiResponse = new APIResponse();

    AuthUtil.requiresAdminRole(academyUser);
    AcademyUser lecturerUser = userService.GetUserById(request.getUserId());
    
    if(lecturerUser == null) {
        apiResponse.setAll(404, "No Users found", null);
    }
    else if (lecturerUser.getRole() != Role.Lecturer) {
    	apiResponse.setAll(404, "The user isn't lecturer", null);
    } else {
    	lecturerUser.setFullName(request.getFullName());
    	lecturerUser.setBiography(request.getBiography());
    	UserDto userDto = DtoMapper.ToDto(userService.Save(lecturerUser));
    	apiResponse.setAll(200, "Lecturer info updated successfully", userDto);       	
    }
    return ResponseEntity.ok().body(apiResponse);
  }

  @GetMapping(value = "/list-lecturers")
  @ResponseBody
  public ResponseEntity<List<UserDto>> returnAllLecturers() {
    return ResponseEntity.ok(userService.GetAllLecturers());
  }

  @GetMapping(value = "/get-lecturer-info")
  @ResponseBody
  public ResponseEntity<UserDto> getLecturerInfo(@RequestParam("lecturerId") int lecturerId)
      throws UserNotFoundException {
    UserDto userDto = DtoMapper.ToDto(userService.GetUserById(lecturerId));
    if (userDto.getRole() == Role.Lecturer) {
      return ResponseEntity.ok(userDto);
    } else {
      throw new UserNotFoundException("The user is not a lecturer");
    }
  }
  

  
}
