package com.nexuszen.auth.modules.users.delegate;

import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.modules.users.dto.PreferenceUpdateDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import com.nexuszen.auth.modules.users.services.PreferenceService;
import org.springframework.stereotype.Component;

@Component
public class PreferenceDelegate {

  private final PreferenceService preferenceService;

  public PreferenceDelegate(PreferenceService preferenceService) {
    this.preferenceService = preferenceService;
  }

  public UsuarioResponseDTO updatePreference(String email, PreferenceUpdateDTO dto) {
    Usuario usuario = preferenceService.updatePreference(email, dto);
    return UserMapper.mapToDTO(usuario);
  }
}
