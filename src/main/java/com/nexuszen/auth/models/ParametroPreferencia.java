package com.nexuszen.auth.models;

import com.nexuszen.auth.models.enums.CategoriaPreferencia;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "parametros_preferencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParametroPreferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaPreferencia categoria;
}
