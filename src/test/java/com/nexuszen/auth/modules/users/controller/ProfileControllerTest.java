package com.nexuszen.auth.modules.users.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.nexuszen.auth.modules.users.delegate.ProfileDelegate;
import com.nexuszen.auth.modules.users.dto.PasswordUpdateDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioUpdateDTO;
import java.util.Map;
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
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ProfileControllerTest {

  @Mock private ProfileDelegate profileDelegate;
  @Mock private SecurityContext securityContext;
  @Mock private Authentication authentication;

  @InjectMocks private ProfileController profileController;

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
  void getProfile_Success() {
    mockAuthentication();
    UsuarioResponseDTO mockResponse = UsuarioResponseDTO.builder().build();
    when(profileDelegate.getProfile("test@nexuszen.com")).thenReturn(mockResponse);

    ResponseEntity<UsuarioResponseDTO> result = profileController.getMyProfile();

    assertEquals(200, result.getStatusCode().value());
    verify(profileDelegate).getProfile("test@nexuszen.com");
  }

  @Test
  void getProfile_Unauthorized() {
    when(securityContext.getAuthentication()).thenReturn(null);

    ResponseEntity<UsuarioResponseDTO> result = profileController.getMyProfile();

    assertEquals(401, result.getStatusCode().value());
    verifyNoInteractions(profileDelegate);
  }

  @Test
  void updateMyProfile_Success() {
    mockAuthentication();
    UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
    UsuarioResponseDTO mockResponse = UsuarioResponseDTO.builder().build();
    when(profileDelegate.updateProfile(eq("test@nexuszen.com"), any())).thenReturn(mockResponse);

    ResponseEntity<UsuarioResponseDTO> result = profileController.updateMyProfile(dto);

    assertEquals(200, result.getStatusCode().value());
    verify(profileDelegate).updateProfile(eq("test@nexuszen.com"), any());
  }

  @Test
  void updatePassword_Success() {
    mockAuthentication();
    PasswordUpdateDTO dto = new PasswordUpdateDTO();
    ResponseEntity<Map<String, String>> result = profileController.updatePassword(dto);

    assertEquals(200, result.getStatusCode().value());
    verify(profileDelegate).updatePassword("test@nexuszen.com", dto);
  }

  @Test
  void uploadAvatar_Success() {
    mockAuthentication();
    MultipartFile file = mock(MultipartFile.class);
    UsuarioResponseDTO mockResponse = UsuarioResponseDTO.builder().build();
    when(profileDelegate.updateAvatar("test@nexuszen.com", file)).thenReturn(mockResponse);

    ResponseEntity<UsuarioResponseDTO> result = profileController.updateAvatar(file);

    assertEquals(200, result.getStatusCode().value());
    verify(profileDelegate).updateAvatar("test@nexuszen.com", file);
  }
}
