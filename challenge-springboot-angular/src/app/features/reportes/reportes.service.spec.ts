import { TestBed } from "@angular/core/testing";
import { provideHttpClient } from "@angular/common/http";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { ReportesService } from "./reportes.service";
import { API_BASE_URL } from "../../core/api/api.config";

describe("ReportesService", () => {
  let svc: ReportesService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReportesService, provideHttpClient(), provideHttpClientTesting()],
    });

    svc = TestBed.inject(ReportesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it("estadoCuenta() debe llamar GET /api/reportes con query params", () => {
    svc.estadoCuenta("C001", "2026-02-01", "2026-02-09").subscribe();

    const req = httpMock.expectOne((r) => {
      return (
        r.url === `${API_BASE_URL}/reportes` &&
        r.params.get("clienteId") === "C001" &&
        r.params.get("desde") === "2026-02-01" &&
        r.params.get("hasta") === "2026-02-09"
      );
    });

    expect(req.request.method).toBe("GET");
    req.flush({ clienteId: "C001", desde: "2026-02-01", hasta: "2026-02-09", cuentas: [] });
  });
});
