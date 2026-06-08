package com.nexuszen.auth.modules.users.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAdminUpdateDTO {
  private String usuario;
  private String username;
  private String estado;
  private Set<String> roles;
}
