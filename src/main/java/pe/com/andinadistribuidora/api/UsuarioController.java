package pe.com.andinadistribuidora.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.UsuarioRequestDto;
import pe.com.andinadistribuidora.api.response.UsuarioResponseDto;
import pe.com.andinadistribuidora.service.RolService;
import pe.com.andinadistribuidora.service.UsuarioService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService; 

    // ========================================
    // 🔐 AUTENTICACIÓN - LOGIN
    // ========================================

    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        // Si ya está logueado, redirigir al home
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String usuario,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            // Buscar usuario por nombre de usuario
            UsuarioResponseDto usuarioDto = usuarioService.autenticar(usuario, password);

            // Guardar datos en sesión
            session.setAttribute("usuarioLogueado", usuarioDto);
            session.setAttribute("nombreCompleto", usuarioDto.getNombre() + " " + usuarioDto.getApellido());
            session.setAttribute("rol", usuarioDto.getRolDescripcion());

            log.info("Usuario '{}' autenticado correctamente con rol '{}'", usuario, usuarioDto.getRolDescripcion());
            return "redirect:/home";

        } catch (Exception e) {
            log.warn("Intento de login fallido para usuario '{}'", usuario);
            redirectAttributes.addFlashAttribute("error", "Usuario o contraseña incorrectos");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        String usuario = session.getAttribute("usuarioLogueado") != null 
            ? ((UsuarioResponseDto) session.getAttribute("usuarioLogueado")).getUsuario() 
            : "desconocido";
        
        session.invalidate();
        log.info("Usuario '{}' cerró sesión", usuario);
        
        redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada exitosamente");
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // Verificar si hay sesión activa
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        UsuarioResponseDto usuario = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);
        
        return "home"; // Vista principal según el rol
    }

    // ========================================
    // 📋 CRUD DE USUARIOS (Solo ADMIN)
    // ========================================

    @GetMapping("/usuarios")
    public String listar(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Verificar autenticación y rol
        if (!validarAccesoAdmin(session, redirectAttributes)) {
            return "redirect:/login";
        }

        model.addAttribute("lstUsuarios", usuarioService.listar());
        model.addAttribute("lstRoles", rolService.listar());
        model.addAttribute("usuarioRequestDto", new UsuarioRequestDto());
        
        return "listUsuario";
    }

    @PostMapping("/usuarios/guardar")
    public String guardar(@ModelAttribute UsuarioRequestDto usuarioRequestDto, 
                         HttpSession session,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        
        if (!validarAccesoAdmin(session, redirectAttributes)) {
            return "redirect:/login";
        }

        try {
            usuarioService.crear(usuarioRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
        } catch (Exception e) {
            log.error("Error al crear usuario: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editar(@PathVariable Integer id, 
                        HttpSession session,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        
        if (!validarAccesoAdmin(session, redirectAttributes)) {
            return "redirect:/login";
        }

        try {
            UsuarioResponseDto usuario = usuarioService.obtener(id);
            model.addAttribute("usuarioRequestDto", usuario);
            model.addAttribute("lstUsuarios", usuarioService.listar());
            model.addAttribute("lstRoles", rolService.listar());
            model.addAttribute("editando", true);
        } catch (Exception e) {
            log.error("Error al obtener usuario {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/usuarios";
        }

        return "crudusuarios";
    }

    @PostMapping("/usuarios/actualizar")
    public String actualizar(@ModelAttribute UsuarioRequestDto usuarioRequestDto,
                           @RequestParam Integer id,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        
        if (!validarAccesoAdmin(session, redirectAttributes)) {
            return "redirect:/login";
        }

        try {
            usuarioService.actualizar(id, usuarioRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
        } catch (Exception e) {
            log.error("Error al actualizar usuario {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        
        if (!validarAccesoAdmin(session, redirectAttributes)) {
            return "redirect:/login";
        }

        try {
            // Evitar que el admin se elimine a sí mismo
            UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
            if (usuarioLogueado.getId().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "No puedes eliminar tu propio usuario");
                return "redirect:/usuarios";
            }

            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar usuario {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/usuarios";
    }
    
    @GetMapping("/")
    public String index(HttpSession session) {
        // Si ya está logueado, ir al home
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/home";
        }
        // Si no, ir al login
        return "redirect:/login";
    }

    // ========================================
    // 🔒 MÉTODOS DE VALIDACIÓN DE ACCESO
    // ========================================

    private boolean validarAccesoAdmin(HttpSession session, RedirectAttributes redirectAttributes) {
        UsuarioResponseDto usuario = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
        
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión");
            return false;
        }

        if (!"ADMIN".equals(usuario.getRolDescripcion())) {
            redirectAttributes.addFlashAttribute("error", "No tiene permisos para acceder a esta sección");
            log.warn("Usuario '{}' intentó acceder sin permisos de ADMIN", usuario.getUsuario());
            return false;
        }

        return true;
    }
}