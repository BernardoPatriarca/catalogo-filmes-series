import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { providePrimeNG } from 'primeng/config';
import { MessageService, ConfirmationService } from 'primeng/api';

import { routes } from './app.routes';
import { errorInterceptor } from './core/interceptors/error.interceptor';
import { CatalogoPreset } from './core/theme/theme-preset';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([errorInterceptor])),
    providePrimeNG({
      theme: {
        preset: CatalogoPreset,
        options: {
          darkModeSelector: '.app-dark',
        },
      },
    }),
    MessageService,
    ConfirmationService,
  ],
};
