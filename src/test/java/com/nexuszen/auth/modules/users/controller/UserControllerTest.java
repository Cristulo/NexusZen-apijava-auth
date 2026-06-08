package com.nexuszen.auth.modules.users.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.nexuszen.auth.modules.users.delegate.UserDelegate;
import com.nexuszen.auth.modules.users.dto.UsuarioAdminUpdateDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioCreateDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  @Mock private UserDelegate userDelegate;

  @InjectMocks private UserController userController;

  @Test
  @SuppressWarnings("unchecked")
  void getAllUsers_Success() {
    Page<UsuarioResponseDTO> mockPage = mock(Page.class);
    when(userDelegate.getAllUsers(any(Pageable.class))).thenReturn(mockPage);

    ResponseEntity<Page<UsuarioResponseDTO>> result = userController.getAllUsers(Pageable.unpaged());

    assertEquals(200, result.getStatusCode().value());
    verify(userDelegate).getAllUsers(any(Pageable.class));
  }

  @Test
  void getUserById_Success() {
    UUID id = UUID.randomUUID();
    UsuarioResponseDTO mockResponse = UsuarioResponseDTO.builder().build();
    when(userDelegate.getUserById(id)).thenReturn(mockResponse);

    ResponseEntity<UsuarioResponseDTO> result = userController.getUserById(id);

    assertEquals(200, result.getStatusCode().value());
    verify(userDelegate).getUserById(id);
  }

  @Test
  void createUser_Success() {
    UsuarioCreateDTO dto = new UsuarioCreateDTO();
    UsuarioResponseDTO mockResponse = UsuarioResponseDTO.builder().build();
    when(userDelegate.createUser(dto)).thenReturn(mockResponse);

    ResponseEntity<UsuarioResponseDTO> result = userController.createUser(dto);

    assertEquals(200, result.getStatusCode().value());
    verify(userDelegate).createUser(dto);
  }

  @Test
  void updateUser_Success() {
    UUID id = UUID.randomUUID();
    UsuarioAdminUpdateDTO dto = new UsuarioAdminUpdateDTO();
    UsuarioResponseDTO mockResponse = UsuarioResponseDTO.builder().build();
    when(userDelegate.updateUser(id, dto)).thenReturn(mockResponse);

    ResponseEntity<UsuarioResponseDTO> result = userController.updateUser(id, dto);

    assertEquals(200, result.getStatusCode().value());
    verify(userDelegate).updateUser(id, dto);
  }

  @Test
  void deleteUser_Success() {
    UUID id = UUID.randomUUID();

    ResponseEntity<Void> result = userController.deleteUser(id);

    assertEquals(204, result.getStatusCode().value());
    verify(userDelegate).deleteUser(id);
  }
}
