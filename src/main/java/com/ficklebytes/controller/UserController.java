package com.ficklebytes.controller;

import com.DtoMapper;
import com.ficklebytes.dto.UserDto;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.enums.Role;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.exception.UserNotFoundException;
import com.ficklebytes.request.auth.UpdateUserRoleRequest;
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
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping(value = "/update-teacher-picture")
  @ResponseBody
  public ResponseEntity updateTeacherPicture(@AuthenticationPrincipal AcademyUser academyUser,
      @RequestParam("picture") MultipartFile aFile,
      @RequestParam("teacherId") int teacherId)
      throws UnAuthorizedException, IOException, UserNotFoundException {
    AuthUtil.requiresAdminRole(academyUser);
    AcademyUser teacherUser = userService.GetUserById(teacherId);
    if (teacherUser.getRole() != Role.TEACHER) {
      throw new UserNotFoundException("The user isn't teacher");
    }
    File file = Files.createTempFile("temp",
        aFile.getOriginalFilename()).toFile();
    aFile.transferTo(file);
    String pictureURL = CloudinaryUtil.UploadImage(file, "mawhoubacademy/teachers/" + teacherId);
    teacherUser.setPictureUrl(pictureURL);
    userService.Save(teacherUser);
    return ResponseEntity.ok(pictureURL);
  }

  @PostMapping(value = "/update-teacher-info")
  @ResponseBody
  public ResponseEntity updateTeacherInfo(@AuthenticationPrincipal AcademyUser academyUser,
      @RequestBody UpdateTeacherRequest request)
      throws UnAuthorizedException, UserNotFoundException {
    AuthUtil.requiresAdminRole(academyUser);
    AcademyUser teacherUser = userService.GetUserById(request.getUserId());
    if (teacherUser.getRole() != Role.TEACHER) {
      throw new UserNotFoundException("The user isn't teacher");
    }
    teacherUser.setFullName(request.getFullName());
    teacherUser.setBiography(request.getBiography());
    userService.Save(teacherUser);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/list-teachers")
  @ResponseBody
  public ResponseEntity returnAllTeachers() {
    return ResponseEntity.ok(userService.GetAllTeachers());
  }
  
  

  

  @GetMapping(value = "/get-teacher-info")
  @ResponseBody
  public ResponseEntity getTeacherInfo(@RequestParam("teacherId") int teacherId)
      throws UserNotFoundException {
    UserDto userDto = DtoMapper.ToDto(userService.GetUserById(teacherId));
    if (userDto.getRole() == Role.TEACHER) {
      return ResponseEntity.ok(userDto);
    } else {
      throw new UserNotFoundException("The user is not a teacher");
    }
  }
  
  @PostMapping(value = "/update-user-role")
  public ResponseEntity < APIResponse > updateUserRole(@RequestBody UpdateUserRoleRequest request,
      @AuthenticationPrincipal AcademyUser academyUser) throws UnAuthorizedException {

      AuthUtil.requiresAdminRole(academyUser);

      APIResponse apiResponse = new APIResponse();

      try {
          int rowsAffected = userService.updateUserRole(request);
          if (rowsAffected > 0) {
              apiResponse.setAll(200, "User " + request.getUsername() + " assigened to " + request.getRole() + " successfully", null);
          } else {
              apiResponse.setAll(404, "No User Found", null);
          }
      } catch (Exception e) {
          apiResponse.setAll(404, e.getMessage(), null);
      }
      return ResponseEntity.ok().body(apiResponse);

  }
  
  @GetMapping(value = "/list-all")
  public ResponseEntity< APIResponse > getUsers(@AuthenticationPrincipal AcademyUser academyUser) throws UnAuthorizedException {
      AuthUtil.requiresAdminRole(academyUser);
      APIResponse apiResponse = new APIResponse();

      try {
          apiResponse.setAll(200, "Users retrieved successfully", userService.getAllUsers());
      } catch (Exception e) {
          apiResponse.setAll(404, e.getMessage(), new ArrayList <> ());
      }
      return ResponseEntity.ok().body(apiResponse);
  }
  
}
