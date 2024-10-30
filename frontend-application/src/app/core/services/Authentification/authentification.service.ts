import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginPayload } from 'src/app/models/Login-Payload/login-payload';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  constructor(private http: HttpClient) {}

  registerUser(formData: FormData) {
    return this.http.post(
      `${environment.baseUrl}/auth/registerAdhrent`,
      formData
    );
  }

  confirm(formData: FormData) {
    return this.http.post(`${environment.baseUrl}/auth/verifyUser`, formData);
  }

  login(user: LoginPayload) {
    return this.http.post(`${environment.baseUrl}/auth/login`, user);
  }
}
