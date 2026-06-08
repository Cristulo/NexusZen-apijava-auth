package com.nexuszen.auth.modules.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceUpdateDTO {
  private String parametro;
  private Boolean valorBooleano;
  private Integer valorEntero;
  private String valorCadena;
  private LocalDateTime valorFecha;
}
