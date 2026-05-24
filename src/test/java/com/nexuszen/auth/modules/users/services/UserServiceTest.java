package com.nexuszen.auth.modules.users.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nexuszen.auth.models.Permiso;
import com.nexuszen.auth.models.Rol;
import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import com.nexuszen.auth.repositories.UsuarioRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock private UsuarioRepository usuarioRepository;

  @InjectMocks private UserService userService;

  private Usuario testUsuario;

  @BeforeEach
  void setUp() {
    Permiso p1 = Permiso.builder().id(UUID.randomUUID()).name("READ").build();
    Rol rol = Rol.builder().id(UUID.randomUUID()).name("ADMIN").permisos(Set.of(p1)).build();

    testUsuario =
        Usuario.builder()
            .id(UUID.randomUUID())
            .email("test@nexuszen.com")
            .isActive(true)
            .roles(Set.of(rol))
            .build();
  }

  @Test
  void getProfileByEmail_Success() {
    when(usuarioRepository.findByEmail("test@nexuszen.com")).thenReturn(Optional.of(testUsuario));

    UsuarioResponseDTO response = userService.getProfileByEmail("test@nexuszen.com");

    assertNotNull(response);
    assertEquals("test@nexuszen.com", response.getEmail());
    assertTrue(response.getRoles().contains("ADMIN"));
    assertTrue(response.getPermisos().contains("READ"));

    verify(usuarioRepository, times(1)).findByEmail("test@nexuszen.com");
  }

  @Test
  void getProfileByEmail_NotFound() {
    when(usuarioRepository.findByEmail("notfound@nexuszen.com")).thenReturn(Optional.empty());

    assertThrows(
        RuntimeException.class,
        () -> {
          userService.getProfileByEmail("notfound@nexuszen.com");
        });
  }
}
