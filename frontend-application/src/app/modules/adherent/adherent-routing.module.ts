import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdherantLayoutComponent } from './adherant-layout/adherant-layout.component';
import { ListeLivreComponent } from './Views/liste-livre/liste-livre.component';

const routes: Routes = [
  {
    path: '',
    component: AdherantLayoutComponent,
    children: [
      {
        path: 'Liste-livre',
        component: ListeLivreComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdherentRoutingModule {}
