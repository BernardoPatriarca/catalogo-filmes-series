import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Genero, GeneroRequest } from '../models/genero.model';

@Injectable({ providedIn: 'root' })
export class GeneroService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/generos`;

  listAll(): Observable<Genero[]> {
    return this.http.get<Genero[]>(this.baseUrl);
  }

  getById(id: number): Observable<Genero> {
    return this.http.get<Genero>(`${this.baseUrl}/${id}`);
  }

  create(dto: GeneroRequest): Observable<Genero> {
    return this.http.post<Genero>(this.baseUrl, dto);
  }

  update(id: number, dto: GeneroRequest): Observable<Genero> {
    return this.http.put<Genero>(`${this.baseUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
