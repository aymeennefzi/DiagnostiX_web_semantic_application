import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MedecinService } from 'src/app/core/services/medecin.service';
import { MedecinDto } from 'src/app/models/medecin.dto';

@Component({
  selector: 'app-medecin-edit',
  templateUrl: './medecin-edit.component.html',
  styleUrls: ['./medecin-edit.component.css']
})
export class MedecinEditComponent implements OnInit {
  medicinDto: any= { nom: '', specialite: '', localisation: '' }; // Initialize with empty values
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private medicinService: MedecinService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const medicinName = this.route.snapshot.paramMap.get('name');
    if (medicinName) {
      this.loadMedicin(medicinName); // Load the current medicin details if necessary
    }
  }

  loadMedicin(name: string) {
    const medicinName = this.route.snapshot.paramMap.get('name');
    this.medicinService.getMedicinByName(name).subscribe(
      (data) => {
        if (data) {
          this.medicinDto = {
            nom: medicinName, // Handle unavailable name
            specialite: data.specialite,
            localisation: data.localisation
          };
        } else {
          this.errorMessage = 'Médecin non trouvé.';
        }
      },
      (error) => {
        this.errorMessage = 'Une erreur s\'est produite lors du chargement du médecin.';
        console.error(error);
      }
    );
  }

  updateMedicin() {
    const medicinName = this.route.snapshot.paramMap.get('name');
    if (medicinName) {
      this.medicinService.updateMedicin(medicinName, this.medicinDto).subscribe(
        (response) => {
          this.successMessage = 'Médecin mis à jour avec succès.';
          this.errorMessage = null;
          // Optionally navigate back or to a different page
        },
        (error) => {
          this.errorMessage = 'Une erreur s\'est produite lors de la mise à jour du médecin.';
          console.error(error);
        }
      );
    }
  }
}
