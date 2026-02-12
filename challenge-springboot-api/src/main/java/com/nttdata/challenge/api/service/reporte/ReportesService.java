package com.nttdata.challenge.api.service.reporte;

import com.nttdata.challenge.api.domain.cliente.Cliente;
import com.nttdata.challenge.api.domain.cuenta.Cuenta;
import com.nttdata.challenge.api.domain.movimiento.Movimiento;
import com.nttdata.challenge.api.dto.reporte.ReporteCuentaItem;
import com.nttdata.challenge.api.dto.reporte.ReporteEstadoCuentaResponse;
import com.nttdata.challenge.api.exception.ApiException;
import com.nttdata.challenge.api.repository.cliente.ClienteRepository;
import com.nttdata.challenge.api.repository.cuenta.CuentaRepository;
import com.nttdata.challenge.api.repository.movimiento.MovimientoRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Service
public class ReportesService {

    private final ClienteRepository clienteRepo;
    private final CuentaRepository cuentaRepo;
    private final MovimientoRepository movRepo;

    public ReportesService(ClienteRepository clienteRepo, CuentaRepository cuentaRepo, MovimientoRepository movRepo) {
        this.clienteRepo = clienteRepo;
        this.cuentaRepo = cuentaRepo;
        this.movRepo = movRepo;
    }

    public ReporteEstadoCuentaResponse estadoCuenta(String clienteId, LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null || inicio.isAfter(fin)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Rango de fechas inválido");
        }

        Cliente cliente = clienteRepo.findById(clienteId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        List<Cuenta> cuentas = cuentaRepo.findByCliente_ClienteId(clienteId);

        List<ReporteCuentaItem> items = cuentas.stream().map(cuenta -> {
            List<Movimiento> movs = movRepo.findByCuenta_NumeroCuentaAndFechaBetween(
                    cuenta.getNumeroCuenta(), inicio, fin
            );

            BigDecimal totalCreditos = movs.stream()
                    .map(Movimiento::getValor)
                    .filter(v -> v.compareTo(BigDecimal.ZERO) > 0)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalDebitos = movs.stream()
                    .map(Movimiento::getValor)
                    .filter(v -> v.compareTo(BigDecimal.ZERO) < 0)
                    .map(BigDecimal::abs)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal saldoDisponible = movRepo
                    .findTopByCuenta_NumeroCuentaAndFechaLessThanEqualOrderByFechaDescIdDesc(cuenta.getNumeroCuenta(), fin)
                    .map(Movimiento::getSaldo)
                    .orElse(cuenta.getSaldoInicial());

            return new ReporteCuentaItem(
                    cuenta.getNumeroCuenta(),
                    cuenta.getTipoCuenta(),
                    cuenta.getSaldoInicial(),
                    saldoDisponible,
                    totalCreditos,
                    totalDebitos
            );
        }).toList();

        byte[] pdfBytes = generarPdf(cliente, inicio, fin, items);
        String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);

        return new ReporteEstadoCuentaResponse(
                clienteId,
                cliente.getNombre(),
                inicio.toString(),
                fin.toString(),
                items,
                pdfBase64
        );
    }

    private byte[] generarPdf(Cliente cliente, LocalDate inicio, LocalDate fin, List<ReporteCuentaItem> items) {
        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float y = 740;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
                cs.newLineAtOffset(50, y);
                cs.showText("Estado de Cuenta");
                cs.endText();

                y -= 25;
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.newLineAtOffset(50, y);
                cs.showText("Cliente: " + cliente.getNombre() + " (" + cliente.getClienteId() + ")");
                cs.endText();

                y -= 15;
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.newLineAtOffset(50, y);
                cs.showText("Rango: " + inicio + " a " + fin);
                cs.endText();

                y -= 25;
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(50, y);
                cs.showText("Cuenta | Tipo | Saldo Inicial | Saldo Disponible | Total Creditos | Total Debitos");
                cs.endText();

                y -= 12;

                cs.setFont(PDType1Font.HELVETICA, 10);
                for (ReporteCuentaItem it : items) {
                    if (y < 60) break; // simple (1 página). Suficiente para el challenge.

                    String line = it.getNumeroCuenta() + " | " + it.getTipoCuenta()
                            + " | " + it.getSaldoInicial()
                            + " | " + it.getSaldoDisponible()
                            + " | " + it.getTotalCreditos()
                            + " | " + it.getTotalDebitos();

                    cs.beginText();
                    cs.newLineAtOffset(50, y);
                    cs.showText(line);
                    cs.endText();
                    y -= 12;
                }
            }

            doc.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generando PDF del reporte");
        }
    }
}
