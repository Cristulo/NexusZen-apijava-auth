package com.nexuszen.auth.models.repositories;

import com.nexuszen.auth.models.UsuarioProvider;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioProviderRepository extends JpaRepository<UsuarioProvider, UUID> {
  Optional<UsuarioProvider> findByProviderNameAndProviderId(String providerName, String providerId);
  Optional<UsuarioProvider> findByUsuarioIdAndProviderName(UUID usuarioId, String providerName);
}
