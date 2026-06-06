package com.nexuszen.auth.modules.users.controller;

import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import com.nexuszen.auth.modules.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.nexuszen.auth.models.Rol;
import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.repositories.RolRepository;
import com.nexuszen.auth.repositories.UsuarioRepository;
import com.nexuszen.auth.security.JwtService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints de identidad y perfil de usuario")
public class AuthController {

  private final UserService userService;
  private final UsuarioRepository usuarioRepository;
  private final RolRepository rolRepository;
  private final JwtService jwtService;

  public AuthController(
      UserService userService,
      UsuarioRepository usuarioRepository,
      RolRepository rolRepository,
      JwtService jwtService) {
    this.userService = userService;
    this.usuarioRepository = usuarioRepository;
    this.rolRepository = rolRepository;
    this.jwtService = jwtService;
  }

  @Operation(summary = "Login con Email (Sin Password)", description = "Devuelve un JWT para el email proporcionado. Crea el usuario si no existe.")
  @PostMapping("/public/login-email")
  public ResponseEntity<Map<String, String>> loginByEmail(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    if (email == null || email.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    Usuario usuario =
        usuarioRepository
            .findByEmail(email)
            .orElseGet(
                () -> {
                  Usuario nuevo = Usuario.builder().email(email).isActive(true).build();
                  return usuarioRepository.save(nuevo);
                });

    Set<Rol> roles = usuario.getRoles();
    if (roles == null || roles.isEmpty()) {
      Rol roleUser =
          rolRepository
              .findByName("ROLE_USER")
              .orElseGet(
                  () -> {
                    Rol nuevoRol =
                        Rol.builder().name("ROLE_USER").description("Default user role").build();
                    return rolRepository.save(nuevoRol);
                  });
      if (roles == null) {
        roles = new HashSet<>();
      }
      roles.add(roleUser);
      usuario.setRoles(roles);
      usuarioRepository.save(usuario);
    }

    List<String> roleNames = roles.stream().map(Rol::getName).collect(Collectors.toList());
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", roleNames);

    String token = jwtService.generateToken(email, claims);
    Map<String, String> response = new HashMap<>();
    response.put("token", token);

    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Obtener Perfil",
      description = "Retorna el perfil del usuario autenticado, incluyendo roles y permisos.")
  @GetMapping("/me")
  public ResponseEntity<UsuarioResponseDTO> getMyProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    }

    String email;
    if (authentication.getPrincipal() instanceof OAuth2User) {
      email = ((OAuth2User) authentication.getPrincipal()).getAttribute("email");
    } else {
      email = authentication.getName(); // JWT sub fallback
    }

    return ResponseEntity.ok(userService.getProfileByEmail(email));
  }
}
