import { CommonModule } from "@angular/common";
import { Component, computed, inject, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";

import { ReportesService, ReporteResponse } from "./reportes.service";
import { ClientesService, Cliente } from "../clientes/clientes.service";
import { ToastService } from "../../shared/toast/toast.service";
import { base64ToBlob } from "../../core/utils/base64-to-blob";

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: "./reportes.page.html",
  styleUrl: "./reportes.page.scss",
})
export class ReportesPage {
  private fb = inject(FormBuilder);
  private api = inject(ReportesService);
  private clientesApi = inject(ClientesService);
  private toast = inject(ToastService);

  clientes = signal<Cliente[]>([]);
  data = signal<ReporteResponse | null>(null);

  form = this.fb.nonNullable.group({
    clienteId: ["", Validators.required],
    desde: ["", Validators.required],
    hasta: ["", Validators.required],
  });

  // suma de todas las cuentas
  totalDebitos = computed(() => {
    const r = this.data();
    if (!r) return 0;
    return r.cuentas.reduce((acc, c) => acc + (c.totalDebitos || 0), 0);
  });

  totalCreditos = computed(() => {
    const r = this.data();
    if (!r) return 0;
    return r.cuentas.reduce((acc, c) => acc + (c.totalCreditos || 0), 0);
  });

  constructor() {
    this.loadClientes();
  }

  loadClientes() {
    this.clientesApi.list().subscribe((v) => this.clientes.set(v));
  }

  touched(name: keyof typeof this.form.controls) {
    const c = this.form.controls[name];
    return (c.touched || c.dirty) && c.invalid;
  }

  run() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.toast.error("Completa cliente y rango de fechas");
      return;
    }

    const v = this.form.getRawValue();
    this.api.estadoCuenta(v.clienteId, v.desde, v.hasta).subscribe((r) => {
      this.data.set(r);
      this.toast.ok("Reporte generado");
    });
  }

  downloadPdf() {
    const b64 = this.data()?.pdfBase64;
    if (!b64) {
      this.toast.error("El reporte no incluye PDF");
      return;
    }

    const blob = base64ToBlob(b64, "application/pdf");
    const url = URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = url;
    a.download = `estado-cuenta-${this.data()?.clienteId ?? "cliente"}.pdf`;
    a.click();

    URL.revokeObjectURL(url);
  }

  clear() {
    this.data.set(null);
  }
}
