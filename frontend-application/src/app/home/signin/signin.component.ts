import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/core/services/Authentification/authentification.service';
import { LoginPayload } from 'src/app/models/Login-Payload/login-payload';
import { Role } from 'src/app/models/Role/role.enu';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css'],
})
export class SigninComponent {
  password: string = '';
  passwordFieldType: string = 'password';

  togglePasswordVisibility(): void {
    this.passwordFieldType =
      this.passwordFieldType === 'password' ? 'text' : 'password';
  }

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
    ]),
  });

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {}

  login() {
    const payload: LoginPayload = {
      email: this.loginForm.value.email || '',
      password: this.loginForm.value.password || '',
    };

    this.authenticationService.login(payload).subscribe(
      (res: any) => {
        console.log(res);

        if (res && res.userDetails && res.userDetails.enabled) {
          const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1000,
            timerProgressBar: true,
            didOpen: (toast) => {
              toast.addEventListener('mouseenter', Swal.stopTimer);
              toast.addEventListener('mouseleave', Swal.resumeTimer);
            },
          });

          Toast.fire({
            icon: 'success',
            title: 'Connexion réussie',
          });

          localStorage.setItem('userconnect', JSON.stringify(res.userDetails));
          localStorage.setItem('accessToken', res.accessToken);
          localStorage.setItem('refreshToken', res.refreshToken);
          localStorage.setItem('state', '0');

          if (res.userDetails.role === Role.ADHERENT) {
            this.router.navigateByUrl('/adherent/Liste-livre');
          } else {
            this.router.navigateByUrl('/admin/liste-adhrents');
          }
        }
      },
      (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Login failed. Please check your credentials.',
          showConfirmButton: true,
        });
      }
    );
  }
}
