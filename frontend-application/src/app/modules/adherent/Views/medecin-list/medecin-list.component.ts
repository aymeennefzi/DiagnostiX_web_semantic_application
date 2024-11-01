import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { MedecinService } from 'src/app/core/services/medecin.service';
import { MedecinDto } from 'src/app/models/medecin.dto';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-medecin-list',
  templateUrl: './medecin-list.component.html',
  styleUrls: ['./medecin-list.component.css']
})
export class MedecinListComponent implements OnInit{
  medecins: any[] = []; // Array to hold the list of medecins
  errorMessage: string | null = null; // To hold any error message

  constructor(private medicinService: MedecinService, private router:Router ) {}

  ngOnInit(): void {
    this.loadMedecins();
  }
  addMedecin() {
    this.router.navigate(['adherent/medecins/add']);
  }
  editMedecin(name: string) {
    this.router.navigate(['adherent/medecin/update', name]); // Adjust the path based on your routing setup
  }


  loadMedecins() {
    this.medicinService.getAllMedecins().subscribe(
        (response: any) => {
            console.log(response);
            this.medecins = response.medecins; // Access the 'medecins' property
        },
        (error) => {
            this.errorMessage = 'Failed to load medecins'; // Handle error
            console.error(error);
        }
    );
}
deleteMedecin(name: string): void {
  Swal.fire({
    title: 'Êtes-vous sûr?',
    text: `Vous allez supprimer la maladie: ${name}`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Oui, supprimer!',
    cancelButtonText: 'Annuler'
  }).then((result) => {
    if (result.isConfirmed) {
      this.medicinService.deleteMedicinByName(name).subscribe(
        response => {
          Swal.fire(
            'Supprimé!',
            `La maladie "${name}" a été supprimée avec succès.`,
            'success'
          );
          this.ngOnInit(); // Rechargez les maladies
        },
        error => {
          Swal.fire(
            'Erreur!',
            'Une erreur s\'est produite lors de la suppression de le medecin.',
            'error'
          );
          console.error('Erreur lors de la suppression de la maladie:', error);
        }
      );
    }
  });
}

}
