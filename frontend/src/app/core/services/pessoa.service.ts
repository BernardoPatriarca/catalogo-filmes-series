import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Pessoa, PessoaRequest } from '../models/pessoa.model';

@Injectable({ providedIn: 'root' })
export class PessoaService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/pessoas`;

  search(nome?: string): Observable<Pessoa[]> {
    let params = new HttpParams();
    if (nome) params = params.set('nome', nome);
    return this.http.get<Pessoa[]>(this.baseUrl, { params });
  }

  getById(id: number): Observable<Pessoa> {
    return this.http.get<Pessoa>(`${this.baseUrl}/${id}`);
  }

  create(dto: PessoaRequest): Observable<Pessoa> {
    return this.http.post<Pessoa>(this.baseUrl, dto);
  }

  update(id: number, dto: PessoaRequest): Observable<Pessoa> {
    return this.http.put<Pessoa>(`${this.baseUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
