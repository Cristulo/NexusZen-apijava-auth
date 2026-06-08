package com.nexuszen.auth.modules.users.delegate;

import com.nexuszen.auth.models.Rol;
import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.UsuarioEmail;
import com.nexuszen.auth.models.enums.EmailType;
import com.nexuszen.auth.modules.users.services.UserService;
import com.nexuszen.auth.security.JwtService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthDelegate {

  private final UserService userService;
  private final JwtService jwtService;
  private final StringRedisTemplate redisTemplate;
  private final PasswordEncoder passwordEncoder;

  public AuthDelegate(UserService userService, JwtService jwtService, StringRedisTemplate redisTemplate, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.redisTemplate = redisTemplate;
    this.passwordEncoder = passwordEncoder;
  }

  public Map<String, String> login(String usuarioStr, String passwordStr) {
    Usuario usuario;
    try {
      usuario = userService.getByUsuario(usuarioStr);
    } catch (Exception e) {
      throw new RuntimeException("Usuario no encontrado");
    }

    if (usuario.getPasswordHash() == null) {
      throw new RuntimeException("El usuario no tiene contraseña local. Inicie sesión con Google.");
    }

    if (!passwordEncoder.matches(passwordStr, usuario.getPasswordHash())) {
      throw new RuntimeException("Contraseña incorrecta");
    }

    List<String> roleNames = usuario.getRoles().stream()
        .map(Rol::getName)
        .collect(Collectors.toList());

    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", roleNames);

    String tokenSubject = usuario.getEmails().stream()
        .filter(e -> e.getTipo() == EmailType.PRIMARY)
        .map(UsuarioEmail::getEmail)
        .findFirst()
        .orElse(usuarioStr); // Fallback

    String token = jwtService.generateToken(tokenSubject, claims);
    Map<String, String> response = new HashMap<>();
    response.put("token", token);
    return response;
  }

  public void completarRegistro(String email, String nuevoUsuario, String nuevoUsername, String nuevoPassword) {
    userService.updateProfileAndRegister(email, nuevoUsuario, nuevoUsername, nuevoPassword);
  }

  public void logoutAll(String email) {
    Usuario usuario = userService.getByEmail(email);
    if (usuario.getUsuario() != null) {
      String revocationKey = "user_revocation:" + usuario.getUsuario();
      redisTemplate.opsForValue().set(revocationKey, String.valueOf(System.currentTimeMillis()));
    }
  }
}
