import { Injectable, inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { API_BASE_URL } from "../../core/api/api.config";

export type TipoCuenta = "Ahorros" | "Corriente";

export interface Cuenta {
  numeroCuenta: string; // PK
  tipoCuenta: TipoCuenta;
  saldoInicial: number;
  estado: boolean;
  clienteId: string; // FK
}

@Injectable({ providedIn: "root" })
export class CuentasService {
  private http = inject(HttpClient);
  private base = `${API_BASE_URL}/cuentas`;

  list() {
    return this.http.get<Cuenta[]>(this.base);
  }

  create(body: Cuenta) {
    return this.http.post<Cuenta>(this.base, body);
  }

  update(numeroCuenta: string, body: Cuenta) {
    return this.http.put<Cuenta>(`${this.base}/${numeroCuenta}`, body);
  }

  remove(numeroCuenta: string) {
    return this.http.delete<void>(`${this.base}/${numeroCuenta}`);
  }
}
