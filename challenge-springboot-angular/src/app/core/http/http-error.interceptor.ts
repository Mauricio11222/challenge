import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { ToastService } from '../../shared/toast/toast.service';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const toast = inject(ToastService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      console.error('HTTP error interceptado:', error);

      let msg = 'Error inesperado';

      if (error.status === 0) {
        msg = 'No se pudo conectar con el servidor (¿API caída o CORS?)';
      } else if (error.status >= 500) {
        msg = 'Error en el servidor';
      } else if (error.error?.message) {
        msg = error.error.message;
      }

      // Mostrar mensaje global
      toast.error(msg);

      // Re-lanzar el error
      return throwError(() => error);
    })
  );
};
