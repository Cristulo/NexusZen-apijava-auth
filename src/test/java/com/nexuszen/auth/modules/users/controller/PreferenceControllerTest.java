package com.nexuszen.auth.modules.users.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.nexuszen.auth.modules.users.delegate.PreferenceDelegate;
import com.nexuszen.auth.modules.users.dto.PreferenceUpdateDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
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
public class PreferenceControllerTest {

  @Mock private PreferenceDelegate preferenceDelegate;
  @Mock private SecurityContext securityContext;
  @Mock private Authentication authentication;

  @InjectMocks private PreferenceController preferenceController;

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
  void updatePreference_Success() {
    mockAuthentication();
    PreferenceUpdateDTO dto = new PreferenceUpdateDTO();
    UsuarioResponseDTO mockResponse = UsuarioResponseDTO.builder().build();
    when(preferenceDelegate.updatePreference("test@nexuszen.com", dto)).thenReturn(mockResponse);

    ResponseEntity<UsuarioResponseDTO> result = preferenceController.updatePreference(dto);

    assertEquals(200, result.getStatusCode().value());
    verify(preferenceDelegate).updatePreference("test@nexuszen.com", dto);
  }
}
