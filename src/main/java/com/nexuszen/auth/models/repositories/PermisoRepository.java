package com.nexuszen.auth.models.repositories;

import com.nexuszen.auth.models.Permiso;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, UUID> {
  Optional<Permiso> findByName(String name);
}
