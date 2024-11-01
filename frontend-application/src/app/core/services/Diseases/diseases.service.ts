import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { Diseases } from 'src/app/models/diseases/diseases';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DiseasesService {

  constructor(private http: HttpClient) { }

  getDiseaseInfo(diseaseName: string): Observable<any> {
    return this.http.get(`${environment.baseUrl}/${diseaseName}`);
  }
    addDisease(disease: Diseases): Observable<{ success: boolean; message: string }> {
    return this.http.post<{ success: boolean; message: string }>(
      `${environment.baseUrl}/add`,disease,{
        headers: new HttpHeaders({
          'Content-Type': 'application/json', 
        }),
      }
    );
  }
  getDiseaseByName(name: string): Observable<any> {
    return this.http.get<Map<string, any>>(`${environment.baseUrl}/${name}`)
      .pipe(
        catchError(this.handleError)
      );
  }
  updateDisease(name: string, diseaseDTO: Diseases): Observable<any> {
    const url = `${environment.baseUrl}/update/${name}`;
    const headers = new HttpHeaders({'Content-Type': 'application/json'});

    return this.http.put(url, diseaseDTO, { headers });
  }

  deleteDisease(name: string): Observable<HttpResponse<any>> {
    return this.http.delete<HttpResponse<any>>(`${environment.baseUrl}/delete/${name}`, { observe: 'response' });
  }
  checkDiseaseNameUnique(diseaseName: string): Observable<{ [key: string]: any }> {
    return this.http.get<{ [key: string]: any }>(`${environment.baseUrl}/disease/check/${diseaseName}`);
  }

  getAllDiseases(): Observable<Map<string, string>[]> {
    return this.http.get<Map<string, string>[]>(`${environment.baseUrl}/diseases`);
  }

  updateDiseaseByName(name: string, diseaseDTO: Diseases): Observable<string> {
    return this.http.put<string>(`${environment.baseUrl}/update/${name}`, diseaseDTO);
  }
  private handleError(error: HttpErrorResponse) {
    if (error.status === 404) {
      return throwError('Disease not found');
    } else {
      return throwError('An error occurred');
    }
  }
}
