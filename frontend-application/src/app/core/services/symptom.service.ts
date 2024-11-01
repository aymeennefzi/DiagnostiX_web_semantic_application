import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Symptom {
  nom: string;
  description: string;
}

@Injectable({
  providedIn: 'root'
})
export class SymptomService {
  private apiUrl = 'http://localhost:8089'; // Port 8089 pour votre API

  constructor(private http: HttpClient) { }

  // Récupérer tous les symptômes
  getSymptoms(): Observable<Symptom[]> {
    return this.http.get<Symptom[]>(`${this.apiUrl}/symptoms`);
  }

  // Récupérer un symptôme par son nom
  getSymptomByName(symptomName: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/symptom/${symptomName}`);
  }

  // Ajouter un nouveau symptôme
  addSymptom(symptom: Symptom): Observable<Symptom> {
    return this.http.post<Symptom>(`${this.apiUrl}/addSymptom`, symptom);
  }

  // Modifier un symptôme existant
  updateSymptom(currentName: string, symptom: Symptom): Observable<Symptom> {
    return this.http.put<Symptom>(`${this.apiUrl}/updateSymptom/${currentName}`, symptom);
  }

  // Supprimer un symptôme
  deleteSymptom(symptomNom: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/deleteSymptom/${symptomNom}`);
  }

  // Supprimer tous les symptômes
  deleteAllSymptoms(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/deleteAllSymptoms`);
  }
}
