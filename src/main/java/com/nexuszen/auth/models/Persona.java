package com.nexuszen.auth.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.time.LocalDate;

@Entity
@Table(name = "personas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id", nullable = false, unique = true)
  private Usuario usuario;

  @Column(length = 100)
  private String nombre;

  @Column(length = 100)
  private String apellido;

  @Column(length = 50)
  private String documento;

  private LocalDate fechaNacimiento;

  @Column(length = 50)
  private String telefono;

  @Column(length = 255)
  private String direccion;
}
