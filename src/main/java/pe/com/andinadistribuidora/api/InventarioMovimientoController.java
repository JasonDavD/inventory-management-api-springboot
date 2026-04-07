package pe.com.andinadistribuidora.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.InventarioMovimientoRequestDto;
import pe.com.andinadistribuidora.service.AlmacenService;
import pe.com.andinadistribuidora.service.InventarioMovimientoService;
import pe.com.andinadistribuidora.service.ProductoService;

@Slf4j
@Controller
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class InventarioMovimientoController {
    
    private final InventarioMovimientoService movimientoService;
    private final AlmacenService almacenService;
    private final ProductoService productoService;
    
    @GetMapping
    public String listar(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Verificar autenticación
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión");
            return "redirect:/login";
        }
        
        model.addAttribute("lstMovimientos", movimientoService.listarUltimosMovimientos());
        model.addAttribute("lstAlmacenes", almacenService.listar());
        model.addAttribute("lstProductos", productoService.listar());
        model.addAttribute("movimientoRequestDto", new InventarioMovimientoRequestDto());
        return "listMovimientos";
    }
    
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute InventarioMovimientoRequestDto movimientoRequestDto, 
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        
        try {
            // Agregar usuario de la sesión
            String nombreUsuario = (String) session.getAttribute("nombreCompleto");
            if (nombreUsuario != null) {
                movimientoRequestDto.setUsuario(nombreUsuario);
            }
            
            // Establecer fecha actual si no viene
            if (movimientoRequestDto.getFechaMovimiento() == null) {
                movimientoRequestDto.setFechaMovimiento(LocalDateTime.now());
            }
            
            movimientoService.crear(movimientoRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Movimiento registrado exitosamente");
        } catch (Exception e) {
            log.error("Error al crear movimiento: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/movimientos";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, 
                        HttpSession session,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        
        try {
            model.addAttribute("movimientoRequestDto", movimientoService.obtener(id));
            model.addAttribute("lstMovimientos", movimientoService.listarUltimosMovimientos());
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("editando", true);
        } catch (Exception e) {
            log.error("Error al obtener movimiento {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Movimiento no encontrado");
            return "redirect:/movimientos";
        }
        
        return "listMovimientos";
    }
    
    // --- 4. ACTUALIZAR MOVIMIENTO ---
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute InventarioMovimientoRequestDto movimientoRequestDto,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        
        try {
            // El ID debe venir en el DTO
            if (movimientoRequestDto.getId() == null) {
                throw new IllegalArgumentException("El ID del movimiento es obligatorio");
            }
            
            movimientoService.actualizar(movimientoRequestDto.getId(), movimientoRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Movimiento actualizado exitosamente");
        } catch (Exception e) {
            log.error("Error al actualizar movimiento: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/movimientos";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        
        try {
            movimientoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Movimiento eliminado exitosamente (inventario revertido)");
        } catch (Exception e) {
            log.error("Error al eliminar movimiento {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/movimientos";
    }
    
    // --- 6. VER TODOS LOS MOVIMIENTOS ---
    @GetMapping("/todos")
    public String listarTodos(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión");
            return "redirect:/login";
        }
        
        model.addAttribute("lstMovimientos", movimientoService.listar());
        model.addAttribute("lstAlmacenes", almacenService.listar());
        model.addAttribute("lstProductos", productoService.listar());
        model.addAttribute("movimientoRequestDto", new InventarioMovimientoRequestDto());
        return "listMovimientos";
    }
    
    // --- 7. FILTRAR POR ALMACÉN ---
    @GetMapping("/almacen/{almacenId}")
    public String listarPorAlmacen(@PathVariable Integer almacenId,
                                   HttpSession session,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión");
            return "redirect:/login";
        }
        
        try {
            model.addAttribute("lstMovimientos", movimientoService.listarPorAlmacen(almacenId));
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("almacenSeleccionado", almacenId);
            model.addAttribute("movimientoRequestDto", new InventarioMovimientoRequestDto());
            return "listMovimientos";
        } catch (Exception e) {
            log.error("Error al listar movimientos por almacén {}: {}", almacenId, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/movimientos";
        }
    }
    
    @GetMapping("/producto/{productoId}")
    public String listarPorProducto(@PathVariable Integer productoId,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión");
            return "redirect:/login";
        }
        
        try {
            model.addAttribute("lstMovimientos", movimientoService.listarPorProducto(productoId));
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("productoSeleccionado", productoId);
            model.addAttribute("movimientoRequestDto", new InventarioMovimientoRequestDto());
            return "listMovimientos";
        } catch (Exception e) {
            log.error("Error al listar movimientos por producto {}: {}", productoId, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/movimientos";
        }
    }
    
    @GetMapping("/tipo/{tipoMovimiento}")
    public String listarPorTipo(@PathVariable String tipoMovimiento,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión");
            return "redirect:/login";
        }
        
        try {
            model.addAttribute("lstMovimientos", movimientoService.listarPorTipo(tipoMovimiento));
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("tipoSeleccionado", tipoMovimiento);
            model.addAttribute("movimientoRequestDto", new InventarioMovimientoRequestDto());
            return "listMovimientos";
        } catch (Exception e) {
            log.error("Error al listar movimientos por tipo {}: {}", tipoMovimiento, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/movimientos";
        }
    }
    
    @GetMapping("/filtrar-fechas")
    public String listarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión");
            return "redirect:/login";
        }
        
        try {
            // Convertir fechas a LocalDateTime (inicio del día - fin del día)
            LocalDateTime inicio = fechaInicio.atStartOfDay();
            LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
            
            model.addAttribute("lstMovimientos", movimientoService.listarPorFechas(inicio, fin));
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("fechaInicio", fechaInicio);
            model.addAttribute("fechaFin", fechaFin);
            model.addAttribute("movimientoRequestDto", new InventarioMovimientoRequestDto());
            return "listMovimientos";
        } catch (Exception e) {
            log.error("Error al listar movimientos por fechas: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/movimientos";
        }
    }
}