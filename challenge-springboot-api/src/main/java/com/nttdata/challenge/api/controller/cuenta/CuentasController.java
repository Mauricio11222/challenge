package com.nttdata.challenge.api.controller.cuenta;

import com.nttdata.challenge.api.domain.cuenta.Cuenta;
import com.nttdata.challenge.api.dto.cuenta.CuentaRequest;
import com.nttdata.challenge.api.dto.cuenta.CuentaResponse;
import com.nttdata.challenge.api.service.cuenta.CuentasService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentasController {

    private final CuentasService service;

    public CuentasController(CuentasService service) {
        this.service = service;
    }

    private static CuentaResponse toResponse(Cuenta c) {
        return new CuentaResponse(
                c.getNumeroCuenta(),
                c.getTipoCuenta(),
                c.getSaldoInicial(),
                c.getEstado(),
                c.getCliente().getClienteId()
        );
    }

    @GetMapping
    public List<CuentaResponse> list() {
        return service.list().stream().map(CuentasController::toResponse).toList();
    }

    @GetMapping("/{numero}")
    public CuentaResponse get(@PathVariable String numero) {
        return toResponse(service.get(numero));
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> create(@Valid @RequestBody CuentaRequest req) {
        return ResponseEntity.ok(toResponse(service.create(req)));
    }

    @PutMapping("/{numero}")
    public ResponseEntity<CuentaResponse> update(@PathVariable String numero, @Valid @RequestBody CuentaRequest req) {
        return ResponseEntity.ok(toResponse(service.update(numero, req)));
    }

    @DeleteMapping("/{numero}")
    public ResponseEntity<Void> delete(@PathVariable String numero) {
        service.delete(numero);
        return ResponseEntity.noContent().build();
    }
}
