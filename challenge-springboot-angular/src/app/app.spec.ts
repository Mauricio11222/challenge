import { TestBed } from "@angular/core/testing";
import { provideRouter } from "@angular/router";
import { routes } from "./app.routes";
import { App } from "./app";

describe("App", () => {
  it("should create the app", async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [provideRouter(routes)],
    }).compileComponents();

    const fixture = TestBed.createComponent(App);
    expect(fixture.componentInstance).toBeTruthy();
  });
});
