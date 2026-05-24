package com.nexuszen.auth.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "permisos")
public class Rol {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true, length = 50)
  private String name; // Ej: "ROLE_ADMIN"

  @Column(length = 255)
  private String description;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "rol_permisos",
      joinColumns = @JoinColumn(name = "rol_id"),
      inverseJoinColumns = @JoinColumn(name = "permiso_id"))
  @Builder.Default
  private Set<Permiso> permisos = new HashSet<>();
}
