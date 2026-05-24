package com.nexuszen.auth.modules.roles.dto;

import lombok.Data;
import java.util.UUID;
import java.util.Set;

@Data
public class AssignRoleRequestDTO {
    private UUID usuarioId;
    private Set<UUID> rolIds;
}
