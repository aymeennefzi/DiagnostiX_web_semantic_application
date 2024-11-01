import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DiseasesService } from 'src/app/core/services/Diseases/diseases.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-diseases',
  templateUrl: './add-diseases.component.html',
  styleUrls: ['./add-diseases.component.css']
})
export class AddDiseasesComponent implements OnInit{
  diseaseForm!: FormGroup;
  constructor(
    private formBuilder: FormBuilder,
    private diseasesService: DiseasesService,
    private router: Router
  ) {
    this.diseaseForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      symptoms: ['', Validators.required],
      treatments: ['', Validators.required],
    });
  }
  ngOnInit(): void {}
  onSubmit(): void {
    if (this.diseaseForm.valid) {
        const diseaseData = {
            name: this.diseaseForm.value.name,
            description: this.diseaseForm.value.description,
            treatments: this.diseaseForm.value.treatments.split(',').map((t: string) => t.trim()),
            symptoms: this.diseaseForm.value.symptoms.split(',').map((s: string) => s.trim()),
        };

        this.diseasesService.addDisease(diseaseData).subscribe({
            next: (response) => {
                console.log('Maladie ajoutée avec succès:', response);
                // Vérifiez que la réponse contient les données nécessaires
                if (response && response.success) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Succès',
                        text: 'La maladie a été ajoutée avec succès!',
                    }).then(() => {
                        this.router.navigate(['/adherent/Liste-livre']);
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Erreur',
                        text: 'Une erreur est survenue : ' + (response.message || 'Erreur inconnue.'),
                    });
                }
            },
            error: (err) => {
                console.error('Erreur lors de l\'ajout de la maladie:', err);
                Swal.fire({
                    icon: 'error',
                    title: 'Erreur',
                    text: 'Une erreur est survenue lors de l\'ajout de la maladie. Veuillez réessayer.',
                });
            }
        });
    } else {
        // Si le formulaire est invalide, vous pouvez afficher un message d'erreur
        Swal.fire({
            icon: 'warning',
            title: 'Attention',
            text: 'Veuillez remplir tous les champs requis.',
        });
    }
}


}
