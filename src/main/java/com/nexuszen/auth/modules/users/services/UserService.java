package com.nexuszen.auth.modules.users.services;

import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.Rol;
import com.nexuszen.auth.models.Permiso;
import com.nexuszen.auth.repositories.UsuarioRepository;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UsuarioRepository usuarioRepository;

    public UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponseDTO getProfileByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .isActive(usuario.getIsActive())
                .roles(usuario.getRoles().stream().map(Rol::getName).collect(Collectors.toSet()))
                .permisos(usuario.getRoles().stream()
                        .flatMap(rol -> rol.getPermisos().stream())
                        .map(Permiso::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
