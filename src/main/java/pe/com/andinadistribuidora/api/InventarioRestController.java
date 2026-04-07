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
import pe.com.andinadistribuidora.api.request.InventarioRequestDto;
import pe.com.andinadistribuidora.api.response.InventarioResponseDto;
import pe.com.andinadistribuidora.service.InventarioService;

@Slf4j
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioRestController {

    private final InventarioService service;

    @PostMapping
    public ResponseEntity<InventarioResponseDto> crear(@Valid @RequestBody InventarioRequestDto request) {
        log.info("POST /api/inventario almacen={}, producto={}", request.getAlmacenId(), request.getProductoId());
        InventarioResponseDto saved = service.crear(request);

        return ResponseEntity
                .created(URI.create("/api/inventario/" + saved.getAlmacenId() + "/" + saved.getProductoId()))
                .body(saved);
    }

    @GetMapping("/{almacenId}/{productoId}")
    public InventarioResponseDto obtener(@PathVariable Integer almacenId, 
                                         @PathVariable Integer productoId) {
        log.info("GET /api/inventario/{}/{}", almacenId, productoId);
        return service.obtener(almacenId, productoId);
    }

    @GetMapping
    public List<InventarioResponseDto> listar() {
        log.info("GET /api/inventario");
        return service.listar();
    }

    @PutMapping("/{almacenId}/{productoId}")
    public InventarioResponseDto actualizar(@PathVariable Integer almacenId,
                                            @PathVariable Integer productoId,
                                            @Valid @RequestBody InventarioRequestDto request) {
        log.info("PUT /api/inventario/{}/{}", almacenId, productoId);
        return service.actualizar(almacenId, productoId, request);
    }

    @DeleteMapping("/{almacenId}/{productoId}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer almacenId,
                                        @PathVariable Integer productoId) {
        log.info("DELETE /api/inventario/{}/{}", almacenId, productoId);
        service.eliminar(almacenId, productoId);
        return ResponseEntity.noContent().build();
    }
}