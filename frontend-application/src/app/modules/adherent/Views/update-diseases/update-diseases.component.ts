import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DiseasesService } from 'src/app/core/services/Diseases/diseases.service';
import { Diseases } from 'src/app/models/diseases/diseases';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-diseases',
  templateUrl: './update-diseases.component.html',
  styleUrls: ['./update-diseases.component.css']
})
export class UpdateDiseasesComponent implements OnInit {
  diseaseForm!: FormGroup;
  errorMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private diseaseService: DiseasesService,
    private router: Router 
  ) {}

  ngOnInit(): void {
    this.diseaseForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      symptoms: ['', Validators.required],
      treatments: ['', Validators.required]
    });

    const name = this.route.snapshot.paramMap.get('name');
    if (name) {
      this.loadDiseaseDetails(name);
    }
  }

  loadDiseaseDetails(name: string): void {
    this.diseaseService.getDiseaseByName(name).subscribe(
      (data) => {
        this.diseaseForm.patchValue({
          name: name,
          description: data.description,
          symptoms: data.symptoms,
          treatments: data.treatments
        });
      },
      (error) => {
        this.errorMessage = 'La maladie n’a pas été trouvée ou une erreur est survenue.';
      }
    );
  }

  onSubmit(): void {
    if (this.diseaseForm.valid) {
      const name = this.route.snapshot.paramMap.get('name');
  
      const disease: Diseases = {
        name: name!, 
        description: this.diseaseForm.value.description,
        treatments: this.diseaseForm.value.treatments.split(',').map((item: any) => item.trim()), 
        symptoms: this.diseaseForm.value.symptoms.split(',').map((item: any) => item.trim())
      };
  
      this.diseaseService.updateDisease(name!, disease).subscribe(
        (response) => {
          console.log('Mise à jour réussie:', response);
          
          Swal.fire({
            icon: 'success',
            title: 'Mise à jour réussie',
            text: 'La maladie a été mise à jour avec succès.',
            confirmButtonText: 'OK'
          }).then(() => {
            this.router.navigate(['/adherent/Liste-livre']);
          });
        },
        (error) => {
          this.errorMessage = 'Une erreur est survenue lors de la mise à jour de la maladie.';
          console.error('Erreur lors de la mise à jour:', error);
          
          Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Une erreur est survenue lors de la mise à jour de la maladie.',
            confirmButtonText: 'OK'
          });
        }
      );
    } else {
      this.errorMessage = 'Veuillez remplir tous les champs obligatoires.';
    }
  }
}
