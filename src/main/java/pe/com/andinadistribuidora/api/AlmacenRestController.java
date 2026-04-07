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
import pe.com.andinadistribuidora.api.request.AlmacenRequestDto;
import pe.com.andinadistribuidora.api.response.AlmacenResponseDto;
import pe.com.andinadistribuidora.service.AlmacenService;

@Slf4j
@RestController
@RequestMapping("/api/almacenes")
@RequiredArgsConstructor
public class AlmacenRestController {
    
    private final AlmacenService service;
        
    @PostMapping
    public ResponseEntity<AlmacenResponseDto> crear(@Valid @RequestBody AlmacenRequestDto request) {
        log.info("POST /api/almacenes nombre='{}'", request.getNombre());
        AlmacenResponseDto saved = service.crear(request);
        
        return ResponseEntity
                .created(URI.create("/api/almacenes/" + saved.getId()))
                .body(saved);
    }

    @GetMapping("/{id}")
    public AlmacenResponseDto obtener(@PathVariable Integer id) {
        log.info("GET /api/almacenes/{}", id);
        return service.obtener(id);
    }

    @GetMapping
    public List<AlmacenResponseDto> listar() {
        log.info("GET /api/almacenes");
        return service.listar();
    }    
 
    @GetMapping("/activos")
    public List<AlmacenResponseDto> listarActivos() {
        log.info("GET /api/almacenes/activos");
        return service.listarActivos();
    }    
   
    @PutMapping("/{id}")
    public AlmacenResponseDto actualizar(@PathVariable Integer id,
                                         @Valid @RequestBody AlmacenRequestDto request) {
        log.info("PUT /api/almacenes/{}", id);
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/almacenes/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}