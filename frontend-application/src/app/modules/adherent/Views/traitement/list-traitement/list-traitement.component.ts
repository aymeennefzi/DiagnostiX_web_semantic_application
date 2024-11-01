import { Component, OnInit } from '@angular/core';
import { TraitementService } from 'src/app/core/services/traitement/traitement.service';
import { Traitement } from 'src/app/models/traitement/traitement';

@Component({
  selector: 'app-list-traitement',
  templateUrl: './list-traitement.component.html',
  styleUrls: ['./list-traitement.component.css'],
})
export class ListTraitementComponent implements OnInit {
  traitements: Traitement[] = [];
  loading = false;
  error = '';

  constructor(private traitementService: TraitementService) {}

  ngOnInit(): void {
    this.loadTraitements();
  }

  loadTraitements(): void {
    this.loading = true;
    this.traitementService.getAllTraitements().subscribe({
      next: (data) => {
        this.traitements = data;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error loading treatments';
        this.loading = false;
        console.error(error);
      },
    });
  }

  deleteTraitement(traitement: Traitement): void {
    if (
      traitement.name &&
      confirm('Are you sure you want to delete this treatment?')
    ) {
      this.traitementService.deleteTraitement(traitement.name).subscribe({
        next: () => {},
        error: (error) => {
          if (error.status === 200) {
            this.loadTraitements();
          } else {
            this.error = 'Error deleting treatment';
            this.loading = false;
            console.error(error);
          }
        },
      });
    }
  }
}
