import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { MessageService } from 'primeng/api';
import { catchError, throwError } from 'rxjs';

interface ErrorResponseBody {
  message?: string;
  errors?: string[];
}

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const messageService = inject(MessageService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const body = error.error as ErrorResponseBody | undefined;
      const detail =
        body?.errors && body.errors.length > 0
          ? body.errors.join('; ')
          : body?.message ?? 'Algo deu errado ao falar com o servidor. Tente novamente em instantes.';

      messageService.add({
        severity: 'error',
        summary: 'Ops, tivemos um problema',
        detail,
        life: 6000,
      });

      return throwError(() => error);
    }),
  );
};
