import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminLayoutComponent } from './admin-layout/admin-layout.component';
import { ListeAdherentComponent } from './Views/liste-adherent/liste-adherent.component';
import { ListeTotaleLivreComponent } from './Views/liste-totale-livre/liste-totale-livre.component';

const routes: Routes = [
  {
    path: '',
    component: AdminLayoutComponent,
    children: [
      {
        path: 'liste-adhrents',
        component: ListeAdherentComponent,
      },
      {
        path: 'liste-totale-livre',
        component: ListeTotaleLivreComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminRoutingModule {}
