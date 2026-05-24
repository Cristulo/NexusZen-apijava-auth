package com.nexuszen.auth.modules.roles.services;

import com.nexuszen.auth.models.Rol;
import com.nexuszen.auth.models.Usuario;
import com.nexuszen.auth.models.repositories.RolRepository;
import com.nexuszen.auth.repositories.UsuarioRepository;
import com.nexuszen.auth.modules.roles.dto.AssignRoleRequestDTO;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class RoleService {
  private final RolRepository rolRepository;
  private final UsuarioRepository usuarioRepository;

  public RoleService(RolRepository rolRepository, UsuarioRepository usuarioRepository) {
    this.rolRepository = rolRepository;
    this.usuarioRepository = usuarioRepository;
  }

  public List<Rol> getAllRoles() {
    return rolRepository.findAll();
  }

  public void assignRolesToUser(AssignRoleRequestDTO dto) {
    Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
    List<Rol> rolesAsignar = rolRepository.findAllById(dto.getRolIds());
    usuario.setRoles(new HashSet<>(rolesAsignar));
    
    usuarioRepository.save(usuario);
  }
}
