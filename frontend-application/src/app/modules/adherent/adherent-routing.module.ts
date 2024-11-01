import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdherantLayoutComponent } from './adherant-layout/adherant-layout.component';
import { ListeLivreComponent } from './Views/liste-livre/liste-livre.component';
import { AllergiesComponent } from 'src/app/allergies/allergies.component';

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
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdherentRoutingModule {}
