import { Component } from "@angular/core";
import { RouterModule } from "@angular/router";
import { ToastComponent } from "./shared/toast/toast.component";
@Component({
  standalone: true,
  selector: "app-root",
  imports: [RouterModule, ToastComponent],
  templateUrl: "./app.html",
  styleUrl: "./app.scss",
})
export class App {}
