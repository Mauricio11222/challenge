package com.nttdata.challenge.api.dto.reporte;

import java.math.BigDecimal;

public class ReporteCuentaItem {

    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoDisponible;
    private BigDecimal totalCreditos;
    private BigDecimal totalDebitos;

    public ReporteCuentaItem(
            String numeroCuenta,
            String tipoCuenta,
            BigDecimal saldoInicial,
            BigDecimal saldoDisponible,
            BigDecimal totalCreditos,
            BigDecimal totalDebitos
    ) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoDisponible = saldoDisponible;
        this.totalCreditos = totalCreditos;
        this.totalDebitos = totalDebitos;
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTipoCuenta() { return tipoCuenta; }
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
    public BigDecimal getTotalCreditos() { return totalCreditos; }
    public BigDecimal getTotalDebitos() { return totalDebitos; }
}
