package com.nexuszen.auth.modules.users.dto;

import com.nexuszen.auth.models.dto.UsuarioEmailDTO;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponseDTO {
  private UUID id;
  private String usuario;
  private String username;
  private String profileImageUrl;
  private String estado;
  private Set<String> roles;
  private Set<String> permisos;
  private Set<UsuarioEmailDTO> emails;
  private Map<String, Object> preferencias;
}
