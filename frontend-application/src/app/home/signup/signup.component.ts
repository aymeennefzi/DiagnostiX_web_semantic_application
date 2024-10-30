import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/core/services/Authentification/authentification.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent {
  isLoading: boolean = false;
  passwordFieldType: string = 'password';
  registerForm = new FormGroup({
    nom: new FormControl('', [Validators.required, Validators.minLength(3)]),
    cin: new FormControl('', [Validators.required, Validators.minLength(6)]),
    prenom: new FormControl('', [Validators.required, Validators.minLength(3)]),
    numeroTelephone: new FormControl('', Validators.required),
    image: new FormControl('', [Validators.required]),
    dateNaissance: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
    ]),
  });

  selectedFile: File | null = null;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit(): void {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0] as File;
  }

  togglePasswordVisibility(): void {
    this.passwordFieldType =
      this.passwordFieldType === 'password' ? 'text' : 'password';
  }

  passwordsMatchValidator(formGroup: FormGroup) {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordsMismatch: true };
  }

  register() {
    this.isLoading = true;
    const formData = new FormData();

    const addValueToFormData = (key: string, value: any) => {
      if (value != null) {
        formData.append(key, value);
      }
    };

    addValueToFormData('nom', this.registerForm.get('nom')?.value);
    addValueToFormData('cin', this.registerForm.get('cin')?.value);
    addValueToFormData('prenom', this.registerForm.get('prenom')?.value);
    addValueToFormData(
      'numeroTelephone',
      this.registerForm.get('numeroTelephone')?.value
    );
    addValueToFormData(
      'dateNaissance',
      this.registerForm.get('dateNaissance')?.value
    );
    addValueToFormData('email', this.registerForm.get('email')?.value);
    addValueToFormData('password', this.registerForm.get('password')?.value);

    if (this.selectedFile) {
      formData.append('image', this.selectedFile, this.selectedFile.name);
    }

    this.authenticationService.registerUser(formData).subscribe(
      (response: any) => {
        this.isLoading = false;
        if (response) {
          console.log(response);
          localStorage.setItem('emailUserInscrit', response?.email);
          Swal.fire({
            title: 'Succès !',
            text: 'Votre compte a été créé. Vérifiez votre email pour confirmer votre compte.',
            icon: 'success',
            confirmButtonText: 'OK',
          });
          this.registerForm.reset(); // Reset form after success
          this.redirectToVerifAccount();
        } else {
          Swal.fire({
            title: 'Erreur',
            text: "Une erreur s'est produite. Veuillez réessayer.",
            icon: 'error',
            confirmButtonText: 'OK',
          });
        }
      },
      (error) => {
        this.isLoading = false;
        Swal.fire({
          title: 'Erreur',
          text: "Une erreur s'est produite lors de la création de votre compte. Veuillez réessayer.",
          icon: 'error',
          confirmButtonText: 'OK',
        });
      }
    );
  }
  redirectToVerifAccount() {
    this.router.navigate(['verify-account']);
  }
}
