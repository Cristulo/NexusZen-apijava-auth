package com.nexuszen.auth.modules.users.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nexuszen.auth.models.Permiso;
import com.nexuszen.auth.models.Rol;
import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.enums.EmailType;
import com.nexuszen.auth.models.enums.EmailCategory;
import com.nexuszen.auth.models.enums.EstadoUsuario;
import com.nexuszen.auth.models.UsuarioEmail;
import com.nexuszen.auth.models.repositories.UsuarioEmailRepository;
import com.nexuszen.auth.models.repositories.UsuarioRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock private UsuarioEmailRepository usuarioEmailRepository;
  @Mock private UsuarioRepository usuarioRepository;
  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserService userService;

  private Usuario testUsuario;

  @BeforeEach
  void setUp() {
    Permiso p1 = Permiso.builder().id(UUID.randomUUID()).name("READ").build();
    Rol rol = Rol.builder().id(UUID.randomUUID()).name("ADMIN").permisos(Set.of(p1)).build();

    testUsuario =
        Usuario.builder()
            .id(UUID.randomUUID())
            .usuario("testuser")
            .username("Test User")
            .estado(EstadoUsuario.ACTIVO)
            .roles(Set.of(rol))
            .build();
            
    UsuarioEmail testEmail = UsuarioEmail.builder()
        .id(UUID.randomUUID())
        .email("test@nexuszen.com")
        .tipo(EmailType.PRIMARY)
        .categoria(EmailCategory.PERSONAL)
        .verified(true)
        .usuario(testUsuario)
        .build();
        
    testUsuario.setEmails(Set.of(testEmail));
  }

  @Test
  void getByEmail_Success() {
    UsuarioEmail mockEmail = testUsuario.getEmails().iterator().next();
    when(usuarioEmailRepository.findByEmail("test@nexuszen.com")).thenReturn(Optional.of(mockEmail));

    Usuario response = userService.getByEmail("test@nexuszen.com");

    assertNotNull(response);
    assertTrue(response.getEmails().stream().anyMatch(e -> e.getEmail().equals("test@nexuszen.com")));
    assertTrue(response.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN")));

    verify(usuarioEmailRepository, times(1)).findByEmail("test@nexuszen.com");
  }

  @Test
  void getByEmail_NotFound() {
    when(usuarioEmailRepository.findByEmail("notfound@nexuszen.com")).thenReturn(Optional.empty());

    assertThrows(
        RuntimeException.class,
        () -> {
          userService.getByEmail("notfound@nexuszen.com");
        });
  }

  @Test
  void getByEmail_Normalization_Success() {
    UsuarioEmail mockEmail = testUsuario.getEmails().iterator().next();
    when(usuarioEmailRepository.findByEmail("test@nexuszen.com")).thenReturn(Optional.of(mockEmail));

    Usuario response = userService.getByEmail("  TEST@NexusZen.Com  ");

    assertNotNull(response);
    verify(usuarioEmailRepository, times(1)).findByEmail("test@nexuszen.com");
  }

  @Test
  void getByUsuario_Normalization_Success() {
    when(usuarioRepository.findByUsuario("testuser")).thenReturn(Optional.of(testUsuario));

    Usuario response = userService.getByUsuario("  TESTUSER  ");

    assertNotNull(response);
    assertEquals("testuser", response.getUsuario());
    verify(usuarioRepository, times(1)).findByUsuario("testuser");
  }
}

