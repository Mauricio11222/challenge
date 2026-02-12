package com.nttdata.challenge.api.service.movimiento;

import com.nttdata.challenge.api.domain.cuenta.Cuenta;
import com.nttdata.challenge.api.domain.movimiento.Movimiento;
import com.nttdata.challenge.api.exception.ApiException;
import com.nttdata.challenge.api.repository.cuenta.CuentaRepository;
import com.nttdata.challenge.api.repository.movimiento.MovimientoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MovimientosService {

    private final MovimientoRepository movimientoRepo;
    private final CuentaRepository cuentaRepo;

    public MovimientosService(MovimientoRepository movimientoRepo, CuentaRepository cuentaRepo) {
        this.movimientoRepo = movimientoRepo;
        this.cuentaRepo = cuentaRepo;
    }

    public List<Movimiento> list() {
        return movimientoRepo.findAll();
    }

    public Movimiento get(Long id) {
        return movimientoRepo.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Movimiento no encontrado"));
    }

    @Transactional
    public Movimiento crearMovimiento(String numeroCuenta, LocalDate fecha, String tipoMovimiento, BigDecimal valor) {
        Cuenta cuenta = cuentaRepo.findById(numeroCuenta)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));

        // saldo anterior: último saldo o saldo inicial
        BigDecimal ultimoSaldo = movimientoRepo.lastSaldo(numeroCuenta);
        BigDecimal saldoAnterior = (ultimoSaldo != null) ? ultimoSaldo : cuenta.getSaldoInicial();
        if (valor.compareTo(BigDecimal.ZERO) < 0) {

            BigDecimal montoDebito = valor.abs();

            // saldo cero
            if (saldoAnterior.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Saldo no disponible");
            }

            // saldo menor al monto a debitar
            if (saldoAnterior.compareTo(montoDebito) < 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Saldo no disponible");
            }
        }

        BigDecimal saldoNuevo = saldoAnterior.add(valor);

        Movimiento m = new Movimiento();
        m.setCuenta(cuenta);
        m.setFecha(fecha != null ? fecha : LocalDate.now());
        m.setTipoMovimiento(tipoMovimiento);
        m.setValor(valor);
        m.setSaldo(saldoNuevo);

        return movimientoRepo.save(m);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!movimientoRepo.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Movimiento no encontrado");
        }
        movimientoRepo.deleteById(id);
    }
}
