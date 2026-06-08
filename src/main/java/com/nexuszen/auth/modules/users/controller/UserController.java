package com.nexuszen.auth.modules.users.controller;

import com.nexuszen.auth.modules.users.delegate.UserDelegate;
import com.nexuszen.auth.modules.users.dto.UsuarioAdminUpdateDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioCreateDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/users")
@Tag(name = "Usuarios Admin", description = "Endpoints de CRUD completo para administradores")
public class UserController {

  private final UserDelegate userDelegate;

  public UserController(UserDelegate userDelegate) {
    this.userDelegate = userDelegate;
  }

  @Operation(summary = "Listar Usuarios", description = "Obtiene un listado paginado de todos los usuarios")
  @GetMapping
  public ResponseEntity<Page<UsuarioResponseDTO>> getAllUsers(Pageable pageable) {
    // Aquí idealmente habría un @PreAuthorize("hasRole('ADMIN')") o similar.
    return ResponseEntity.ok(userDelegate.getAllUsers(pageable));
  }

  @Operation(summary = "Obtener Usuario por ID", description = "Retorna los detalles de un usuario específico")
  @GetMapping("/{id}")
  public ResponseEntity<UsuarioResponseDTO> getUserById(@PathVariable UUID id) {
    try {
      return ResponseEntity.ok(userDelegate.getUserById(id));
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Crear Usuario", description = "Crea un usuario manualmente")
  @PostMapping
  public ResponseEntity<UsuarioResponseDTO> createUser(@RequestBody UsuarioCreateDTO dto) {
    try {
      UsuarioResponseDTO created = userDelegate.createUser(dto);
      return ResponseEntity.ok(created);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @Operation(summary = "Editar Usuario (Bulk)", description = "Edita múltiples campos de cualquier usuario en una sola operación")
  @PutMapping("/{id}")
  public ResponseEntity<UsuarioResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UsuarioAdminUpdateDTO dto) {
    try {
      UsuarioResponseDTO updated = userDelegate.updateUser(id, dto);
      return ResponseEntity.ok(updated);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @Operation(summary = "Desactivar/Dar de Baja", description = "Desactiva lógicamente cualquier usuario")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    try {
      userDelegate.deleteUser(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
