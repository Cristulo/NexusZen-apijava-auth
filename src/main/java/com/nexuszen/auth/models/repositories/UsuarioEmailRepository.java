package com.nexuszen.auth.models.repositories;

import com.nexuszen.auth.models.UsuarioEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioEmailRepository extends JpaRepository<UsuarioEmail, UUID> {
    Optional<UsuarioEmail> findByEmail(String email);
    boolean existsByEmail(String email);
}
