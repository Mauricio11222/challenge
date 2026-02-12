package com.nttdata.challenge.api.controller.movimiento;

import com.nttdata.challenge.api.domain.movimiento.Movimiento;
import com.nttdata.challenge.api.dto.movimiento.MovimientoCreateRequest;
import com.nttdata.challenge.api.dto.movimiento.MovimientoResponse;
import com.nttdata.challenge.api.service.movimiento.MovimientosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientosController {

    private final MovimientosService service;

    public MovimientosController(MovimientosService service) {
        this.service = service;
    }

    private static MovimientoResponse toResponse(Movimiento m) {
        return new MovimientoResponse(
                m.getId(),
                m.getFecha(),
                m.getTipoMovimiento(),
                m.getValor(),
                m.getSaldo(),
                m.getCuenta().getNumeroCuenta()
        );
    }

    @GetMapping
    public List<MovimientoResponse> list() {
        return service.list()
                .stream()
                .map(MovimientosController::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public MovimientoResponse get(@PathVariable Long id) {
        return toResponse(service.get(id));
    }

    @PostMapping
    public ResponseEntity<MovimientoResponse> create(
            @Valid @RequestBody MovimientoCreateRequest req
    ) {
        Movimiento created = service.crearMovimiento(
                req.getNumeroCuenta(),
                req.getFecha(),
                req.getTipoMovimiento(),
                req.getValor()
        );
        return ResponseEntity.ok(toResponse(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
