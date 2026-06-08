package com.nexuszen.auth.modules.users.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.nexuszen.auth.modules.users.delegate.EmailDelegate;
import com.nexuszen.auth.modules.users.dto.EmailAddDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {

  @Mock private EmailDelegate emailDelegate;
  @Mock private SecurityContext securityContext;
  @Mock private Authentication authentication;

  @InjectMocks private EmailController emailController;

  @BeforeEach
  void setUp() {
    SecurityContextHolder.setContext(securityContext);
  }

  private void mockAuthentication() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(mock(Jwt.class));
    when(authentication.getName()).thenReturn("test@nexuszen.com");
  }

  @Test
  void addEmail_Success() {
    mockAuthentication();
    EmailAddDTO dto = new EmailAddDTO();
    UsuarioResponseDTO mockResponse = UsuarioResponseDTO.builder().build();
    when(emailDelegate.addEmail("test@nexuszen.com", dto)).thenReturn(mockResponse);

    ResponseEntity<UsuarioResponseDTO> result = emailController.addEmail(dto);

    assertEquals(200, result.getStatusCode().value());
    verify(emailDelegate).addEmail("test@nexuszen.com", dto);
  }

  @Test
  void setPrimaryEmail_Success() {
    mockAuthentication();
    UUID id = UUID.randomUUID();
    UsuarioResponseDTO mockResponse = UsuarioResponseDTO.builder().build();
    when(emailDelegate.setPrimaryEmail("test@nexuszen.com", id)).thenReturn(mockResponse);

    ResponseEntity<UsuarioResponseDTO> result = emailController.setPrimaryEmail(id);

    assertEquals(200, result.getStatusCode().value());
    verify(emailDelegate).setPrimaryEmail("test@nexuszen.com", id);
  }

  @Test
  void removeEmail_Success() {
    mockAuthentication();
    UUID id = UUID.randomUUID();
    UsuarioResponseDTO mockResponse = UsuarioResponseDTO.builder().build();
    when(emailDelegate.removeEmail("test@nexuszen.com", id)).thenReturn(mockResponse);

    ResponseEntity<UsuarioResponseDTO> result = emailController.removeEmail(id);

    assertEquals(200, result.getStatusCode().value());
    verify(emailDelegate).removeEmail("test@nexuszen.com", id);
  }
}
