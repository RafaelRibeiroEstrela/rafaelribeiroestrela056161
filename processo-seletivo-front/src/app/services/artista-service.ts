import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Artista } from '../models/artista';
import { Observable } from 'rxjs';
import { Pageable } from '../utils/pageable';
import { Page } from '../utils/page';
import { SortEnum } from '../utils/sort';

@Injectable({
  providedIn: 'root',
})
export class ArtistaService {
  //private readonly baseUrl = `${API_BASE_URL}/v1/artistas`;
  private readonly baseUrl = 'http://localhost:26000/v1/artistas';

  constructor(private readonly http: HttpClient) {}

  create(request: Artista): Observable<Artista> {
    return this.http.post<Artista>(this.baseUrl, request);
  }

  update(id: number, request: Artista): Observable<Artista> {
    return this.http.put<Artista>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  findById(id: number): Observable<Artista> {
    return this.http.get<Artista>(`${this.baseUrl}/${id}`);
  }

  find(nomeArtista: string, pageable: Pageable): Observable<Page<Artista>> {
    const page: number = pageable.pageNumber;
    const size: number = pageable.pageSize;
    const ordenacao: SortEnum = pageable.sort;
    let params: HttpParams = new HttpParams()
    .set('page', page)
    .set('size', size)
    .set('ordenacao', ordenacao);
    if (nomeArtista && nomeArtista.trim.length > 0) {
      params = params.set('nomeArtista', nomeArtista);
    }
    return this.http.get<Page<Artista>>(this.baseUrl, { params });
  }
}
