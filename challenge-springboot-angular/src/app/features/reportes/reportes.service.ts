import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { API_BASE_URL } from "../../core/api/api.config";

export interface ReporteMovimiento {
  fecha: string;
  tipoMovimiento: string;
  valor: number;
  saldo: number;
}

export interface ReporteCuenta {
  numeroCuenta: string;
  tipoCuenta: string;
  saldoActual: number;
  totalDebitos: number;
  totalCreditos: number;
  movimientos: ReporteMovimiento[];
  saldoDisponible: number;
}

export interface ReporteResponse {
  clienteId: string;
  fechaInicio: string;  
  fechaFin: string;  
  cuentas: ReporteCuenta[];
  pdfBase64?: string;
}

@Injectable({ providedIn: "root" })
export class ReportesService {
  private http = inject(HttpClient);
  private base = `${API_BASE_URL}/reportes`;

  estadoCuenta(clienteId: string, desde: string, hasta: string) {
    const params = new HttpParams()
      .set("clienteId", clienteId)
      .set("fechaInicio", desde)  
      .set("fechaFin", hasta);   

    return this.http.get<ReporteResponse>(this.base, { params });
  }
}
