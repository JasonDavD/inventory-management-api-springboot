package pe.com.andinadistribuidora.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.UsuarioRequestDto;
import pe.com.andinadistribuidora.api.response.UsuarioResponseDto;
import pe.com.andinadistribuidora.entity.Rol;
import pe.com.andinadistribuidora.entity.Usuario;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.mapper.UsuarioMapper;
import pe.com.andinadistribuidora.repository.RolRepository;
import pe.com.andinadistribuidora.repository.UsuarioRepository;
import pe.com.andinadistribuidora.service.UsuarioService;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final UsuarioMapper mapper;

    @Override
    public UsuarioResponseDto crear(UsuarioRequestDto req) {
        log.info("Creando usuario '{}'", req.getUsuario());

        // Validar que el nombre de usuario no exista
        if (usuarioRepo.findByUsuario(req.getUsuario()).isPresent()) {
            throw new BusinessException("Ya existe un usuario con el nombre: " + req.getUsuario());
        }

        // Validar rol obligatorio y existente
        if (req.getRolId() == null) {
            throw new BusinessException("Debe indicar un rol (rolId).");
        }
        Rol rol = rolRepo.findById(req.getRolId())
                .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + req.getRolId()));

        // Mapear DTO -> Entity
        Usuario entity = mapper.toEntity(req);
        entity.setRol(rol);
        entity.setCreadoEn(LocalDateTime.now());

        // Reglas de negocio
        validarDatosUsuario(entity);
        entity.setNombre(entity.getNombre().trim());
        entity.setApellido(entity.getApellido().trim());
        entity.setUsuario(entity.getUsuario().trim().toLowerCase());

        Usuario saved = usuarioRepo.save(entity);
        log.debug("Usuario creado id='{}'", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public UsuarioResponseDto actualizar(Integer idUsuario, UsuarioRequestDto req) {
        log.info("Actualizando usuario id='{}'", idUsuario);

        Usuario actual = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + idUsuario));

        // Validar que el nombre de usuario no esté en uso por otro usuario
        usuarioRepo.findByUsuario(req.getUsuario()).ifPresent(u -> {
            if (!u.getId().equals(idUsuario)) {
                throw new BusinessException("El nombre de usuario ya está en uso: " + req.getUsuario());
            }
        });

        // Actualizaciones parciales con MapStruct (ignora nulos)
        mapper.updateEntityFromDto(req, actual);

        // Si viene rolId y quieres permitir cambiarlo:
        if (req.getRolId() != null) {
            Rol nuevoRol = rolRepo.findById(req.getRolId())
                    .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + req.getRolId()));
            actual.setRol(nuevoRol);
        }

        // Reglas de negocio
        validarDatosUsuario(actual);
        if (actual.getNombre() != null) {
            actual.setNombre(actual.getNombre().trim());
        }
        if (actual.getApellido() != null) {
            actual.setApellido(actual.getApellido().trim());
        }
        if (actual.getUsuario() != null) {
            actual.setUsuario(actual.getUsuario().trim().toLowerCase());
        }

        Usuario saved = usuarioRepo.save(actual);
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminar(Integer idUsuario) {
        log.info("Eliminando usuario id='{}'", idUsuario);
        Usuario u = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + idUsuario));
        usuarioRepo.delete(u);
        log.debug("Usuario eliminado id='{}'", idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDto obtener(Integer idUsuario) {
        Usuario u = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + idUsuario));
        return mapper.toResponseDto(u);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listar() {
        return usuarioRepo.findAll().stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listarPorRol(Integer idRol) {
        if (!rolRepo.existsById(idRol)) {
            throw new NotFoundException("Rol no encontrado: " + idRol);
        }
        return usuarioRepo.findByRolId(idRol)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    // === Reglas de negocio ===
    private void validarDatosUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            throw new BusinessException("El apellido es obligatorio.");
        }
        if (usuario.getUsuario() == null || usuario.getUsuario().trim().isEmpty()) {
            throw new BusinessException("El nombre de usuario es obligatorio.");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new BusinessException("La contraseña es obligatoria.");
        }
        if (usuario.getPassword().length() < 4) {
            throw new BusinessException("La contraseña debe tener al menos 4 caracteres.");
        }
        if (usuario.getActivo() == null) {
            throw new BusinessException("El estado activo es obligatorio.");
        }
    }

	@Override
	@Transactional(readOnly = true)
	public UsuarioResponseDto autenticar(String usuario, String password) {
	    log.info("Intentando autenticar usuario '{}'", usuario);
	    
	    Usuario u = usuarioRepo.findByUsuario(usuario)
	            .orElseThrow(() -> new NotFoundException("Usuario o contraseña incorrectos"));
	    
	    // Verificar que el usuario esté activo
	    if (!u.getActivo()) {
	        throw new BusinessException("Usuario inactivo. Contacte al administrador.");
	    }
	    
	    // Validar contraseña (en producción deberías usar BCrypt)
	    if (!password.equals(u.getPassword())) {
	        throw new BusinessException("Usuario o contraseña incorrectos");
	    }
	    
	    log.info("Usuario '{}' autenticado exitosamente", usuario);
	    return mapper.toResponseDto(u);
	}
}