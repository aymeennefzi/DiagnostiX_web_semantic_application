import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdherentRoutingModule } from './adherent-routing.module';
import { ListeLivreComponent } from './Views/liste-livre/liste-livre.component';
import { AdherantLayoutComponent } from './adherant-layout/adherant-layout.component';
import { ListTraitementComponent } from './Views/traitement/list-traitement/list-traitement.component';
import { AddTraitementComponent } from './Views/traitement/add-traitement/add-traitement.component';
import { ReactiveFormsModule } from '@angular/forms';
import { EditTraitementComponent } from './Views/traitement/edit-traitement/edit-traitement.component';

@NgModule({
  declarations: [ListeLivreComponent, AdherantLayoutComponent, ListTraitementComponent,
    AddTraitementComponent,
    EditTraitementComponent,],
  imports: [CommonModule, AdherentRoutingModule,ReactiveFormsModule],
})
export class AdherentModule {}
