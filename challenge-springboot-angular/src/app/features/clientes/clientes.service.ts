import { Injectable, inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { API_BASE_URL } from "../../core/api/api.config";

export interface Cliente {
  clienteId: string;          // PK / unique
  nombre: string;
  genero: string;
  edad: number;
  identificacion: string;
  direccion: string;
  telefono: string;
  contrasena: string;
  estado: boolean;
}

export type ClienteUpsert = Omit<Cliente, "clienteId"> & { clienteId?: string };

@Injectable({ providedIn: "root" })
export class ClientesService {
  private http = inject(HttpClient);
  private base = `${API_BASE_URL}/clientes`;

  list() {
    return this.http.get<Cliente[]>(this.base);
  }

  create(body: ClienteUpsert) {
    return this.http.post<Cliente>(this.base, body);
  }

  update(id: string, body: ClienteUpsert) {
    return this.http.put<Cliente>(`${this.base}/${id}`, body);
  }

  remove(id: string) {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
