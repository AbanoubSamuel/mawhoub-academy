package com.ficklebytes.security;

import com.ficklebytes.enums.Role;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

  Role role() default Role.STUDENT;

  int userId() default 1;

  boolean guest() default false;

}
