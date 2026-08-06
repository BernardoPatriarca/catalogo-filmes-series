import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Avaliacao, AvaliacaoRequest } from '../models/avaliacao.model';

@Injectable({ providedIn: 'root' })
export class AvaliacaoService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  listByTitulo(tituloId: number): Observable<Avaliacao[]> {
    return this.http.get<Avaliacao[]>(`${this.apiUrl}/titulos/${tituloId}/avaliacoes`);
  }

  create(tituloId: number, dto: AvaliacaoRequest): Observable<Avaliacao> {
    return this.http.post<Avaliacao>(`${this.apiUrl}/titulos/${tituloId}/avaliacoes`, dto);
  }

  update(id: number, dto: AvaliacaoRequest): Observable<Avaliacao> {
    return this.http.put<Avaliacao>(`${this.apiUrl}/avaliacoes/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/avaliacoes/${id}`);
  }
}
