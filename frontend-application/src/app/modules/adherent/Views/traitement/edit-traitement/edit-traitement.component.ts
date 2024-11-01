import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TraitementService } from 'src/app/core/services/traitement/traitement.service';
import { Traitement } from 'src/app/models/traitement/traitement';

@Component({
  selector: 'app-edit-traitement',
  templateUrl: './edit-traitement.component.html',
  styleUrls: ['./edit-traitement.component.css'],
})
export class EditTraitementComponent {
  traitementForm: FormGroup;
  loading = false;
  error = '';
  originalName = '';

  constructor(
    private formBuilder: FormBuilder,
    private traitementService: TraitementService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.traitementForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: [''],
    });
  }

  ngOnInit(): void {
    // Get the treatment name from the route parameters
    this.route.params.subscribe((params) => {
      this.originalName = params['name'];
      this.loadTraitementData(this.originalName);
    });
  }

  loadTraitementData(name: string): void {
    this.traitementService.getAllTraitements().subscribe(
      (traitements) => {
        const traitement = traitements.find((t) => t.name === name);
        if (traitement) {
          this.traitementForm.patchValue({
            name: traitement.name,
            description: traitement.description,
          });
        } else {
          this.error = 'Treatment not found';
        }
      },
      (error) => {
        this.error = 'Error loading treatment data';
        console.error('Error:', error);
      }
    );
  }

  onSubmit(): void {
    if (this.traitementForm.valid) {
      this.loading = true;
      this.error = '';

      const updatedTraitement: Traitement = {
        name: this.traitementForm.value.name,
        description: this.traitementForm.value.description,
      };

      this.traitementService
        .updateTraitement(this.originalName, updatedTraitement)
        .subscribe(
          () => {
            this.loading = false;
            this.router.navigate(['/admin/liste-traitement']);
          },
          (error) => {
            if (error.status === 200) {
              this.router.navigate(['adherent/liste-traitement']);
            } else {
              this.error = 'Error editing treatment';
              this.loading = false;
              console.error(error);
            }
          }
        );
    }
  }
}
