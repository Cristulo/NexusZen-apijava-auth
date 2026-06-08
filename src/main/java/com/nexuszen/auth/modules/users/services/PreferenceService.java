package com.nexuszen.auth.modules.users.services;

import com.nexuszen.auth.models.ParametroPreferencia;
import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.UsuarioPreferencia;
import com.nexuszen.auth.modules.users.dto.PreferenceUpdateDTO;
import com.nexuszen.auth.models.repositories.ParametroPreferenciaRepository;
import com.nexuszen.auth.models.repositories.UsuarioPreferenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PreferenceService {
  private final ParametroPreferenciaRepository parametroPreferenciaRepository;
  private final UsuarioPreferenciaRepository usuarioPreferenciaRepository;
  private final UserService userService;

  public PreferenceService(ParametroPreferenciaRepository parametroPreferenciaRepository,
                           UsuarioPreferenciaRepository usuarioPreferenciaRepository,
                           UserService userService) {
    this.parametroPreferenciaRepository = parametroPreferenciaRepository;
    this.usuarioPreferenciaRepository = usuarioPreferenciaRepository;
    this.userService = userService;
  }

  public Usuario updatePreference(String email, PreferenceUpdateDTO dto) {
    Usuario usuario = userService.getByEmail(email);

    ParametroPreferencia parametro = parametroPreferenciaRepository.findByNombre(dto.getParametro())
        .orElseGet(() -> {
            ParametroPreferencia nuevoParam = ParametroPreferencia.builder()
                .nombre(dto.getParametro())
                .descripcion("Parámetro autogenerado: " + dto.getParametro())
                .build();
            return parametroPreferenciaRepository.save(nuevoParam);
        });

    UsuarioPreferencia preferencia = usuarioPreferenciaRepository
        .findByUsuarioIdAndParametroNombre(usuario.getId(), dto.getParametro())
        .orElseGet(() -> UsuarioPreferencia.builder()
            .usuario(usuario)
            .parametro(parametro)
            .build());

    if (dto.getValorBooleano() != null) preferencia.setValorBooleano(dto.getValorBooleano());
    if (dto.getValorEntero() != null) preferencia.setValorEntero(dto.getValorEntero());
    if (dto.getValorCadena() != null) preferencia.setValorCadena(dto.getValorCadena());
    if (dto.getValorFecha() != null) preferencia.setValorFecha(dto.getValorFecha());

    usuarioPreferenciaRepository.save(preferencia);

    return userService.getById(usuario.getId()); // Refresh
  }
}
