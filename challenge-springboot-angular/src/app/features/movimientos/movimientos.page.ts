import { CommonModule } from "@angular/common";
import { Component, computed, inject, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";

import { MovimientosService, Movimiento, TipoMovimiento } from "./movimientos.service";
import { CuentasService, Cuenta } from "../cuentas/cuentas.service";
import { ToastService } from "../../shared/toast/toast.service";

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: "./movimientos.page.html",
  styleUrl: "./movimientos.page.scss",
})
export class MovimientosPage {
  private fb = inject(FormBuilder);
  private api = inject(MovimientosService);
  private cuentasApi = inject(CuentasService);
  private toast = inject(ToastService);

  q = signal("");
  rows = signal<Movimiento[]>([]);
  cuentas = signal<Cuenta[]>([]);

  // filtros
  filtroCuenta = signal<string>("");
  filtroDesde = signal<string>("");
  filtroHasta = signal<string>("");

  // ui
  isOpen = signal(false);
  editingId = signal<number | null>(null);

  tipos: TipoMovimiento[] = ["DEPOSITO", "RETIRO"];

  form = this.fb.nonNullable.group({
    numeroCuenta: ["", [Validators.required]],
    fecha: ["", [Validators.required]], // type="date" => YYYY-MM-DD
    tipoMovimiento: ["" as any, [Validators.required]],
    valor: [0, [Validators.required, Validators.min(0.01)]],
  });

  // búsqueda rápida (sin Object.values)
  filtered = computed(() => {
    const term = this.q().trim().toLowerCase();
    if (!term) return this.rows();

    return this.rows().filter((r) =>
      (
        [
          r.id,
          r.fecha,
          r.tipoMovimiento,
          r.valor,
          r.saldo,
          r.numeroCuenta,
        ] as Array<string | number>
      ).some((v) => String(v).toLowerCase().includes(term))
    );
  });

  constructor() {
    this.loadCuentas();
    this.load();
  }

  touched(name: keyof typeof this.form.controls) {
    const c = this.form.controls[name];
    return (c.touched || c.dirty) && c.invalid;
  }

  loadCuentas() {
    this.cuentasApi.list().subscribe((v) => this.cuentas.set(v));
  }

  load() {
    const numeroCuenta = this.filtroCuenta() || undefined;
    const desde = this.filtroDesde() || undefined;
    const hasta = this.filtroHasta() || undefined;

    this.api.list({ numeroCuenta, desde, hasta }).subscribe((v) => this.rows.set(v));
  }

  applyFilters() {
    this.load();
  }

  clearFilters() {
    this.filtroCuenta.set("");
    this.filtroDesde.set("");
    this.filtroHasta.set("");
    this.load();
  }

  openCreate() {
    this.editingId.set(null);
    this.form.reset({
      numeroCuenta: "",
      fecha: "",
      tipoMovimiento: "" as any,
      valor: 0,
    });
    this.isOpen.set(true);
  }

  openEdit(m: Movimiento) {
    this.editingId.set(m.id);
    this.form.setValue({
      numeroCuenta: m.numeroCuenta,
      fecha: this.onlyDate(m.fecha),
      tipoMovimiento: m.tipoMovimiento as any,
      valor: Math.abs(m.valor),
    });
    this.isOpen.set(true);
  }

  close() {
    this.isOpen.set(false);
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.toast.error("Revisa los campos requeridos");
      return;
    }

    const raw = this.form.getRawValue();

    // normaliza el signo del valor (ayuda al backend)
    const valorAbs = Math.abs(raw.valor);
    const valor = raw.tipoMovimiento === "RETIRO" ? -valorAbs : valorAbs;

    const payload = {
      numeroCuenta: raw.numeroCuenta,
      fecha: raw.fecha,
      tipoMovimiento: raw.tipoMovimiento,
      valor,
    };

    const id = this.editingId();
    const req = id ? this.api.update(id, payload) : this.api.create(payload);

    req.subscribe({
      next: () => {
        this.toast.ok("Movimiento registrado");
        this.close();
        this.load();
      },
      error: (err) => {
        const msg = err?.error?.message ?? "Error al registrar movimiento";
        this.toast.error(msg);
      },
    });

  }

  del(m: Movimiento) {
    if (!confirm(`Eliminar movimiento #${m.id}?`)) return;

    this.api.remove(m.id).subscribe(() => {
      this.toast.ok("Eliminado");
      this.load();
    });
  }

  // si backend devuelve datetime, convierto a YYYY-MM-DD para input date
  private onlyDate(value: string) {
    // "2026-02-09T10:00:00" -> "2026-02-09"
    return value?.length >= 10 ? value.slice(0, 10) : value;
  }
}
