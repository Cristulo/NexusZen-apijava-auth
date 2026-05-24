package com.nexuszen.auth.modules.users.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UsuarioResponseDTO {
  private UUID id;
  private String email;
  private Boolean isActive;
  private Set<String> roles;
  private Set<String> permisos;
}
