import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Album } from '../models/album';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AlbumService {
  //private readonly baseUrl = `${API_BASE_URL}/v1/artistas`;
  private readonly baseUrl = 'http://localhost:26000/v1/albuns';

  constructor(private readonly http: HttpClient) {}

  create(request: Album): Observable<Album> {
    return this.http.post<Album>(this.baseUrl, request);
  }

  update(id: number, request: Album): Observable<Album> {
    return this.http.put<Album>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  findById(id: number): Observable<Album> {
    return this.http.get<Album>(`${this.baseUrl}/${id}`);
  }
}
