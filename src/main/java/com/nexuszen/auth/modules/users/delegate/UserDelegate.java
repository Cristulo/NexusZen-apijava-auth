package com.nexuszen.auth.modules.users.delegate;

import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.modules.users.dto.UsuarioAdminUpdateDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioCreateDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import com.nexuszen.auth.modules.users.services.UserService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class UserDelegate {

  private final UserService userService;

  public UserDelegate(UserService userService) {
    this.userService = userService;
  }

  public Page<UsuarioResponseDTO> getAllUsers(Pageable pageable) {
    return userService.getAllUsers(pageable)
        .map(UserMapper::mapToDTO);
  }

  public UsuarioResponseDTO getUserById(UUID id) {
    Usuario usuario = userService.getById(id);
    return UserMapper.mapToDTO(usuario);
  }

  public UsuarioResponseDTO createUser(UsuarioCreateDTO dto) {
    Usuario usuario = userService.createUser(
        dto.getUsuario(),
        dto.getUsername(),
        dto.getEmail(),
        dto.getPassword()
    );

    return UserMapper.mapToDTO(usuario);
  }

  public UsuarioResponseDTO updateUser(UUID id, UsuarioAdminUpdateDTO dto) {
    Usuario usuario = userService.updateAdmin(
        id,
        dto.getUsuario(),
        dto.getUsername(),
        dto.getEstado()
    );

    return UserMapper.mapToDTO(usuario);
  }

  public void deleteUser(UUID id) {
    userService.logicalDeleteById(id);
  }
}
