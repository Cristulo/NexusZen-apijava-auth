package com.nexuszen.auth.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametroPreferenciaDTO {
  private UUID id;
  private String nombre;
  private String tipoDato;
  private String descripcion;
}
