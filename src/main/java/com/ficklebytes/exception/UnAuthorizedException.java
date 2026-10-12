package com.ficklebytes.exception;

import com.ficklebytes.enums.Role;

public class UnAuthorizedException extends Exception {

  public UnAuthorizedException(Role requiredRole) {
    super();
  }

  public UnAuthorizedException() {
    super();
  }
}
