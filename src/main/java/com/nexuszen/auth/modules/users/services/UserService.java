package com.nexuszen.auth.modules.users.services;

import com.nexuszen.auth.models.enums.EstadoUsuario;
import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.UsuarioEmail;
import com.nexuszen.auth.models.enums.EmailCategory;
import com.nexuszen.auth.models.enums.EmailType;
import com.nexuszen.auth.models.repositories.UsuarioEmailRepository;
import com.nexuszen.auth.models.repositories.UsuarioRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
  private final UsuarioEmailRepository usuarioEmailRepository;
  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UsuarioEmailRepository usuarioEmailRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
    this.usuarioEmailRepository = usuarioEmailRepository;
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Usuario getByEmail(String email) {
    return usuarioEmailRepository
        .findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
        .getUsuario();
  }

  public Usuario getById(UUID id) {
    return usuarioRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado por ID: " + id));
  }

  public Usuario getByUsuario(String usuario) {
    return usuarioRepository
        .findByUsuario(usuario)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuario));
  }

  public Page<Usuario> getAllUsers(Pageable pageable) {
    return usuarioRepository.findAll(pageable);
  }

  public Usuario updateProfile(String email, String newUsuario, String newUsername) {
    Usuario usuario = getByEmail(email);

    if (newUsuario != null && !newUsuario.isEmpty()) {
      usuarioRepository.findByUsuario(newUsuario).ifPresent(existingUser -> {
        if (!existingUser.getId().equals(usuario.getId())) {
          throw new RuntimeException("El @usuario ya está en uso");
        }
      });
      usuario.setUsuario(newUsuario);
    }

    if (newUsername != null && !newUsername.isEmpty()) {
      usuario.setUsername(newUsername);
    }

    return usuarioRepository.save(usuario);
  }

  public Usuario updateProfileAndRegister(String email, String newUsuario, String newUsername, String newPassword) {
    Usuario usuario = getByEmail(email);

    if (newUsuario != null && !newUsuario.isEmpty()) {
      usuarioRepository.findByUsuario(newUsuario).ifPresent(existingUser -> {
        if (!existingUser.getId().equals(usuario.getId())) {
          throw new RuntimeException("El @usuario ya está en uso");
        }
      });
      usuario.setUsuario(newUsuario);
    }

    if (newUsername != null && !newUsername.isEmpty()) {
      usuario.setUsername(newUsername);
    }

    if (newPassword != null && !newPassword.isEmpty()) {
      usuario.setPasswordHash(passwordEncoder.encode(newPassword));
    }

    return usuarioRepository.save(usuario);
  }

  public Usuario updateAvatar(String email, String avatarUrl) {
    Usuario usuario = getByEmail(email);
    usuario.setProfileImageUrl(avatarUrl);
    return usuarioRepository.save(usuario);
  }

  public void updatePassword(String email, String newPassword) {
    Usuario usuario = getByEmail(email);
    usuario.setPasswordHash(passwordEncoder.encode(newPassword));
    usuarioRepository.save(usuario);
  }

  public void logicalDelete(String email) {
    Usuario usuario = getByEmail(email);
    // Patrón State: Ejecutamos transición
    usuario.setEstado(usuario.getEstado().darDeBaja());
    usuarioRepository.save(usuario);
  }

  public void logicalDeleteById(UUID id) {
    Usuario usuario = getById(id);
    usuario.setEstado(usuario.getEstado().darDeBaja());
    usuarioRepository.save(usuario);
  }

  public Usuario createUser(String usuarioName, String username, String email, String plainPassword) {
    if (usuarioRepository.existsByUsuario(usuarioName)) {
      throw new RuntimeException("El @usuario ya está en uso");
    }
    if (usuarioEmailRepository.findByEmail(email).isPresent()) {
      throw new RuntimeException("El email ya está en uso");
    }

    Usuario nuevoUsuario = Usuario.builder()
        .usuario(usuarioName)
        .username(username)
        .passwordHash(passwordEncoder.encode(plainPassword))
        .estado(EstadoUsuario.ACTIVO)
        .build();
    
    nuevoUsuario = usuarioRepository.save(nuevoUsuario);

    UsuarioEmail userEmail = UsuarioEmail.builder()
        .usuario(nuevoUsuario)
        .email(email)
        .tipo(EmailType.PRIMARY)
        .categoria(EmailCategory.PERSONAL)
        .verified(true) // Assumed verified if created by admin
        .build();
    
    usuarioEmailRepository.save(userEmail);

    return nuevoUsuario;
  }

  public Usuario updateAdmin(UUID id, String newUsuario, String newUsername, String newEstado) {
    Usuario usuario = getById(id);

    if (newUsuario != null && !newUsuario.isEmpty()) {
      usuarioRepository.findByUsuario(newUsuario).ifPresent(existingUser -> {
        if (!existingUser.getId().equals(usuario.getId())) {
          throw new RuntimeException("El @usuario ya está en uso");
        }
      });
      usuario.setUsuario(newUsuario);
    }

    if (newUsername != null && !newUsername.isEmpty()) {
      usuario.setUsername(newUsername);
    }

    if (newEstado != null && !newEstado.isEmpty()) {
      try {
        EstadoUsuario estado = EstadoUsuario.valueOf(newEstado);
        usuario.setEstado(estado);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Estado inválido");
      }
    }

    return usuarioRepository.save(usuario);
  }
}
