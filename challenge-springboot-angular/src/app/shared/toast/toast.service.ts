import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

export type ToastKind = "ok" | "error" | "info";
export interface ToastMsg { kind: ToastKind; text: string; }

@Injectable({ providedIn: "root" })
export class ToastService {
  private _msg = new BehaviorSubject<ToastMsg | null>(null);
  msg$ = this._msg.asObservable();

  ok(text: string) { this._msg.next({ kind: "ok", text }); }
  error(text: string) { this._msg.next({ kind: "error", text }); }
  info(text: string) { this._msg.next({ kind: "info", text }); }
  clear() { this._msg.next(null); }
}
