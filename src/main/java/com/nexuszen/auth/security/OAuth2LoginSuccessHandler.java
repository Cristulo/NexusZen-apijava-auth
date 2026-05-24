package com.nexuszen.auth.security;

import com.nexuszen.auth.models.Rol;
import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.repositories.RolRepository;
import com.nexuszen.auth.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final UsuarioRepository usuarioRepository;
  private final RolRepository rolRepository;

  @Value("${frontend.url:https://rank-bottle-front-electron.trycloudflare.com}")
  private String frontendUrl;

  public OAuth2LoginSuccessHandler(
      JwtService jwtService,
      UsuarioRepository usuarioRepository,
      RolRepository rolRepository) {
    this.jwtService = jwtService;
    this.usuarioRepository = usuarioRepository;
    this.rolRepository = rolRepository;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication)
      throws IOException {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    String email = oAuth2User.getAttribute("email");

    // Cargar Roles desde la base de datos
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseGet(() -> {
          Usuario nuevo = Usuario.builder()
              .email(email)
              .isActive(true)
              .build();
          return usuarioRepository.save(nuevo);
        });

    Set<Rol> roles = usuario.getRoles();
    if (roles == null || roles.isEmpty()) {
      Rol roleUser = rolRepository.findByName("ROLE_USER")
          .orElseGet(() -> {
            Rol nuevoRol = Rol.builder()
                .name("ROLE_USER")
                .description("Default user role")
                .build();
            return rolRepository.save(nuevoRol);
          });
      if (roles == null) {
        roles = new HashSet<>();
      }
      roles.add(roleUser);
      usuario.setRoles(roles);
      usuarioRepository.save(usuario);
    }

    List<String> roleNames = roles.stream()
        .map(Rol::getName)
        .collect(Collectors.toList());

    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", roleNames);

    String token = jwtService.generateToken(email, claims);

    // Redirigir al frontend con el token en la URL (Opcionalmente cookie HTTP-Only)
    response.sendRedirect(frontendUrl + "/?token=" + token);
  }
}
