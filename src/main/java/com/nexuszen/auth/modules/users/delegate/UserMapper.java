package com.nexuszen.auth.modules.users.delegate;

import com.nexuszen.auth.models.Permiso;
import com.nexuszen.auth.models.Rol;
import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.dto.UsuarioEmailDTO;
import com.nexuszen.auth.modules.users.dto.UsuarioResponseDTO;
import java.util.stream.Collectors;

public class UserMapper {

  public static UsuarioResponseDTO mapToDTO(Usuario usuario) {
    if (usuario == null) return null;

    return UsuarioResponseDTO.builder()
        .id(usuario.getId())
        .usuario(usuario.getUsuario())
        .username(usuario.getUsername())
        .profileImageUrl(usuario.getProfileImageUrl())
        .estado(usuario.getEstado().name())
        .roles(usuario.getRoles().stream().map(Rol::getName).collect(Collectors.toSet()))
        .permisos(
            usuario.getRoles().stream()
                .flatMap(rol -> rol.getPermisos().stream())
                .map(Permiso::getName)
                .collect(Collectors.toSet()))
        .emails(
            usuario.getEmails().stream()
                .map(e -> UsuarioEmailDTO.builder()
                    .email(e.getEmail())
                    .tipo(e.getTipo().name())
                    .categoria(e.getCategoria().name())
                    .verified(e.getVerified())
                    .build())
                .collect(Collectors.toSet()))
        .preferencias(
            usuario.getPreferencias().stream()
                .collect(Collectors.toMap(
                    p -> p.getParametro().getNombre(),
                    p -> {
                        if (p.getValorBooleano() != null) return p.getValorBooleano();
                        if (p.getValorEntero() != null) return p.getValorEntero();
                        if (p.getValorCadena() != null) return p.getValorCadena();
                        if (p.getValorFecha() != null) return p.getValorFecha();
                        return "";
                    }
                ))
        )
        .build();
  }
}
