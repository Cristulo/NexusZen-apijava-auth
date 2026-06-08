package com.nexuszen.auth.modules.users.delegate;

import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.modules.users.dto.PasswordUpdateDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioUpdateDTO;
import com.nexuszen.auth.modules.users.services.ImageStorageService;
import com.nexuszen.auth.modules.users.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ProfileDelegate {

  private final UserService userService;
  private final ImageStorageService imageStorageService;

  public ProfileDelegate(UserService userService, ImageStorageService imageStorageService) {
    this.userService = userService;
    this.imageStorageService = imageStorageService;
  }

  public UsuarioResponseDTO getProfile(String email) {
    Usuario usuario = userService.getByEmail(email);
    return UserMapper.mapToDTO(usuario);
  }

  public UsuarioResponseDTO updateProfile(String email, UsuarioUpdateDTO dto) {
    Usuario usuario = userService.updateProfile(email, dto.getUsuario(), dto.getUsername());
    return UserMapper.mapToDTO(usuario);
  }

  public void updatePassword(String email, PasswordUpdateDTO dto) {
    userService.updatePassword(email, dto.getNewPassword());
  }

  public UsuarioResponseDTO updateAvatar(String email, MultipartFile file) {
    Usuario usuario = userService.getByEmail(email);
    String avatarUrl = imageStorageService.storeAvatar(file, usuario.getId());
    Usuario updated = userService.updateAvatar(email, avatarUrl);
    return UserMapper.mapToDTO(updated);
  }

  public void logicalDelete(String email) {
    userService.logicalDelete(email);
  }
}
