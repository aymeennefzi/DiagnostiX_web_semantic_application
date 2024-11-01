import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdherantLayoutComponent } from './adherant-layout/adherant-layout.component';
import { ListeLivreComponent } from './Views/liste-livre/liste-livre.component';
import { AddDiseasesComponent } from './Views/add-diseases/add-diseases.component';
import { UpdateDiseasesComponent } from './Views/update-diseases/update-diseases.component';
import { DetailsDiseasesComponent } from './Views/details-diseases/details-diseases.component';
import { PatientComponent } from './patient/patient.component';

const routes: Routes = [
  {
    path: '',
    component: AdherantLayoutComponent,
    children: [
      {path: 'Liste-livre',component: ListeLivreComponent},
      {path: 'Liste-livre/addDisease',component: AddDiseasesComponent},
      { path: 'Liste-livre/UpdateDisease/:name', component: UpdateDiseasesComponent },
      { path: 'Liste-livre/DetailDisease/:name', component: DetailsDiseasesComponent },
      {path: 'Liste-livre',component: ListeLivreComponent},
      {path: 'patients',component: PatientComponent }
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdherentRoutingModule {}
