import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdherentRoutingModule } from './adherent-routing.module';
import { ListeLivreComponent } from './Views/liste-livre/liste-livre.component';
import { AdherantLayoutComponent } from './adherant-layout/adherant-layout.component';
import { ListTraitementComponent } from './Views/traitement/list-traitement/list-traitement.component';
import { AddTraitementComponent } from './Views/traitement/add-traitement/add-traitement.component';
import { ReactiveFormsModule } from '@angular/forms';
import { EditTraitementComponent } from './Views/traitement/edit-traitement/edit-traitement.component';
import { AddDiseasesComponent } from './Views/add-diseases/add-diseases.component';
import { FormsModule } from '@angular/forms';
import { UpdateDiseasesComponent } from './Views/update-diseases/update-diseases.component';
import { DetailsDiseasesComponent } from './Views/details-diseases/details-diseases.component';
import { PatientComponent } from './patient/patient.component';
import { MedecinListComponent } from './Views/medecin-list/medecin-list.component';
import { MedecinAddComponent } from './Views/medecin-add/medecin-add.component';
import { MedecinEditComponent } from './Views/medecin-edit/medecin-edit.component';
import { SymptomComponent } from './Views/symptom/symptom.component';
import { TipComponent } from './Views/Tip/tip/tip.component';


@NgModule({
  declarations: [ListeLivreComponent, AdherantLayoutComponent, AddDiseasesComponent, UpdateDiseasesComponent, DetailsDiseasesComponent , PatientComponent , MedecinListComponent, MedecinAddComponent, MedecinEditComponent, SymptomComponent, TipComponent, ListTraitementComponent,
    AddTraitementComponent,
    EditTraitementComponent,],
  imports: [CommonModule, AdherentRoutingModule , ReactiveFormsModule ,  FormsModule],
})
export class AdherentModule {}
