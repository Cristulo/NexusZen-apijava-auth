package com.nexuszen.auth.models.repositories;

import com.nexuszen.auth.models.Rol;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, UUID> {
  Optional<Rol> findByName(String name);
}
