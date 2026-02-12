import { TestBed } from "@angular/core/testing";
import { provideHttpClient } from "@angular/common/http";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { CuentasService } from "./cuentas.service";
import { API_BASE_URL } from "../../core/api/api.config";

describe("CuentasService", () => {
  let svc: CuentasService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CuentasService, provideHttpClient(), provideHttpClientTesting()],
    });

    svc = TestBed.inject(CuentasService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it("list() debe llamar GET /api/cuentas", () => {
    svc.list().subscribe();
    const req = httpMock.expectOne(`${API_BASE_URL}/cuentas`);
    expect(req.request.method).toBe("GET");
    req.flush([]);
  });

  it("remove(numero) debe llamar DELETE /api/cuentas/:numero", () => {
    svc.remove("478758").subscribe();
    const req = httpMock.expectOne(`${API_BASE_URL}/cuentas/478758`);
    expect(req.request.method).toBe("DELETE");
    req.flush(null);
  });
});
