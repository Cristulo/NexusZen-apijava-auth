package com.nexuszen.auth.security;

import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.UsuarioEmail;
import com.nexuszen.auth.models.enums.EmailCategory;
import com.nexuszen.auth.models.enums.EmailType;
import com.nexuszen.auth.models.enums.EstadoUsuario;
import com.nexuszen.auth.models.repositories.UsuarioEmailRepository;
import com.nexuszen.auth.models.repositories.UsuarioRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);
  private final UsuarioRepository usuarioRepository;
  private final UsuarioEmailRepository usuarioEmailRepository;

  public CustomOAuth2UserService(UsuarioRepository usuarioRepository, UsuarioEmailRepository usuarioEmailRepository) {
    this.usuarioRepository = usuarioRepository;
    this.usuarioEmailRepository = usuarioEmailRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    String email = oAuth2User.getAttribute("email");
    if (email == null) {
      log.error("OAuth2 provider did not return an email address.");
      throw new OAuth2AuthenticationException("Email no provisto por el proveedor OAuth");
    }

    Optional<UsuarioEmail> emailOpt = usuarioEmailRepository.findByEmail(email);

    if (emailOpt.isEmpty()) {
      log.info("Creando nuevo usuario a partir de login OAuth2: {}", email);
      String displayName = oAuth2User.getAttribute("name");
      String profileImageUrl = oAuth2User.getAttribute("picture");
      
      Usuario nuevoUsuario = Usuario.builder()
          .usuario(null) // Debe completarse después
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
          .verified(true) // Viene de OAuth2
          .build();
          
      usuarioEmailRepository.save(nuevoEmail);
    } else {
      log.info("Usuario existente inició sesión con OAuth2: {}", email);
      Usuario existingUser = emailOpt.get().getUsuario();
      String profileImageUrl = oAuth2User.getAttribute("picture");
      // Actualizamos la foto de perfil si viene de Google y no la tenía o es diferente
      if (profileImageUrl != null && !profileImageUrl.equals(existingUser.getProfileImageUrl())) {
          existingUser.setProfileImageUrl(profileImageUrl);
          usuarioRepository.save(existingUser);
      }
    }

    return oAuth2User;
  }
}
