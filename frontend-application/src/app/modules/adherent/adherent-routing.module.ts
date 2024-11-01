import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdherantLayoutComponent } from './adherant-layout/adherant-layout.component';
import { ListeLivreComponent } from './Views/liste-livre/liste-livre.component';
import { ListTraitementComponent } from './Views/traitement/list-traitement/list-traitement.component';
import { AddTraitementComponent } from './Views/traitement/add-traitement/add-traitement.component';
import { EditTraitementComponent } from './Views/traitement/edit-traitement/edit-traitement.component';

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
        path: 'liste-traitement',
        component: ListTraitementComponent,
      },
      {
        path: 'add-traitement',
        component: AddTraitementComponent,
      },
      {
        path: 'edit-traitement/:name',
        component: EditTraitementComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdherentRoutingModule {}
