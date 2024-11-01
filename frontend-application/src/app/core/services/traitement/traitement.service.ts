import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Traitement } from 'src/app/models/traitement/traitement';

@Injectable({
  providedIn: 'root'
})
export class TraitementService {

  private apiUrl = 'http://localhost:8089/traitement';

  constructor(private http: HttpClient) { }

  getAllTraitements(): Observable<Traitement[]> {
    return this.http.get<Traitement[]>(this.apiUrl);
  }

  addTraitement(traitement: Traitement): Observable<any> {
    return this.http.post(this.apiUrl, traitement);
  }

  updateTraitement(oldName: string, traitement: Traitement): Observable<any> {
    return this.http.put(`${this.apiUrl}/${oldName}`, traitement);
  }

  deleteTraitement(name: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${name}`);
  }
}
