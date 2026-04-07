package pe.com.andinadistribuidora.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.CategoriaRequestDto;
import pe.com.andinadistribuidora.api.response.CategoriaResponseDto;
import pe.com.andinadistribuidora.service.CategoriaService;

@Slf4j
@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaRestController {

    private final CategoriaService service;
   
    @PostMapping
    public ResponseEntity<CategoriaResponseDto> crear(@Valid @RequestBody CategoriaRequestDto request) {
        log.info("POST /api/categorias nombre='{}'", request.getNombre());
        CategoriaResponseDto saved = service.crear(request);

        return ResponseEntity
                .created(URI.create("/api/categorias/" + saved.getId()))
                .body(saved);
    }

    @GetMapping("/{id}")
    public CategoriaResponseDto obtener(@PathVariable Integer id) {
        log.info("GET /api/categorias/{}", id);
        return service.obtener(id);
    }

    @GetMapping
    public List<CategoriaResponseDto> listar() {
        log.info("GET /api/categorias");
        return service.listar();
    }    

    
    @PutMapping("/{id}")
    public CategoriaResponseDto actualizar(@PathVariable Integer id,
                                           @Valid @RequestBody CategoriaRequestDto request) {
        log.info("PUT /api/categorias/{}", id);
        return service.actualizar(id, request);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/categorias/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}