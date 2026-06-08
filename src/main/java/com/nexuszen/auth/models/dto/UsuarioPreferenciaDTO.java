package com.nexuszen.auth.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPreferenciaDTO {
  private UUID id;
  private ParametroPreferenciaDTO parametro;
  private Integer valorEntero;
  private Boolean valorBooleano;
  private LocalDateTime valorFecha;
  private String valorCadena;
}
