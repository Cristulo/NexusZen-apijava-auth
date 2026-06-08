package com.nexuszen.auth.models.repositories;

import com.nexuszen.auth.models.UsuarioPreferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioPreferenciaRepository extends JpaRepository<UsuarioPreferencia, UUID> {
    List<UsuarioPreferencia> findByUsuarioId(UUID usuarioId);
    Optional<UsuarioPreferencia> findByUsuarioIdAndParametroNombre(UUID usuarioId, String parametroNombre);
}
