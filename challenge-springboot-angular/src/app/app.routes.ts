import { Routes } from "@angular/router";
import { ClientesPage } from "./features/clientes/clientes.page";
import { CuentasPage } from "./features/cuentas/cuentas.page";
import { MovimientosPage } from "./features/movimientos/movimientos.page";
import { ReportesPage } from "./features/reportes/reportes.page";

export const routes: Routes = [
  { path: "", pathMatch: "full", redirectTo: "clientes" },
  { path: "clientes", component: ClientesPage },
  { path: "cuentas", component: CuentasPage },
  { path: "movimientos", component: MovimientosPage },
  { path: "reportes", component: ReportesPage },
  { path: "**", redirectTo: "clientes" },
];
