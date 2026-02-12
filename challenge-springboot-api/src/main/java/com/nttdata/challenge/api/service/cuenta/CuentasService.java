package com.nttdata.challenge.api.service.cuenta;

import com.nttdata.challenge.api.domain.cliente.Cliente;
import com.nttdata.challenge.api.domain.cuenta.Cuenta;
import com.nttdata.challenge.api.dto.cuenta.CuentaRequest;
import com.nttdata.challenge.api.exception.ApiException;
import com.nttdata.challenge.api.repository.cliente.ClienteRepository;
import com.nttdata.challenge.api.repository.cuenta.CuentaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CuentasService {

    private final CuentaRepository cuentaRepo;
    private final ClienteRepository clienteRepo;

    public CuentasService(CuentaRepository cuentaRepo, ClienteRepository clienteRepo) {
        this.cuentaRepo = cuentaRepo;
        this.clienteRepo = clienteRepo;
    }

    public List<Cuenta> list() {
        return cuentaRepo.findAll();
    }

    public Cuenta get(String numero) {
        return cuentaRepo.findById(numero)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));
    }

    @Transactional
    public Cuenta create(CuentaRequest req) {
        if (cuentaRepo.existsById(req.getNumeroCuenta())) {
            throw new ApiException(HttpStatus.CONFLICT, "Cuenta ya existe");
        }

        Cliente cliente = clienteRepo.findById(req.getClienteId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        Cuenta c = new Cuenta();
        c.setNumeroCuenta(req.getNumeroCuenta());
        c.setTipoCuenta(req.getTipoCuenta());
        c.setSaldoInicial(req.getSaldoInicial());
        c.setEstado(req.getEstado());
        c.setCliente(cliente);

        return cuentaRepo.save(c);
    }

    @Transactional
    public Cuenta update(String numero, CuentaRequest req) {
        Cuenta c = get(numero);

        Cliente cliente = clienteRepo.findById(req.getClienteId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        c.setTipoCuenta(req.getTipoCuenta());
        c.setSaldoInicial(req.getSaldoInicial());
        c.setEstado(req.getEstado());
        c.setCliente(cliente);

        return cuentaRepo.save(c);
    }

    @Transactional
    public void delete(String numero) {
        if (!cuentaRepo.existsById(numero)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Cuenta no encontrada");
        }
        cuentaRepo.deleteById(numero);
    }
}
