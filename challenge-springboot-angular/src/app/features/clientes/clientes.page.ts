import { CommonModule } from "@angular/common";
import { Component, computed, inject, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";

import { ClientesService, Cliente } from "./clientes.service";
import { ToastService } from "../../shared/toast/toast.service";

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: "./clientes.page.html",
  styleUrl: "./clientes.page.scss",
})
export class ClientesPage {
  private fb = inject(FormBuilder);
  private api = inject(ClientesService);
  private toast = inject(ToastService);

  q = signal("");
  rows = signal<Cliente[]>([]);
  isOpen = signal(false);
  editingId = signal<string | null>(null);

  // Form 
  form = this.fb.nonNullable.group({
    clienteId: ["", [Validators.required, Validators.minLength(2)]],
    nombre: ["", [Validators.required, Validators.minLength(3)]],
    genero: ["", [Validators.required]],
    edad: [0, [Validators.required, Validators.min(0)]],
    identificacion: ["", [Validators.required]],
    direccion: ["", [Validators.required]],
    telefono: ["", [Validators.required]],
    contrasena: ["", [Validators.required, Validators.minLength(4)]],
    estado: [true, [Validators.required]],
  });

  // Tabla filtrada 
  filtered = computed(() => {
    const term = this.q().trim().toLowerCase();
    if (!term) return this.rows();
    return this.rows().filter((r) =>
      Object.values(r).some((v) => String(v).toLowerCase().includes(term))
    );
  });

  constructor() {
    this.load();
  }

  // Helpers
  touched(name: keyof typeof this.form.controls) {
    const c = this.form.controls[name];
    return (c.touched || c.dirty) && c.invalid;
  }

  // API
  load() {
    this.api.list().subscribe((v) => this.rows.set(v));
  }

  // Modal actions
  openCreate() {
    this.editingId.set(null);
    this.form.reset({
      clienteId: "",
      nombre: "",
      genero: "",
      edad: 0,
      identificacion: "",
      direccion: "",
      telefono: "",
      contrasena: "",
      estado: true,
    });
    this.isOpen.set(true);
  }

  openEdit(c: Cliente) {
    this.editingId.set(c.clienteId);
    this.form.setValue({
      clienteId: c.clienteId,
      nombre: c.nombre,
      genero: c.genero,
      edad: c.edad,
      identificacion: c.identificacion,
      direccion: c.direccion,
      telefono: c.telefono,
      contrasena: c.contrasena,
      estado: c.estado,
    });
    this.isOpen.set(true);
  }

  close() {
    this.isOpen.set(false);
  }

save() {
  console.log(" save() ejecutado", this.form.value);

  if (this.form.invalid) {
    this.form.markAllAsTouched();
    this.toast.error("Revisa los campos requeridos");
    return;
  }

  const raw = this.form.getRawValue();
  const id = this.editingId();

  if (id) raw.clienteId = id;

  const req = id ? this.api.update(id, raw) : this.api.create(raw);

  req.subscribe({
    next: () => {
      this.toast.ok(id ? "Cliente actualizado correctamente" : "Cliente creado correctamente");
      this.close();
      this.load();
    },
    error: (err) => {
      console.error("Error al guardar cliente:", err);
      this.toast.error("Error al guardar el cliente");
    },
  });
}


  del(c: Cliente) {
    if (!confirm(`Eliminar cliente "${c.nombre}"?`)) return;

    this.api.remove(c.clienteId).subscribe(() => {
      this.toast.ok("Eliminado");
      this.load();
    });
  }
}
