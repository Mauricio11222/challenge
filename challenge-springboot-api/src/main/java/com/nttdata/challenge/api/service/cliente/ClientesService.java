package com.nttdata.challenge.api.service.cliente;

import com.nttdata.challenge.api.domain.cliente.Cliente;
import com.nttdata.challenge.api.dto.cliente.ClienteRequest;
import com.nttdata.challenge.api.exception.ApiException;
import com.nttdata.challenge.api.repository.cliente.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientesService {

    private final ClienteRepository repo;

    public ClientesService(ClienteRepository repo) {
        this.repo = repo;
    }

    public List<Cliente> list() {
        return repo.findAll();
    }

    public Cliente get(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    @Transactional
    public Cliente create(ClienteRequest req) {
        if (repo.existsById(req.getClienteId())) {
            throw new ApiException(HttpStatus.CONFLICT, "Cliente ya existe");
        }

        Cliente c = new Cliente();
        c.setClienteId(req.getClienteId());
        c.setNombre(req.getNombre());
        c.setGenero(req.getGenero());
        c.setEdad(req.getEdad());
        c.setIdentificacion(req.getIdentificacion());
        c.setDireccion(req.getDireccion());
        c.setTelefono(req.getTelefono());
        c.setContrasena(req.getContrasena());
        c.setEstado(req.getEstado());

        return repo.save(c);
    }

    @Transactional
    public Cliente update(String id, ClienteRequest req) {
        Cliente c = get(id);

        c.setNombre(req.getNombre());
        c.setGenero(req.getGenero());
        c.setEdad(req.getEdad());
        c.setIdentificacion(req.getIdentificacion());
        c.setDireccion(req.getDireccion());
        c.setTelefono(req.getTelefono());
        c.setContrasena(req.getContrasena());
        c.setEstado(req.getEstado());

        return repo.save(c);
    }

    @Transactional
    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        repo.deleteById(id);
    }
}
