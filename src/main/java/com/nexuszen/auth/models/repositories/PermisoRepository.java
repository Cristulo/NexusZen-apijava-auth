package com.nexuszen.auth.models.repositories;

import com.nexuszen.auth.models.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, UUID> {
    Optional<Permiso> findByName(String name);
}
