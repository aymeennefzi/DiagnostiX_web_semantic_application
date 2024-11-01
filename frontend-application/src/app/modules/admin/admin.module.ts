import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';

import { ListeAdherentComponent } from './Views/liste-adherent/liste-adherent.component';
import { ListeTotaleLivreComponent } from './Views/liste-totale-livre/liste-totale-livre.component';
import { AdminLayoutComponent } from './admin-layout/admin-layout.component';

import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    AdminLayoutComponent,
    ListeAdherentComponent,
    ListeTotaleLivreComponent,
   
  ],
  imports: [CommonModule, AdminRoutingModule],
})
export class AdminModule {}
