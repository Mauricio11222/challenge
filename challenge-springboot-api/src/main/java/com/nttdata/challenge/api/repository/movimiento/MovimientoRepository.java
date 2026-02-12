package com.nttdata.challenge.api.repository.movimiento;

import com.nttdata.challenge.api.domain.movimiento.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    @Query("""
        select coalesce(max(m.saldo), null)
        from Movimiento m
        where m.cuenta.numeroCuenta = :numeroCuenta
    """)
    BigDecimal lastSaldo(String numeroCuenta);

    @Query("""
        select m from Movimiento m
        where m.cuenta.numeroCuenta = :numeroCuenta
          and m.fecha between :desde and :hasta
        order by m.fecha asc, m.id asc
    """)
    List<Movimiento> findByCuentaAndRango(String numeroCuenta, LocalDate desde, LocalDate hasta);

    @Query("""
        select m from Movimiento m
        where m.cuenta.cliente.clienteId = :clienteId
          and m.fecha between :desde and :hasta
        order by m.cuenta.numeroCuenta asc, m.fecha asc, m.id asc
    """)
    List<Movimiento> findByClienteAndRango(String clienteId, LocalDate desde, LocalDate hasta);

    List<Movimiento> findByCuenta_NumeroCuentaAndFechaBetween(String numeroCuenta, LocalDate inicio, LocalDate fin);

    Optional<Movimiento> findTopByCuenta_NumeroCuentaAndFechaLessThanEqualOrderByFechaDescIdDesc(String numeroCuenta, LocalDate fin);
}
