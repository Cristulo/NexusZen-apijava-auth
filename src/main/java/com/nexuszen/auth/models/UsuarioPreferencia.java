package com.nexuszen.auth.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuario_preferencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPreferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parametro_id", nullable = false)
    private ParametroPreferencia parametro;

    @Column(name = "valor_entero")
    private Integer valorEntero;

    @Column(name = "valor_booleano")
    private Boolean valorBooleano;

    @Column(name = "valor_fecha")
    private LocalDateTime valorFecha;

    @Column(name = "valor_cadena")
    private String valorCadena;
}
