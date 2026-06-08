package com.nexuszen.auth.models.repositories;

import com.nexuszen.auth.models.ParametroPreferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParametroPreferenciaRepository extends JpaRepository<ParametroPreferencia, UUID> {
    Optional<ParametroPreferencia> findByNombre(String nombre);
}
