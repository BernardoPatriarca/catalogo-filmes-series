import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PageResponse } from '../models/page.model';
import { TituloDetalhe, TituloFiltro, TituloListItem, TituloRequest } from '../models/titulo.model';

@Injectable({ providedIn: 'root' })
export class TituloService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/titulos`;

  search(filtro: TituloFiltro): Observable<PageResponse<TituloListItem>> {
    let params = new HttpParams();
    if (filtro.titulo) params = params.set('titulo', filtro.titulo);
    if (filtro.generoId) params = params.set('generoId', filtro.generoId);
    if (filtro.tipo) params = params.set('tipo', filtro.tipo);
    if (filtro.ano) params = params.set('ano', filtro.ano);
    if (filtro.notaMin !== undefined && filtro.notaMin !== null) params = params.set('notaMin', filtro.notaMin);
    if (filtro.notaMax !== undefined && filtro.notaMax !== null) params = params.set('notaMax', filtro.notaMax);
    params = params.set('page', filtro.page ?? 0);
    params = params.set('size', filtro.size ?? 10);
    params = params.set('sort', filtro.sort ?? 'titulo');
    params = params.set('direction', filtro.direction ?? 'asc');

    return this.http.get<PageResponse<TituloListItem>>(this.baseUrl, { params });
  }

  getById(id: number): Observable<TituloDetalhe> {
    return this.http.get<TituloDetalhe>(`${this.baseUrl}/${id}`);
  }

  create(dto: TituloRequest): Observable<TituloDetalhe> {
    return this.http.post<TituloDetalhe>(this.baseUrl, dto);
  }

  update(id: number, dto: TituloRequest): Observable<TituloDetalhe> {
    return this.http.put<TituloDetalhe>(`${this.baseUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
