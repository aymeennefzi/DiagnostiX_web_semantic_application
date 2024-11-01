import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdherantLayoutComponent } from './adherant-layout/adherant-layout.component';
import { ListeLivreComponent } from './Views/liste-livre/liste-livre.component';
import { AllergiesComponent } from 'src/app/allergies/allergies.component';
import { AddDiseasesComponent } from './Views/add-diseases/add-diseases.component';
import { UpdateDiseasesComponent } from './Views/update-diseases/update-diseases.component';
import { DetailsDiseasesComponent } from './Views/details-diseases/details-diseases.component';
import { MedecinListComponent } from './Views/medecin-list/medecin-list.component';
import { MedecinAddComponent } from './Views/medecin-add/medecin-add.component';
import { MedecinEditComponent } from './Views/medecin-edit/medecin-edit.component';
import { PatientComponent } from './patient/patient.component';
import { SymptomComponent } from './Views/symptom/symptom.component';
import { TipComponent } from './Views/Tip/tip/tip.component';

const routes: Routes = [
  {
    path: '',
    component: AdherantLayoutComponent,
    children: [
      {
        path: 'Liste-livre',
        component: ListeLivreComponent,
      },
      {
        path: 'Liste-Allergy',
        component: AllergiesComponent,
      },
      {path: 'Liste-livre',component: ListeLivreComponent},
      {path: 'Liste-livre/addDisease',component: AddDiseasesComponent},
      {path: 'Liste-livre/UpdateDisease/:name', component: UpdateDiseasesComponent },
      {path: 'Liste-livre/DetailDisease/:name', component: DetailsDiseasesComponent },
      {path: 'Liste-livre',component: ListeLivreComponent},
      {path: 'patients',component: PatientComponent },
      {path: 'symptome',component: SymptomComponent },
      {path: 'Liste-livre',component: ListeLivreComponent},
      {path: 'medecins', component: MedecinListComponent },
      {path: 'medecins/add', component: MedecinAddComponent},
      {path: 'medecin/update/:name', component: MedecinEditComponent},
      {path: 'patients', component: PatientComponent},
      {path: 'tips', component: TipComponent},
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdherentRoutingModule {}
