package com.nexuszen.auth.modules.users.delegate;

import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.modules.users.dto.EmailAddDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import com.nexuszen.auth.modules.users.services.EmailService;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class EmailDelegate {

  private final EmailService emailService;

  public EmailDelegate(EmailService emailService) {
    this.emailService = emailService;
  }

  public UsuarioResponseDTO addEmail(String tokenEmail, EmailAddDTO dto) {
    Usuario usuario = emailService.addEmail(tokenEmail, dto.getEmail());
    return UserMapper.mapToDTO(usuario);
  }

  public UsuarioResponseDTO removeEmail(String tokenEmail, UUID emailId) {
    Usuario usuario = emailService.removeEmail(tokenEmail, emailId);
    return UserMapper.mapToDTO(usuario);
  }

  public UsuarioResponseDTO setPrimaryEmail(String tokenEmail, UUID emailId) {
    Usuario usuario = emailService.setPrimaryEmail(tokenEmail, emailId);
    return UserMapper.mapToDTO(usuario);
  }
}
