package com.nexuszen.auth.modules.users.controller;

import com.nexuszen.auth.modules.users.delegate.PreferenceDelegate;
import com.nexuszen.auth.modules.users.dto.PreferenceUpdateDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import com.nexuszen.auth.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/me/preferencias")
@Tag(name = "Preferencias", description = "Endpoints para la gestión de las preferencias del usuario")
public class PreferenceController {

  private final PreferenceDelegate preferenceDelegate;

  public PreferenceController(PreferenceDelegate preferenceDelegate) {
    this.preferenceDelegate = preferenceDelegate;
  }

  @Operation(summary = "Actualizar Preferencia", description = "Crea o actualiza el valor de una preferencia del usuario actual")
  @PostMapping
  public ResponseEntity<UsuarioResponseDTO> updatePreference(@RequestBody PreferenceUpdateDTO dto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    }

    String email = SecurityUtils.getEmailFromAuthentication(authentication);
    try {
      UsuarioResponseDTO updated = preferenceDelegate.updatePreference(email, dto);
      return ResponseEntity.ok(updated);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
