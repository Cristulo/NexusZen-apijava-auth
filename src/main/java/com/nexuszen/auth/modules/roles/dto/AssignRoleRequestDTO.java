package com.nexuszen.auth.modules.roles.dto;

import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class AssignRoleRequestDTO {
  private UUID usuarioId;
  private Set<UUID> rolIds;
}
