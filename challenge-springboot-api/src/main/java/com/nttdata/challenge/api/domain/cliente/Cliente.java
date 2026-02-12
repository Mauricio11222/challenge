package com.nttdata.challenge.api.domain.cliente;

import com.nttdata.challenge.api.domain.persona.Persona;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "clientes")
public class Cliente extends Persona {

    @Id
    @Column(name = "cliente_id", nullable = false, unique = true, length = 50)
    private String clienteId;

    @NotBlank
    @Column(nullable = false)
    private String contrasena;

    @NotNull
    @Column(nullable = false)
    private Boolean estado;

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
