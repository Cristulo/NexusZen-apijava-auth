package com.nexuszen.auth.models.repositories;

import com.nexuszen.auth.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolRepository extends JpaRepository<Rol, UUID> {
  Optional<Rol> findByName(String name);
}
