package com.nexuszen.auth.modules.users.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.nexuszen.auth.models.ParametroPreferencia;
import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.UsuarioPreferencia;
import com.nexuszen.auth.models.enums.CategoriaPreferencia;
import com.nexuszen.auth.modules.users.dto.PreferenceUpdateDTO;
import com.nexuszen.auth.models.repositories.ParametroPreferenciaRepository;
import com.nexuszen.auth.models.repositories.UsuarioPreferenciaRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PreferenceServiceTest {

  @Mock private ParametroPreferenciaRepository parametroPreferenciaRepository;
  @Mock private UsuarioPreferenciaRepository usuarioPreferenciaRepository;
  @Mock private UserService userService;

  @InjectMocks private PreferenceService preferenceService;

  private Usuario testUsuario;
  private ParametroPreferencia existingParam;

  @BeforeEach
  void setUp() {
    testUsuario = Usuario.builder()
        .id(UUID.randomUUID())
        .usuario("testuser")
        .build();

    existingParam = ParametroPreferencia.builder()
        .id(UUID.randomUUID())
        .nombre("tema_oscuro")
        .descripcion("Tema oscuro")
        .categoria(CategoriaPreferencia.FRONTEND)
        .build();
  }

  @Test
  void updatePreference_ExistingParameter_Success() {
    PreferenceUpdateDTO dto = PreferenceUpdateDTO.builder()
        .parametro("tema_oscuro")
        .valorBooleano(true)
        .build();

    when(userService.getByEmail("test@example.com")).thenReturn(testUsuario);
    when(parametroPreferenciaRepository.findByNombre("tema_oscuro")).thenReturn(Optional.of(existingParam));
    when(usuarioPreferenciaRepository.findByUsuarioIdAndParametroNombre(testUsuario.getId(), "tema_oscuro"))
        .thenReturn(Optional.empty());
    when(userService.getById(testUsuario.getId())).thenReturn(testUsuario);

    Usuario result = preferenceService.updatePreference("test@example.com", dto);

    assertNotNull(result);
    ArgumentCaptor<UsuarioPreferencia> preferenceCaptor = ArgumentCaptor.forClass(UsuarioPreferencia.class);
    verify(usuarioPreferenciaRepository).save(preferenceCaptor.capture());
    
    UsuarioPreferencia saved = preferenceCaptor.getValue();
    assertEquals(testUsuario, saved.getUsuario());
    assertEquals(existingParam, saved.getParametro());
    assertEquals(true, saved.getValorBooleano());
  }

  @Test
  void updatePreference_NewParameter_Success() {
    PreferenceUpdateDTO dto = PreferenceUpdateDTO.builder()
        .parametro("nuevo_param")
        .valorCadena("algun_valor")
        .build();

    when(userService.getByEmail("test@example.com")).thenReturn(testUsuario);
    when(parametroPreferenciaRepository.findByNombre("nuevo_param")).thenReturn(Optional.empty());
    
    ParametroPreferencia savedParam = ParametroPreferencia.builder()
        .id(UUID.randomUUID())
        .nombre("nuevo_param")
        .descripcion("Parámetro autogenerado: nuevo_param")
        .categoria(CategoriaPreferencia.SISTEMA)
        .build();
    when(parametroPreferenciaRepository.save(any(ParametroPreferencia.class))).thenReturn(savedParam);

    when(usuarioPreferenciaRepository.findByUsuarioIdAndParametroNombre(testUsuario.getId(), "nuevo_param"))
        .thenReturn(Optional.empty());
    when(userService.getById(testUsuario.getId())).thenReturn(testUsuario);

    Usuario result = preferenceService.updatePreference("test@example.com", dto);

    assertNotNull(result);
    
    // Verificar que se haya guardado el nuevo parámetro con categoría SISTEMA
    ArgumentCaptor<ParametroPreferencia> paramCaptor = ArgumentCaptor.forClass(ParametroPreferencia.class);
    verify(parametroPreferenciaRepository).save(paramCaptor.capture());
    assertEquals("nuevo_param", paramCaptor.getValue().getNombre());
    assertEquals(CategoriaPreferencia.SISTEMA, paramCaptor.getValue().getCategoria());

    // Verificar que se haya guardado la preferencia
    ArgumentCaptor<UsuarioPreferencia> preferenceCaptor = ArgumentCaptor.forClass(UsuarioPreferencia.class);
    verify(usuarioPreferenciaRepository).save(preferenceCaptor.capture());
    assertEquals("algun_valor", preferenceCaptor.getValue().getValorCadena());
  }
}
