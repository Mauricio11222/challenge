import { TestBed } from "@angular/core/testing";
import { provideHttpClient } from "@angular/common/http";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { ClientesService } from "./clientes.service";
import { API_BASE_URL } from "../../core/api/api.config";

describe("ClientesService", () => {
  let svc: ClientesService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ClientesService, provideHttpClient(), provideHttpClientTesting()],
    });

    svc = TestBed.inject(ClientesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it("list() debe llamar GET /api/clientes", () => {
    svc.list().subscribe();
    const req = httpMock.expectOne(`${API_BASE_URL}/clientes`);
    expect(req.request.method).toBe("GET");
    req.flush([]);
  });

  it("remove(id) debe llamar DELETE /api/clientes/:id", () => {
    svc.remove("C001").subscribe();
    const req = httpMock.expectOne(`${API_BASE_URL}/clientes/C001`);
    expect(req.request.method).toBe("DELETE");
    req.flush(null);
  });
});
