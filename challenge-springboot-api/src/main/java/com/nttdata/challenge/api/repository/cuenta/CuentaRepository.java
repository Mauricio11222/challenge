package com.nttdata.challenge.api.repository.cuenta;

import com.nttdata.challenge.api.domain.cuenta.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, String> {
    List<Cuenta> findByCliente_ClienteId(String clienteId);
}
