package com.nexuszen.auth.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class SecurityUtils {

  public static String getEmailFromAuthentication(Authentication authentication) {
    if (authentication == null) {
      return null;
    }
    if (authentication.getPrincipal() instanceof OAuth2User) {
      return ((OAuth2User) authentication.getPrincipal()).getAttribute("email");
    }
    return authentication.getName();
  }
}
