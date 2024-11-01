import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tip } from '../../../models/Tip/tip.model';

@Injectable({
  providedIn: 'root'
})
export class TipService {
  private baseUrl = 'http://localhost:8089/api/tips'; 

  constructor(private http: HttpClient) { }

  // Create a new tip
  createTip(tip: Tip): Observable<Tip> {
    return this.http.post<Tip>(this.baseUrl, tip, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    });
  }

  // Get a tip by cible
  getTipByCible(cible: string): Observable<Tip> {
    return this.http.get<Tip>(`${this.baseUrl}/${cible}`);
  }

  // Get all tips
  getAllTips(): Observable<Tip[]> {
    return this.http.get<Tip[]>(this.baseUrl);
  }

  // Update a tip
  updateTip(cible: string, tip: Tip): Observable<Tip> {
    return this.http.put<Tip>(`${this.baseUrl}/${cible}`, tip, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    });
  }

  // Delete a tip
  deleteTip(cible: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${cible}`);
  }
}
