import { CommonModule } from "@angular/common";
import { Component, computed, inject, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";

import { CuentasService, Cuenta, TipoCuenta } from "./cuentas.service";
import { ClientesService, Cliente } from "../clientes/clientes.service";
import { ToastService } from "../../shared/toast/toast.service";

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: "./cuentas.page.html",
  styleUrl: "./cuentas.page.scss",
})
export class CuentasPage {
  private fb = inject(FormBuilder);
  private api = inject(CuentasService);
  private clientesApi = inject(ClientesService);
  private toast = inject(ToastService);

  // data
  q = signal("");
  rows = signal<Cuenta[]>([]);
  clientes = signal<Cliente[]>([]);

  // ui
  isOpen = signal(false);
  editingId = signal<string | null>(null); // numeroCuenta

  tipos: TipoCuenta[] = ["Ahorros", "Corriente"];

  form = this.fb.nonNullable.group({
    numeroCuenta: ["", [Validators.required, Validators.minLength(3)]],
    tipoCuenta: ["" as any, [Validators.required]],
    saldoInicial: [0, [Validators.required, Validators.min(0)]],
    estado: [true, [Validators.required]],
    clienteId: ["", [Validators.required]],
  });

  // búsqueda rápida SIN Object.values 
  filtered = computed(() => {
    const term = this.q().trim().toLowerCase();
    if (!term) return this.rows();

    return this.rows().filter((r) =>
      (
        [
          r.numeroCuenta,
          r.tipoCuenta,
          r.saldoInicial,
          r.estado,
          r.clienteId,
        ] as Array<string | number | boolean>
      ).some((v) => String(v).toLowerCase().includes(term))
    );
  });

  // map para mostrar nombre del cliente
  clienteNameById = computed(() => {
    const map = new Map<string, string>();
    for (const c of this.clientes()) map.set(c.clienteId, c.nombre);
    return map;
  });

  constructor() {
    this.loadClientes();
    this.load();
  }

  touched(name: keyof typeof this.form.controls) {
    const c = this.form.controls[name];
    return (c.touched || c.dirty) && c.invalid;
  }

  load() {
    this.api.list().subscribe((v) => this.rows.set(v));
  }

  loadClientes() {
    this.clientesApi.list().subscribe((v) => this.clientes.set(v));
  }

  openCreate() {
    this.editingId.set(null);
    this.form.reset({
      numeroCuenta: "",
      tipoCuenta: "" as any,
      saldoInicial: 0,
      estado: true,
      clienteId: "",
    });
    this.isOpen.set(true);
  }

  openEdit(c: Cuenta) {
    this.editingId.set(c.numeroCuenta);
    this.form.setValue({
      numeroCuenta: c.numeroCuenta,
      tipoCuenta: c.tipoCuenta as any,
      saldoInicial: c.saldoInicial,
      estado: c.estado,
      clienteId: c.clienteId,
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
    const id = this.editingId();

    // si edita, no cambies numeroCuenta
    if (id) raw.numeroCuenta = id;

    const req = id ? this.api.update(id, raw as Cuenta) : this.api.create(raw as Cuenta);

    req.subscribe(() => {
      this.toast.ok("Guardado");
      this.close();
      this.load();
    });
  }

  del(c: Cuenta) {
    if (!confirm(`Eliminar cuenta ${c.numeroCuenta}?`)) return;

    this.api.remove(c.numeroCuenta).subscribe(() => {
      this.toast.ok("Eliminado");
      this.load();
    });
  }

  clienteLabel(clienteId: string) {
    return this.clienteNameById().get(clienteId) ?? clienteId;
  }
}
