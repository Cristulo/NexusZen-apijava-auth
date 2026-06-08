package com.nexuszen.auth.modules.users.controller;

import com.nexuszen.auth.modules.users.delegate.AuthDelegate;
import com.nexuszen.auth.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints de sesión (Login, Registro, Logout)")
public class AuthController {

  private final AuthDelegate authDelegate;

  public AuthController(AuthDelegate authDelegate) {
    this.authDelegate = authDelegate;
  }

  @Operation(summary = "Login con Usuario", description = "Devuelve un JWT para el usuario y contraseña proporcionados.")
  @PostMapping("/public/login")
  public ResponseEntity<Map<String, String>> loginByUsuario(@RequestBody Map<String, String> request) {
    String usuarioStr = request.get("usuario");
    String passwordStr = request.get("password");
    if (usuarioStr == null || usuarioStr.isEmpty() || passwordStr == null || passwordStr.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    try {
      Map<String, String> response = authDelegate.login(usuarioStr, passwordStr);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(401).build();
    }
  }

  @Operation(summary = "Completar Registro", description = "Completa el registro de un usuario OAuth2 asignando usuario y username")
  @PostMapping("/completar-registro")
  public ResponseEntity<Map<String, String>> completarRegistro(@RequestBody Map<String, String> request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    }

    String email = SecurityUtils.getEmailFromAuthentication(authentication);
    String nuevoUsuario = request.get("usuario");
    String nuevoUsername = request.get("username");
    String nuevoPassword = request.get("password");

    if (nuevoUsuario == null || nuevoUsuario.isEmpty() 
        || nuevoUsername == null || nuevoUsername.isEmpty() 
        || nuevoPassword == null || nuevoPassword.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    try {
      authDelegate.completarRegistro(email, nuevoUsuario, nuevoUsername, nuevoPassword);
      Map<String, String> response = new HashMap<>();
      response.put("message", "Registro completado con éxito");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(409).build(); // Assuming conflict for duplicate username
    }
  }

  @Operation(summary = "Cerrar sesión en todos los dispositivos", description = "Revoca todos los tokens JWT emitidos para el usuario actual utilizando Redis")
  @PostMapping("/logout-all")
  public ResponseEntity<Map<String, String>> logoutAll() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
      return ResponseEntity.status(401).build();
    }

    String email = SecurityUtils.getEmailFromAuthentication(authentication);
    try {
      authDelegate.logoutAll(email);
      Map<String, String> response = new HashMap<>();
      response.put("message", "Sesiones revocadas exitosamente");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(404).build();
    }
  }
}
