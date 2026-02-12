package com.nttdata.challenge.api.repository.cliente;

import com.nttdata.challenge.api.domain.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
}
