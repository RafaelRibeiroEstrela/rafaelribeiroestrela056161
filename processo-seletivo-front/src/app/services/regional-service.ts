import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Regional } from '../models/regional';

@Injectable({
  providedIn: 'root',
})
export class RegionalService {
  //private readonly baseUrl = `${API_BASE_URL}/v1/artistas`;
  private readonly baseUrl = 'http://localhost:26000/v1/regionais';

  constructor(private readonly http: HttpClient) {}

  sync(): Observable<string> {
    return this.http.get(`${this.baseUrl}/synchronize`, {
      responseType: 'text',
    });
  }

  findAll(): Observable<Regional[]> {
    return this.http.get<Regional[]>(`${this.baseUrl}/all`);
  }
}
