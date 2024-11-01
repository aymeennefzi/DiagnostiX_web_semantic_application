import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MedecinService } from 'src/app/core/services/medecin.service';
import { MedecinDto } from 'src/app/models/medecin.dto';

@Component({
  selector: 'app-medecin-add',
  templateUrl: './medecin-add.component.html',
  styleUrls: ['./medecin-add.component.css']
})
export class MedecinAddComponent {

  medicinDTO: any = {
    nom: '',
    specialite: '',
    localisation: ''
  };

  errorMessage: string | null = null;

  constructor(private medicinService: MedecinService, private router: Router) {}

  addMedicin() {
    this.medicinService.addMedicin(this.medicinDTO).subscribe(
      response => {
        console.log('Médecin ajouté avec succès:', response);
        this.router.navigate(['adherent/medecins']); // Redirect to the list of doctors after adding
      },
      error => {
        console.error('Erreur lors de l\'ajout du médecin:', error);
        this.errorMessage = 'Erreur lors de l\'ajout du médecin. Veuillez réessayer.';
      }
    );
  }

}
