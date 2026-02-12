package com.nttdata.challenge.api.controller.reporte;

import com.nttdata.challenge.api.dto.reporte.ReporteEstadoCuentaResponse;
import com.nttdata.challenge.api.service.reporte.ReportesService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ReportesService service;
    private final DateTimeFormatter ISO = DateTimeFormatter.ISO_DATE;

    public ReportesController(ReportesService service) {
        this.service = service;
    }

    @GetMapping
    public ReporteEstadoCuentaResponse estadoCuenta(
            @RequestParam String clienteId,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin
    ) {
        LocalDate inicio = parseDateOrBadRequest(fechaInicio, "fechaInicio");
        LocalDate fin = parseDateOrBadRequest(fechaFin, "fechaFin");

        if (inicio.isAfter(fin)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fechaInicio no puede ser posterior a fechaFin");
        }

        return service.estadoCuenta(clienteId.trim(), inicio, fin);
    }

    private LocalDate parseDateOrBadRequest(String raw, String paramName) {
        if (raw == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, paramName + " es requerido");
        }
        String s = raw.trim(); // elimina whitespace, retornos de línea, tabs, etc.
        try {
            return LocalDate.parse(s, ISO);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    paramName + " inválida (esperado YYYY-MM-DD): " + s);
        }
    }
}
