package com.nexuszen.auth.modules.users.services;

import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.UsuarioEmail;
import com.nexuszen.auth.models.enums.EmailCategory;
import com.nexuszen.auth.models.enums.EmailType;
import com.nexuszen.auth.models.repositories.UsuarioEmailRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmailService {
  private final UsuarioEmailRepository usuarioEmailRepository;
  private final UserService userService;

  public EmailService(UsuarioEmailRepository usuarioEmailRepository, UserService userService) {
    this.usuarioEmailRepository = usuarioEmailRepository;
    this.userService = userService;
  }

  public Usuario addEmail(String emailTokenOrPrincipal, String newEmailAddress) {
    Usuario usuario = userService.getByEmail(emailTokenOrPrincipal);

    if (usuarioEmailRepository.findByEmail(newEmailAddress).isPresent()) {
      throw new RuntimeException("El email ya está registrado por otro usuario.");
    }

    UsuarioEmail userEmail = UsuarioEmail.builder()
        .usuario(usuario)
        .email(newEmailAddress)
        .tipo(EmailType.SECONDARY)
        .categoria(EmailCategory.PERSONAL)
        .verified(false) // Debería requerir verificación, lo ponemos en falso por defecto
        .build();

    usuarioEmailRepository.save(userEmail);
    return userService.getById(usuario.getId()); // Refresh user entity
  }

  public Usuario removeEmail(String emailTokenOrPrincipal, UUID emailIdToRemove) {
    Usuario usuario = userService.getByEmail(emailTokenOrPrincipal);

    UsuarioEmail emailToRemove = usuarioEmailRepository.findById(emailIdToRemove)
        .orElseThrow(() -> new RuntimeException("Email no encontrado."));

    if (!emailToRemove.getUsuario().getId().equals(usuario.getId())) {
      throw new RuntimeException("El email no pertenece a tu cuenta.");
    }

    if (emailToRemove.getTipo() == EmailType.PRIMARY) {
      throw new RuntimeException("No puedes eliminar tu email principal. Establece otro como primario primero.");
    }

    usuarioEmailRepository.delete(emailToRemove);
    return userService.getById(usuario.getId()); // Refresh user entity
  }

  public Usuario setPrimaryEmail(String emailTokenOrPrincipal, UUID emailIdToSetPrimary) {
    Usuario usuario = userService.getByEmail(emailTokenOrPrincipal);

    UsuarioEmail newPrimary = usuarioEmailRepository.findById(emailIdToSetPrimary)
        .orElseThrow(() -> new RuntimeException("Email no encontrado."));

    if (!newPrimary.getUsuario().getId().equals(usuario.getId())) {
      throw new RuntimeException("El email no pertenece a tu cuenta.");
    }

    if (!newPrimary.getVerified()) {
      throw new RuntimeException("No puedes establecer como primario un correo no verificado.");
    }

    // Demote current primary to secondary
    usuario.getEmails().forEach(email -> {
      if (email.getTipo() == EmailType.PRIMARY) {
        email.setTipo(EmailType.SECONDARY);
        usuarioEmailRepository.save(email);
      }
    });

    newPrimary.setTipo(EmailType.PRIMARY);
    usuarioEmailRepository.save(newPrimary);

    return userService.getById(usuario.getId()); // Refresh
  }
}
