import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdherentRoutingModule } from './adherent-routing.module';
import { ListeLivreComponent } from './Views/liste-livre/liste-livre.component';
import { AdherantLayoutComponent } from './adherant-layout/adherant-layout.component';
import { PatientComponent } from './patient/patient.component';
import { FormsModule } from '@angular/forms';  // <-- Import FormsModule


@NgModule({
  declarations: [ListeLivreComponent, AdherantLayoutComponent,PatientComponent],
  imports: [CommonModule, AdherentRoutingModule,FormsModule],
})
export class AdherentModule {}
