import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DiseasesService } from 'src/app/core/services/Diseases/diseases.service';
import { Diseases } from 'src/app/models/diseases/diseases';

@Component({
  selector: 'app-details-diseases',
  templateUrl: './details-diseases.component.html',
  styleUrls: ['./details-diseases.component.css']
})
export class DetailsDiseasesComponent implements OnInit {
  disease: Diseases | null = null;
  diseaseName: string | null = null; // Ajouter une propriété pour stocker le nom

  ngOnInit(): void {
    this.diseaseName = this.route.snapshot.paramMap.get('name');
    if (this.diseaseName) {
      this.getDiseaseDetails(this.diseaseName);
    }
  }
  constructor(private router: Router , private diseasesService: DiseasesService , private route: ActivatedRoute) {}
  
  getDiseaseDetails(name: string): void {
    this.diseasesService.getDiseaseByName(name).subscribe(
      (data:any) => {
        this.disease = data;
        console.log('Disease details:', data);
      },
      (error:any) => {
        console.error('Error fetching disease details:', error);
      }
    );
  }

}
