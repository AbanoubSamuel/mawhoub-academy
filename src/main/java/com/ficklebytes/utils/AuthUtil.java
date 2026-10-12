package com.ficklebytes.utils;

import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.enums.Role;
import com.ficklebytes.exception.UnAuthenticationException;
import com.ficklebytes.exception.UnAuthorizedException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthUtil {

  public void requiresLoggedIn(AcademyUser academyUser) throws UnAuthenticationException {
    if (academyUser == null) {
      throw new UnAuthenticationException();
    }
  }

    public void requiresOwnerOnly(AcademyUser academyUser) throws UnAuthorizedException {
        if (academyUser == null || academyUser.getEmail() == null || !academyUser.getEmail().equalsIgnoreCase("abuaboud@yahoo.com")) {
            throw new UnAuthorizedException();
        }
    }

  public void requiresAdminRole(AcademyUser academyUser) throws UnAuthorizedException {
    if (academyUser == null || academyUser.getRole() == null || academyUser.getRole() != Role.ADMIN) {
      throw new UnAuthorizedException();
    }
  }
  public void requiresAdminOrTeacherRole(AcademyUser academyUser) throws UnAuthorizedException {
    if (academyUser == null || academyUser.getRole() == null||  (academyUser.getRole() != Role.ADMIN &&  academyUser.getRole() != Role.TEACHER)) {
      throw new UnAuthorizedException();
    }
  }
  
  public void requiresAdminOrLecturerRole(AcademyUser academyUser) throws UnAuthorizedException {
	    if (academyUser == null|| academyUser.getRole() == null || (academyUser.getRole() != Role.ADMIN &&  academyUser.getRole() != Role.Lecturer)) {
	      throw new UnAuthorizedException();
	    }
	  }
  
  public void requiresAdminOrTeacherRoleOrLecturer(AcademyUser academyUser) throws UnAuthorizedException {
	    if (academyUser == null || academyUser.getRole() == null|| (academyUser.getRole() != Role.ADMIN &&  academyUser.getRole() != Role.TEACHER &&  academyUser.getRole() != Role.Lecturer)) {
	      throw new UnAuthorizedException();
	    }
	  }
}
