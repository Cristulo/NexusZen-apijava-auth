package com.nexuszen.auth.models;

import com.nexuszen.auth.models.enums.EmailCategory;
import com.nexuszen.auth.models.enums.EmailType;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "usuario_emails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailType tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailCategory categoria;

    @Column(nullable = false)
    @Builder.Default
    private Boolean verified = false;
}
