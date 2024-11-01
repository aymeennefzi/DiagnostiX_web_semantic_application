import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment'; // Adjust if needed

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private baseUrl = `${environment.baseUrl}/patients`; // Base URL for patient endpoints

  constructor(private http: HttpClient) {}

  // Get all patients
  getAllPatients(): Observable<any> {
    return this.http.get(`${this.baseUrl}`); // Adjusted to the correct path
  }

  // Get patient by name
  getPatientInfo(name: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/${name}`); // Adjusted to the correct path
  }

  // Add a new patient
  addPatient(patientData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/add`, patientData);
  }

  // Update a patient
  updatePatient(name: string, patientData: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/${name}`, patientData); // Adjusted to the correct path
  }

  // Delete a patient
  deletePatient(name: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${name}`); // Adjusted to the correct path
  }
}
