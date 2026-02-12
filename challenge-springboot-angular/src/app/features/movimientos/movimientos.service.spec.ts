import { TestBed } from "@angular/core/testing";
import { provideHttpClient } from "@angular/common/http";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { MovimientosService } from "./movimientos.service";
import { API_BASE_URL } from "../../core/api/api.config";

describe("MovimientosService", () => {
  let svc: MovimientosService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MovimientosService, provideHttpClient(), provideHttpClientTesting()],
    });

    svc = TestBed.inject(MovimientosService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it("list() debe llamar GET /api/movimientos", () => {
    svc.list().subscribe();
    const req = httpMock.expectOne(`${API_BASE_URL}/movimientos`);
    expect(req.request.method).toBe("GET");
    req.flush([]);
  });

  it("remove(id) debe llamar DELETE /api/movimientos/:id", () => {
    svc.remove(10).subscribe();
    const req = httpMock.expectOne(`${API_BASE_URL}/movimientos/10`);
    expect(req.request.method).toBe("DELETE");
    req.flush(null);
  });
});
