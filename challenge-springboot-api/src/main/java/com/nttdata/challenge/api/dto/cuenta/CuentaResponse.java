package com.nttdata.challenge.api.dto.cuenta;

import java.math.BigDecimal;

public class CuentaResponse {
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private Boolean estado;
    private String clienteId;

    public CuentaResponse(String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial, Boolean estado, String clienteId) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.clienteId = clienteId;
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTipoCuenta() { return tipoCuenta; }
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public Boolean getEstado() { return estado; }
    public String getClienteId() { return clienteId; }
}
