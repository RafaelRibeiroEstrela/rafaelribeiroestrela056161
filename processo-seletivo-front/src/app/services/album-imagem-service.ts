import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AlbumImagem } from '../models/album-imagem';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AlbumImagemService {
  //private readonly baseUrl = `${API_BASE_URL}/v1/artistas`;
  private readonly baseUrl = 'http://localhost:26000/v1/albuns/imagens';

  constructor(private readonly http: HttpClient) {}

  uploadBase64(albumId: number, files: AlbumImagem[]): Observable<AlbumImagem[]> {
    return this.http.put<AlbumImagem[]>(`${this.baseUrl}/upload/base64/${albumId}`, files);
  }

  downloadBase64(albumId: number): Observable<AlbumImagem[]> {
    return this.http.get<AlbumImagem[]>(`${this.baseUrl}/download/base64/${albumId}`);
  }

  deleteByAlbumId(albumId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${albumId}`);
  }

  deleteByStorageKey(key: string): Observable<void> {
    const params = new HttpParams().set('key', key);
    return this.http.delete<void>(`${this.baseUrl}/delete/storage-key`, { params });
  }
}
