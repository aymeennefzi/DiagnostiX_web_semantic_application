import { Component, OnInit } from '@angular/core';
import { Symptom, SymptomService } from 'src/app/core/services/symptom.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-symptom',
  templateUrl: './symptom.component.html',
  styleUrls: ['./symptom.component.css']
})
export class SymptomComponent implements OnInit {
  symptoms: Symptom[] = [];
  newSymptom: Symptom = { nom: '', description: '' };
  editedSymptom: Symptom | null = null;
  message: string = '';
  isAddModalOpen: boolean = false; //
  constructor(private symptomService: SymptomService) {}

  ngOnInit(): void {
    this.getAllSymptoms();
  }
  closeAddModal(): void {
    // Code pour fermer le modal, par exemple, réinitialiser les champs ou masquer le modal
    this.newSymptom = { nom: '', description: '' }; // Réinitialiser les valeurs
  }
  

  getAllSymptoms(): void {
    this.symptomService.getSymptoms().subscribe({
      next: (data) => this.symptoms = data,
      error: (error) => console.error(error)
    });
  }

  openAddModal(): void {
    Swal.fire({
      title: 'Ajouter un symptôme',
      html: `
        <input id="nom" class="swal2-input" placeholder="Nom du symptôme">
        <input id="description" class="swal2-input" placeholder="Description">
      `,
      focusConfirm: false,
      preConfirm: () => {
        const nom = (document.getElementById('nom') as HTMLInputElement).value;
        const description = (document.getElementById('description') as HTMLInputElement).value;
        if (!nom || !description) {
          Swal.showValidationMessage('Veuillez entrer le nom et la description du symptôme.');
        }
        return { nom, description };
      }
    }).then((result) => {
      if (result.isConfirmed) {
        this.newSymptom = result.value;
        this.addSymptom();
      }
    });
  }

  openEditModal(symptom: Symptom): void {
    this.editedSymptom = { ...symptom };
    Swal.fire({
      title: 'Modifier le symptôme',
      html: `
        <input id="nom" class="swal2-input" value="${this.editedSymptom.nom}" placeholder="Nom">
        <input id="description" class="swal2-input" value="${this.editedSymptom.description}" placeholder="Description">
      `,
      focusConfirm: false,
      preConfirm: () => {
        const nom = (document.getElementById('nom') as HTMLInputElement).value;
        const description = (document.getElementById('description') as HTMLInputElement).value;
        if (!nom || !description) {
          Swal.showValidationMessage('Veuillez entrer le nom et la description du symptôme.');
        }
        return { nom, description };
      }
    }).then((result) => {
      if (result.isConfirmed) {
        this.editedSymptom = result.value;
        this.editSymptom();
      }
    });
  }

  addSymptom(): void {
    this.symptomService.addSymptom(this.newSymptom).subscribe({
      next: () => {
        this.getAllSymptoms();
        Swal.fire('Succès!', 'Symptôme ajouté avec succès.', 'success');
      },
      error: (error) => {
        Swal.fire('Erreur!', 'Échec de l\'ajout du symptôme.', 'error');
      }
    });
  }

  editSymptom(): void {
    if (this.editedSymptom) {
      const currentName = this.editedSymptom.nom;
      const symptomDTO = {
        nom: this.editedSymptom.nom,
        description: this.editedSymptom.description
      };

      this.symptomService.updateSymptom(currentName, symptomDTO).subscribe({
        next: () => {
          this.getAllSymptoms();
          Swal.fire('Succès!', 'Symptôme mis à jour avec succès.', 'success');
        },
        error: (error) => {
          Swal.fire('Erreur!', 'Échec de la mise à jour du symptôme.', 'error');
        }
      });
    }
  }

// symptom.component.ts
deleteSymptom(symptomName: string): void {
  Swal.fire({
      title: 'Êtes-vous sûr?',
      text: "Cette action est irréversible!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Oui, supprimer!',
      cancelButtonText: 'Annuler'
  }).then((result) => {
      if (result.isConfirmed) {
          // Assurez-vous que le nom du symptôme est passé correctement ici
          this.symptomService.deleteSymptom(symptomName).subscribe({
              next: () => {
                  this.getAllSymptoms(); // Actualiser la liste des symptômes
                  Swal.fire('Supprimé!', 'Le symptôme a été supprimé.', 'success');
              },
              error: (error) => {
                  Swal.fire('Erreur!', 'Échec de la suppression du symptôme.', 'error');
              }
          });
      }
  });
}
// symptom.component.ts
deleteAllSymptoms(): void {
  Swal.fire({
    title: 'Êtes-vous sûr?',
    text: "Cette action supprimera tous les symptômes! Cette action est irréversible!",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Oui, supprimer tout!',
    cancelButtonText: 'Annuler'
  }).then((result) => {
    if (result.isConfirmed) {
      this.symptomService.deleteAllSymptoms().subscribe({
        next: () => {
          this.getAllSymptoms(); // Actualiser la liste des symptômes
          Swal.fire('Supprimé!', 'Tous les symptômes ont été supprimés.', 'success');
        },
        error: (error) => {
          Swal.fire('Erreur!', 'Échec de la suppression des symptômes.', 'error');
        }
      });
    }
  });
}


}
