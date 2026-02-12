import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { API_BASE_URL } from "../../core/api/api.config";

export type TipoMovimiento = "DEPOSITO" | "RETIRO";

export interface Movimiento {
  id: number;                // PK
  fecha: string;             // ISO date or datetime string
  tipoMovimiento: TipoMovimiento;
  valor: number;             // el backend valida signo, pero el front ayuda
  saldo: number;             // saldo resultante (devuelto por backend)
  numeroCuenta: string;      // FK
}

export type MovimientoUpsert = Omit<Movimiento, "id" | "saldo"> & { id?: number };

@Injectable({ providedIn: "root" })
export class MovimientosService {
  private http = inject(HttpClient);
  private base = `${API_BASE_URL}/movimientos`;

  list(params?: { numeroCuenta?: string; desde?: string; hasta?: string }) {
    let p = new HttpParams();
    if (params?.numeroCuenta) p = p.set("numeroCuenta", params.numeroCuenta);
    if (params?.desde) p = p.set("desde", params.desde);
    if (params?.hasta) p = p.set("hasta", params.hasta);
    return this.http.get<Movimiento[]>(this.base, { params: p });
  }

  create(body: MovimientoUpsert) {
    return this.http.post<Movimiento>(this.base, body);
  }

  update(id: number, body: MovimientoUpsert) {
    return this.http.put<Movimiento>(`${this.base}/${id}`, body);
  }

  remove(id: number) {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
