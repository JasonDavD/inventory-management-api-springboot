package pe.com.andinadistribuidora.api;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.CategoriaRequestDto;
import pe.com.andinadistribuidora.service.CategoriaService;

@Slf4j
@Controller
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    
    private final CategoriaService categoriaService;
    
    // --- 1. LISTAR CATEGORÍAS ---
    @GetMapping
    public String listar(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Verificar autenticación
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión");
            return "redirect:/login";
        }
        
        model.addAttribute("lstCategorias", categoriaService.listar());
        model.addAttribute("categoriaRequestDto", new CategoriaRequestDto());
        return "listCategoria";  //OJO AQUI!!!!
    }
    
    // --- 2. CREAR CATEGORÍA ---
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute CategoriaRequestDto categoriaRequestDto, 
                         HttpSession session,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        
        try {
            categoriaService.crear(categoriaRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Categoría creada exitosamente");
        } catch (Exception e) {
            log.error("Error al crear categoría: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/categorias";
    }
    
    // --- 3. EDITAR CATEGORÍA (mostrar datos en form) ---
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, 
                        HttpSession session,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        
        try {
            model.addAttribute("categoriaRequestDto", categoriaService.obtener(id));
            model.addAttribute("lstCategorias", categoriaService.listar());
            model.addAttribute("editando", true);
        } catch (Exception e) {
            log.error("Error al obtener categoría {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Categoría no encontrada");
            return "redirect:/categorias";
        }
        
        return "crudcategorias";
    }
    
    // --- 4. ACTUALIZAR CATEGORÍA ---
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute CategoriaRequestDto categoriaRequestDto,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        
        try {
            // El ID debe venir en el DTO
            if (categoriaRequestDto.getId() == null) {
                throw new IllegalArgumentException("El ID de la categoría es obligatorio");
            }
            
            categoriaService.actualizar(categoriaRequestDto.getId(), categoriaRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Categoría actualizada exitosamente");
        } catch (Exception e) {
            log.error("Error al actualizar categoría: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/categorias";
    }
    
    // --- 5. ELIMINAR CATEGORÍA ---
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        
        try {
            categoriaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Categoría eliminada exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar categoría {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/categorias";
    }
}