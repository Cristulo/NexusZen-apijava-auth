package com.nexuszen.auth.modules.users.controller;

import com.nexuszen.auth.modules.users.delegate.ProfileDelegate;
import com.nexuszen.auth.modules.users.dto.PasswordUpdateDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioUpdateDTO;
import com.nexuszen.auth.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth/me")
@Tag(name = "Perfil", description = "Endpoints para la gestión del propio perfil de usuario")
public class ProfileController {

  private final ProfileDelegate profileDelegate;

  public ProfileController(ProfileDelegate profileDelegate) {
    this.profileDelegate = profileDelegate;
  }

  @Operation(summary = "Obtener Perfil", description = "Retorna el perfil del usuario autenticado, incluyendo roles y permisos.")
  @GetMapping
  public ResponseEntity<UsuarioResponseDTO> getMyProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    }

    String email = SecurityUtils.getEmailFromAuthentication(authentication);
    return ResponseEntity.ok(profileDelegate.getProfile(email));
  }

  @Operation(summary = "Actualizar Perfil (Bulk)", description = "Actualiza múltiples campos del perfil en una sola operación (ej: username y @usuario)")
  @PutMapping
  public ResponseEntity<UsuarioResponseDTO> updateMyProfile(@RequestBody UsuarioUpdateDTO dto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    }
    
    String email = SecurityUtils.getEmailFromAuthentication(authentication);
    try {
      UsuarioResponseDTO updated = profileDelegate.updateProfile(email, dto);
      return ResponseEntity.ok(updated);
    } catch (Exception e) {
      e.printStackTrace();
      if (e.getMessage() != null && e.getMessage().contains("Usuario no encontrado")) {
        return ResponseEntity.status(401).build();
      }
      return ResponseEntity.badRequest().build();
    }
  }

  @Operation(summary = "Actualizar Contraseña", description = "Asigna una nueva contraseña al usuario")
  @PutMapping("/password")
  public ResponseEntity<Map<String, String>> updatePassword(@RequestBody PasswordUpdateDTO dto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    }
    
    String email = SecurityUtils.getEmailFromAuthentication(authentication);
    try {
      profileDelegate.updatePassword(email, dto);
      Map<String, String> response = new HashMap<>();
      response.put("message", "Contraseña actualizada exitosamente");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().contains("Usuario no encontrado")) {
        return ResponseEntity.status(401).build();
      }
      return ResponseEntity.badRequest().build();
    }
  }

  @Operation(summary = "Actualizar Avatar", description = "Sube y actualiza la imagen de perfil del usuario")
  @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UsuarioResponseDTO> updateAvatar(@RequestParam("file") MultipartFile file) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    }

    if (file.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    String email = SecurityUtils.getEmailFromAuthentication(authentication);
    try {
      UsuarioResponseDTO updated = profileDelegate.updateAvatar(email, file);
      return ResponseEntity.ok(updated);
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().contains("Usuario no encontrado")) {
        return ResponseEntity.status(401).build();
      }
      return ResponseEntity.badRequest().build();
    }
  }

  @Operation(summary = "Desactivar cuenta", description = "Da de baja lógica al usuario actual (Estado INACTIVO)")
  @DeleteMapping
  public ResponseEntity<Map<String, String>> deleteMyAccount() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    }
    
    String email = SecurityUtils.getEmailFromAuthentication(authentication);
    try {
      profileDelegate.logicalDelete(email);
      Map<String, String> response = new HashMap<>();
      response.put("message", "Cuenta desactivada correctamente");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      if (e.getMessage() != null && e.getMessage().contains("Usuario no encontrado")) {
        return ResponseEntity.status(401).build();
      }
      return ResponseEntity.badRequest().build();
    }
  }
}
