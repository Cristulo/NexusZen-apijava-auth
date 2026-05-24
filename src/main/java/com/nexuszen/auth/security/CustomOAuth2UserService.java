package com.nexuszen.auth.security;

import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.repositories.UsuarioRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);
  private final UsuarioRepository usuarioRepository;

  public CustomOAuth2UserService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    String email = oAuth2User.getAttribute("email");
    if (email == null) {
      log.error("OAuth2 provider did not return an email address.");
      throw new OAuth2AuthenticationException("Email no provisto por el proveedor OAuth");
    }

    Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

    if (usuarioOpt.isEmpty()) {
      log.info("Creando nuevo usuario a partir de login OAuth2: {}", email);
      Usuario nuevoUsuario = Usuario.builder().email(email).isActive(true).build();
      usuarioRepository.save(nuevoUsuario);
    } else {
      log.info("Usuario existente inició sesión con OAuth2: {}", email);
    }

    return oAuth2User;
  }
}
