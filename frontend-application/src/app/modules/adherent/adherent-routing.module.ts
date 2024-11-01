import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdherantLayoutComponent } from './adherant-layout/adherant-layout.component';
import { ListeLivreComponent } from './Views/liste-livre/liste-livre.component';
import { MedecinListComponent } from './Views/medecin-list/medecin-list.component';
import { MedecinAddComponent } from './Views/medecin-add/medecin-add.component';
import { MedecinEditComponent } from './Views/medecin-edit/medecin-edit.component';

const routes: Routes = [
  {
    path: '',
    component: AdherantLayoutComponent,
    children: [
      {
        path: 'Liste-livre',
        component: ListeLivreComponent,
      },
      { path: 'medecins', component: MedecinListComponent },
      { path: 'medecins/add', component: MedecinAddComponent},
      { path: 'medecin/update/:name', component: MedecinEditComponent},

    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdherentRoutingModule {}
