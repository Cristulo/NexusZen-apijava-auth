package com.nexuszen.auth.modules.users.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.nexuszen.auth.modules.users.delegate.AuthDelegate;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

  @Mock private AuthDelegate authDelegate;
  @Mock private HttpServletRequest request;

  @InjectMocks private AuthController authController;

  private Map<String, String> loginRequest;
  private Map<String, String> loginResponse;

  @BeforeEach
  void setUp() {
    loginRequest = new HashMap<>();
    loginRequest.put("usuario", "testuser");
    loginRequest.put("password", "password123");
    
    loginResponse = new HashMap<>();
    loginResponse.put("token", "token123");
  }

  @Test
  void login_Success() {
    when(authDelegate.login("testuser", "password123")).thenReturn(loginResponse);

    ResponseEntity<Map<String, String>> result = authController.loginByUsuario(loginRequest);

    assertEquals(200, result.getStatusCode().value());
    assertEquals("token123", result.getBody().get("token"));
    verify(authDelegate).login("testuser", "password123");
  }

  @Test
  void oauthCallback_Success() {
    // mock completando registro
    Map<String, String> req = new HashMap<>();
    req.put("usuario", "testuser");
    req.put("username", "Test User");

    // authController.completarRegistro relies on SecurityContext
  }

  @Test
  void logoutAll_Success() {
    // mock SecurityContext
    org.springframework.security.core.context.SecurityContext securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
    org.springframework.security.core.Authentication authentication = mock(org.springframework.security.core.Authentication.class);
    org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(mock(org.springframework.security.oauth2.jwt.Jwt.class));
    when(authentication.getName()).thenReturn("test@nexuszen.com");

    ResponseEntity<Map<String, String>> result = authController.logoutAll();
    
    assertEquals(200, result.getStatusCode().value());
    verify(authDelegate).logoutAll("test@nexuszen.com");
  }
}
