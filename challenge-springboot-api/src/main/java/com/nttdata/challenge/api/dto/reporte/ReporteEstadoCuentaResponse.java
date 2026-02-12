package com.nttdata.challenge.api.dto.reporte;

import java.util.List;

public class ReporteEstadoCuentaResponse {

    private String clienteId;
    private String cliente;
    private String fechaInicio;
    private String fechaFin;
    private List<ReporteCuentaItem> cuentas;
    private String pdfBase64;

    public ReporteEstadoCuentaResponse(
            String clienteId,
            String cliente,
            String fechaInicio,
            String fechaFin,
            List<ReporteCuentaItem> cuentas,
            String pdfBase64
    ) {
        this.clienteId = clienteId;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cuentas = cuentas;
        this.pdfBase64 = pdfBase64;
    }

    public String getClienteId() { return clienteId; }
    public String getCliente() { return cliente; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public List<ReporteCuentaItem> getCuentas() { return cuentas; }
    public String getPdfBase64() { return pdfBase64; }
}
