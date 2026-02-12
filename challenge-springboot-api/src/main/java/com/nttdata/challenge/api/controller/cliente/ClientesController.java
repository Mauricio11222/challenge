package com.nttdata.challenge.api.controller.cliente;

import com.nttdata.challenge.api.domain.cliente.Cliente;
import com.nttdata.challenge.api.dto.cliente.ClienteRequest;
import com.nttdata.challenge.api.service.cliente.ClientesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClientesController {

    private final ClientesService service;

    public ClientesController(ClientesService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cliente> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Cliente get(@PathVariable String id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<Cliente> create(@Valid @RequestBody ClienteRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable String id, @Valid @RequestBody ClienteRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
