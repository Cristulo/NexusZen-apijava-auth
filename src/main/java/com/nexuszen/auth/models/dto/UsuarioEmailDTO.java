package com.nexuszen.auth.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioEmailDTO {
    private String email;
    private String tipo;
    private String categoria;
    private Boolean verified;
}
