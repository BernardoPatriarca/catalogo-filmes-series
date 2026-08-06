import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Estatisticas } from '../models/estatisticas.model';

@Injectable({ providedIn: 'root' })
export class EstatisticaService {
  private readonly http = inject(HttpClient);

  obter(): Observable<Estatisticas> {
    return this.http.get<Estatisticas>(`${environment.apiUrl}/estatisticas`);
  }
}
