package com.nexuszen.auth.modules.users.controller;

import com.nexuszen.auth.modules.users.delegate.EmailDelegate;
import com.nexuszen.auth.modules.users.dto.EmailAddDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import com.nexuszen.auth.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/me/emails")
@Tag(name = "Correos", description = "Endpoints para la gestión de múltiples correos electrónicos")
public class EmailController {

  private final EmailDelegate emailDelegate;

  public EmailController(EmailDelegate emailDelegate) {
    this.emailDelegate = emailDelegate;
  }

  @Operation(summary = "Añadir Correo", description = "Añade un correo secundario a la cuenta del usuario")
  @PostMapping
  public ResponseEntity<UsuarioResponseDTO> addEmail(@RequestBody EmailAddDTO dto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    }

    String emailToken = SecurityUtils.getEmailFromAuthentication(authentication);
    try {
      UsuarioResponseDTO updated = emailDelegate.addEmail(emailToken, dto);
      return ResponseEntity.ok(updated);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @Operation(summary = "Establecer Principal", description = "Convierte un correo secundario en principal")
  @PutMapping("/{emailId}/primary")
  public ResponseEntity<UsuarioResponseDTO> setPrimaryEmail(@PathVariable UUID emailId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    }

    String emailToken = SecurityUtils.getEmailFromAuthentication(authentication);
    try {
      UsuarioResponseDTO updated = emailDelegate.setPrimaryEmail(emailToken, emailId);
      return ResponseEntity.ok(updated);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @Operation(summary = "Eliminar Correo", description = "Elimina un correo secundario")
  @DeleteMapping("/{emailId}")
  public ResponseEntity<UsuarioResponseDTO> removeEmail(@PathVariable UUID emailId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    }

    String emailToken = SecurityUtils.getEmailFromAuthentication(authentication);
    try {
      UsuarioResponseDTO updated = emailDelegate.removeEmail(emailToken, emailId);
      return ResponseEntity.ok(updated);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
