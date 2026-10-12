package com.ficklebytes.service;


import com.DtoMapper;
import com.ficklebytes.dto.CustomUserDto;
import com.ficklebytes.dto.UserDto;
import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.enums.Role;
import com.ficklebytes.exception.UserNotFoundException;
import com.ficklebytes.repository.AcademyUserRepository;
import com.ficklebytes.request.auth.UpdateUserRoleRequest;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

  @Autowired
  private AcademyUserRepository academyUserRepository;

  public boolean IsEmailExists(String email) {
    return academyUserRepository.findByEmailIgnoreCase(email).isPresent();
  }

  public List<CustomUserDto> getAllUsers() {
	  List<AcademyUser> users = (List<AcademyUser>) academyUserRepository.findAll();
	  return  DtoMapper.CustomUserListDto(users);
}
  
  public List<UserDto> GetAllTeachers() {
    return DtoMapper.UserListDto(academyUserRepository.findAllByRoleEquals(Role.TEACHER));
  }
  
  public List<UserDto> GetAllLecturers() {
	    return DtoMapper.UserListDto(academyUserRepository.findAllByRoleEquals(Role.Lecturer));
}

  public AcademyUser GetUserById(int userId) throws UserNotFoundException {
    Optional<AcademyUser> user = academyUserRepository.findById(userId);
    if (!user.isPresent()) {
      throw new UserNotFoundException("Invalid email or username");
    }
    return user.get();
  }

  public AcademyUser GetUserByEmailOrUsername(String emailOrUsername) throws UserNotFoundException {
    AcademyUser user =
        academyUserRepository.findByEmailIgnoreCase(emailOrUsername).orElse(null);
    if (user == null) {
      throw new UserNotFoundException("Invalid email or username");
    }
    return user;
  }


  public AcademyUser Save(AcademyUser user) {
    return academyUserRepository.save(user);
  }

  
  public int  updateUserRole(UpdateUserRoleRequest req) {
     return academyUserRepository.updateUserRole(req.getUserId(), req.getRole());
  }

  public Optional<AcademyUser> getUserByEmail(String email) throws UserNotFoundException {
    Optional<AcademyUser> user = academyUserRepository.findByEmailIgnoreCase(email);
    if (!user.isPresent()) {
      throw new UserNotFoundException("Email doesn't exist");
    }
    return user;
  }

}
