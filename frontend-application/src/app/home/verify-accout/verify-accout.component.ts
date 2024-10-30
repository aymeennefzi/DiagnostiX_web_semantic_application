import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationService } from 'src/app/core/services/Authentification/authentification.service';

import Swal from 'sweetalert2';

@Component({
  selector: 'app-verify-accout',
  templateUrl: './verify-accout.component.html',
  styleUrls: ['./verify-accout.component.css'],
})
export class VerifyAccoutComponent implements OnInit {
  message = '';
  isOkay = true;
  submitted = false;
  email!: string | null;
  code!: any;
  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {}

  ngOnInit(): void {}

  confirmAccount() {
    const formData = new FormData();
    this.email = localStorage.getItem('emailUserInscrit');
    if (this.email != null || this.email != undefined) {
      formData.append('email', this.email);
    }
    formData.append('code', this.code);
    this.authService.confirm(formData).subscribe(
      (response: any) => {
        if (response) {
          Swal.fire({
            title: 'Succès !',
            text: 'Votre compte a été vonfirmé',
            icon: 'success',
            confirmButtonText: 'OK',
          });
          this.router.navigate(['signin']);
        } else {
          Swal.fire({
            title: 'Erreur',
            text: 'Votre code est faux',
            icon: 'error',
            confirmButtonText: 'OK',
          });
        }
      },
      (error) => {
        Swal.fire({
          title: 'Succès !',
          text: 'Votre compte a été vonfirmé',
          icon: 'success',
          confirmButtonText: 'OK',
        });
      }
    );
  }

  redirectToLogin() {
    this.router.navigate(['signin']);
  }

  onCodeCompleted(code: string) {
    this.code = code;
    console.log(code);
    this.confirmAccount();
    this.redirectToLogin();
  }
}
