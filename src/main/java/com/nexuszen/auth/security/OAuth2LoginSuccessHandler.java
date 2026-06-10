package com.nexuszen.auth.security;

import com.nexuszen.auth.models.Rol;
import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.UsuarioEmail;
import com.nexuszen.auth.models.enums.EmailCategory;
import com.nexuszen.auth.models.enums.EmailType;
import com.nexuszen.auth.models.enums.EstadoUsuario;
import com.nexuszen.auth.models.repositories.RolRepository;
import com.nexuszen.auth.models.repositories.UsuarioEmailRepository;
import com.nexuszen.auth.models.repositories.UsuarioRepository;
import com.nexuszen.auth.utils.StringUtils;
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

import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final UsuarioRepository usuarioRepository;
  private final UsuarioEmailRepository usuarioEmailRepository;
  private final RolRepository rolRepository;

  @Value("${frontend.url:https://rank-bottle-front-electron.trycloudflare.com}")
  private String frontendUrl;

  public OAuth2LoginSuccessHandler(
      JwtService jwtService, UsuarioRepository usuarioRepository, UsuarioEmailRepository usuarioEmailRepository, RolRepository rolRepository) {
    this.jwtService = jwtService;
    this.usuarioRepository = usuarioRepository;
    this.usuarioEmailRepository = usuarioEmailRepository;
    this.rolRepository = rolRepository;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    String email = StringUtils.normalizeEmail(oAuth2User.getAttribute("email"));

    // Cargar Roles desde la base de datos
    Usuario usuario = usuarioEmailRepository.findByEmail(email)
        .map(UsuarioEmail::getUsuario)
        .orElseGet(() -> {
            String displayName = oAuth2User.getAttribute("name");
            String profileImageUrl = oAuth2User.getAttribute("picture");
            
            Usuario nuevoUsuario = Usuario.builder()
                .usuario(null)
                .username(displayName)
                .profileImageUrl(profileImageUrl)
                .estado(EstadoUsuario.ACTIVO)
                .build();
                
            nuevoUsuario = usuarioRepository.save(nuevoUsuario);

            UsuarioEmail nuevoEmail = UsuarioEmail.builder()
                .usuario(nuevoUsuario)
                .email(email)
                .tipo(EmailType.PRIMARY)
                .categoria(EmailCategory.PERSONAL)
                .verified(true)
                .build();
                
            usuarioEmailRepository.save(nuevoEmail);
            return nuevoUsuario;
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

    // Redirigir al frontend con el token en la URL
    if (usuario.getUsuario() == null) {
      // Falta completar el registro
      response.sendRedirect(frontendUrl + "/completar-registro?token=" + token);
    } else {
      response.sendRedirect(frontendUrl + "/?token=" + token);
    }
  }
}
