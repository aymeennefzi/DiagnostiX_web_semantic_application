import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MedecinDto } from 'src/app/models/medecin.dto';

@Injectable({
  providedIn: 'root'
})
export class MedecinService {

  private baseUrl = 'http://localhost:8089/medecins'; // Adjust the base URL as needed

  constructor(private http: HttpClient) {}

  getAllMedecins(): Observable<any> {
    return this.http.get(`${this.baseUrl}/all`);
  }

  addMedicin(medicinDTO: MedecinDto): Observable<any> {
    return this.http.post(`${this.baseUrl}/add`, medicinDTO);
  }

  updateMedicin(name: string, medicinDTO: MedecinDto): Observable<any> {
    return this.http.put(`${this.baseUrl}/update/${name}`, medicinDTO);
  }

  deleteMedicinByName(name: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete/${name}`);
  }

  getMedicinByName(name: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/${name}`);
  }
}
