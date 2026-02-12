import { Component } from "@angular/core";
import { AsyncPipe, NgIf } from "@angular/common";
import { ToastService } from "./toast.service";

@Component({
  selector: "app-toast",
  standalone: true,
  imports: [AsyncPipe, NgIf],
  template: `
    <div class="toast" *ngIf="(toast.msg$ | async) as t" [attr.data-kind]="t.kind">
      <span>{{ t.text }}</span>
      <button (click)="toast.clear()">✕</button>
    </div>
  `,
  styleUrl: "./toast.component.scss",
})
export class ToastComponent {
  constructor(public toast: ToastService) {}
}
