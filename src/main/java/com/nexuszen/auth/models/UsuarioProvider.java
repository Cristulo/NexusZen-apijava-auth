package com.nexuszen.auth.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "usuario_providers", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"provider_name", "provider_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "provider_name", nullable = false, length = 50)
    private String providerName; // Ej: "google", "microsoft", "apple"

    @Column(name = "provider_id", nullable = false, length = 255)
    private String providerId; // El ID único que devuelve Google/Microsoft para este usuario
}
