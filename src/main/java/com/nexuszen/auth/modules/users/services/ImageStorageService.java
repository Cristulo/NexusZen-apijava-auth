package com.nexuszen.auth.modules.users.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageStorageService {

  private static final Logger log = LoggerFactory.getLogger(ImageStorageService.class);

  @Value("${app.storage.avatar-dir:uploads/avatars}")
  private String avatarDir;

  public String storeAvatar(MultipartFile file, UUID userId) {
    try {
      Path uploadPath = Paths.get(avatarDir);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      String originalFilename = file.getOriginalFilename();
      String extension = "";
      if (originalFilename != null && originalFilename.contains(".")) {
        extension = originalFilename.substring(originalFilename.lastIndexOf("."));
      }
      
      String newFilename = userId.toString() + extension;
      Path filePath = uploadPath.resolve(newFilename);
      
      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
      
      log.info("Avatar saved for user {}: {}", userId, newFilename);
      
      // Devuelve una URL relativa para ser servida, por ejemplo /static/avatars/{newFilename}
      // o almacenamos la URI. En un entorno real se subiría a S3 y devolvería la URL de S3.
      return "/api/v1/auth/public/avatars/" + newFilename;
      
    } catch (IOException e) {
      log.error("Error al guardar el avatar para el usuario {}", userId, e);
      throw new RuntimeException("Error al guardar la imagen de perfil", e);
    }
  }
}
