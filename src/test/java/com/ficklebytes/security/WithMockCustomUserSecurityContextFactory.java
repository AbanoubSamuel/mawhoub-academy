package com.ficklebytes.security;

import com.ficklebytes.entity.AcademyUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
      if (customUser.guest()) {
          return context;
      }
    AcademyUser principal = new AcademyUser();
    principal.setEmail("bob@mawhoubacademy.com");
    principal.setUserId(customUser.userId());
    principal.setRole(customUser.role());
    Authentication auth =
        new UsernamePasswordAuthenticationToken(principal, "password",
            principal.getAuthorities());
    context.setAuthentication(auth);
    return context;
  }
}