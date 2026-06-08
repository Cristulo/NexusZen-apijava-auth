package com.nexuszen.auth.models.repositories;

import com.nexuszen.auth.models.Persona;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, UUID> {
  Optional<Persona> findByUsuarioId(UUID usuarioId);
}
