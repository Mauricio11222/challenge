package com.nttdata.challenge.api.dto.movimiento;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimientoResponse {
    private Long id;
    private LocalDate fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private String numeroCuenta;

    public MovimientoResponse(Long id, LocalDate fecha, String tipoMovimiento, BigDecimal valor, BigDecimal saldo, String numeroCuenta) {
        this.id = id;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.numeroCuenta = numeroCuenta;
    }

    public Long getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public BigDecimal getValor() { return valor; }
    public BigDecimal getSaldo() { return saldo; }
    public String getNumeroCuenta() { return numeroCuenta; }
}
