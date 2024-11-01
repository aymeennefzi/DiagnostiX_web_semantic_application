import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DiseasesService } from 'src/app/core/services/Diseases/diseases.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-liste-livre',
  templateUrl: './liste-livre.component.html',
  styleUrls: ['./liste-livre.component.css']
})
export class ListeLivreComponent implements OnInit {
  allDisease: any[] = [];
  filteredDiseases: any[] = []; // Liste filtrée
  filter: string = ''; // Valeur du filtre

  constructor(
    private diseasesService: DiseasesService, 
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadAllDiseases();
  }

  loadAllDiseases(): void {
    this.diseasesService.getAllDiseases().subscribe(
      (data) => {
        this.allDisease = data;
        this.filteredDiseases = data; // Initialiser la liste filtrée
        console.log('Diseases loaded:', data);
      },
      (error) => {
        console.error('Error fetching diseases:', error);
      }
    );
  }

  filterDiseases(): void {
    const searchTerm = this.filter.toLowerCase(); // Convertir en minuscules pour la recherche
    this.filteredDiseases = this.allDisease.filter(disease => {
      // Vérifier si les symptômes et traitements sont des tableaux
      const symptoms = Array.isArray(disease.symptoms) ? disease.symptoms : [disease.symptoms];
      const treatments = Array.isArray(disease.treatments) ? disease.treatments : [disease.treatments];

      return (
        disease.name.toLowerCase().includes(searchTerm) || // Filtrer par nom
        symptoms.some((symptom: any) => symptom.toLowerCase().includes(searchTerm)) || // Filtrer par symptômes
        treatments.some((treatment: any) => treatment.toLowerCase().includes(searchTerm)) // Filtrer par traitements
      );
    });
  }

  addDisease() {
    this.router.navigate(['adherent/Liste-livre/addDisease']);
  }


  goToUpdateDisease(diseaseName: string): void {
    this.router.navigate([`adherent/Liste-livre/UpdateDisease`, diseaseName]);
  }
  goToDetailDisease(diseaseName: string): void {
    this.router.navigate([`adherent/Liste-livre/DetailDisease`, diseaseName]);
  }
  CheckDisease(diseaseName: string) {
    this.diseasesService.checkDiseaseNameUnique(diseaseName).subscribe(
      (response : any) => {
        if (response.isUnique) {
          Swal.fire({
            title: 'Unique!',
            text: `La maladie "${diseaseName}" est unique.`,
            icon: 'success',
            confirmButtonText: 'OK'
          });
        } else {
          Swal.fire({
            title: 'Existe!',
            text: `La maladie "${diseaseName}" existe déjà.`,
            icon: 'warning',
            confirmButtonText: 'OK'
          });
        }
      },
      (error) => {
        Swal.fire({
          title: 'Erreur!',
          text: `Impossible de vérifier l'unicité de la maladie.`,
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    );
  }
  deleteDisease(name: string): void {
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
        this.diseasesService.deleteDisease(name).subscribe(
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
              'Une erreur s\'est produite lors de la suppression de la maladie.',
              'error'
            );
            console.error('Erreur lors de la suppression de la maladie:', error);
          }
        );
      }
    });
  }
}
