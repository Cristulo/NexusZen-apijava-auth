package com.nexuszen.auth.modules.users.controller;

import com.nexuszen.auth.modules.users.services.UserService;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints de identidad y perfil de usuario")
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "Obtener Perfil", description = "Retorna el perfil del usuario autenticado, incluyendo roles y permisos.")
  @GetMapping("/me")
  public ResponseEntity<UsuarioResponseDTO> getMyProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
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
