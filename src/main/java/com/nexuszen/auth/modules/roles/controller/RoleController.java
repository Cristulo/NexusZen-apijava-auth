package com.nexuszen.auth.modules.roles.controller;

import com.nexuszen.auth.models.Rol;
import com.nexuszen.auth.modules.roles.dto.AssignRoleRequestDTO;
import com.nexuszen.auth.modules.roles.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/roles")
@Tag(name = "Gestión de Roles", description = "Endpoints administrativos para asignar roles.")
public class RoleController {

  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @Operation(
      summary = "Listar Roles",
      description = "Obtiene todos los roles disponibles en el sistema.")
  @GetMapping
  public ResponseEntity<List<Rol>> getAllRoles() {
    return ResponseEntity.ok(roleService.getAllRoles());
  }

  @Operation(
      summary = "Asignar Roles",
      description = "Asigna uno o múltiples roles a un usuario específico.")
  @PostMapping("/assign")
  public ResponseEntity<Void> assignRoles(@RequestBody AssignRoleRequestDTO request) {
    roleService.assignRolesToUser(request);
    return ResponseEntity.ok().build();
  }
}
