package com.nttdata.challenge.api.domain.movimiento;

import com.nttdata.challenge.api.domain.cuenta.Cuenta;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "movimientos")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate fecha;

    @NotBlank
    @Column(nullable = false)
    private String tipoMovimiento; // DEPOSITO | RETIRO

    @NotNull
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valor; // + deposito, - retiro

    @NotNull
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal saldo; // saldo luego del movimiento

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "numero_cuenta", nullable = false)
    private Cuenta cuenta;

    public Long getId() { return id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public Cuenta getCuenta() { return cuenta; }
    public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }
}
