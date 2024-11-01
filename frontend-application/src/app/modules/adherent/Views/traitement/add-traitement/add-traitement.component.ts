import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TraitementService } from 'src/app/core/services/traitement/traitement.service';

@Component({
  selector: 'app-add-traitement',
  templateUrl: './add-traitement.component.html',
  styleUrls: ['./add-traitement.component.css'],
})
export class AddTraitementComponent {
  traitementForm: FormGroup;
  error = '';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private traitementService: TraitementService,
    private router: Router
  ) {
    this.traitementForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
    });
  }

  onSubmit(): void {
    if (this.traitementForm.valid) {
      this.loading = true;
      console.log(this.traitementForm.value);
      this.traitementService
        .addTraitement(this.traitementForm.value)
        .subscribe({
          next: (data) => {
            console.log('zczczczvcevcv', data);
          },
          error: (error) => {
            if (error.status === 200) {
              this.router.navigate(['adherent/liste-traitement']);
            } else {
              this.error = 'Error adding treatment';
              this.loading = false;
              console.error(error);
            }
          },
        });
    }
  }
}
