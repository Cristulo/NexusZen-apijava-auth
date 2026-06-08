package com.nexuszen.auth.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import com.nexuszen.auth.models.enums.EstadoUsuario;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = true, unique = true)
  private String usuario; // Unique login, nullable because it might not be complete yet for OAuth

  @Column(nullable = true)
  private String username; // Display name

  @Column(nullable = true)
  private String profileImageUrl;

  @Column(nullable = true)
  private String passwordHash; // Opcional, ya que puede entrar solo con OAuth

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private EstadoUsuario estado = EstadoUsuario.ACTIVO;

  @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Persona persona;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "usuario_roles",
      joinColumns = @JoinColumn(name = "usuario_id"),
      inverseJoinColumns = @JoinColumn(name = "rol_id"))
  @Builder.Default
  private Set<Rol> roles = new HashSet<>();

  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<UsuarioProvider> providers = new HashSet<>();

  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<UsuarioEmail> emails = new HashSet<>();

  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<UsuarioPreferencia> preferencias = new HashSet<>();
}
