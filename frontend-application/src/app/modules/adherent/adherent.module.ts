import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdherentRoutingModule } from './adherent-routing.module';
import { ListeLivreComponent } from './Views/liste-livre/liste-livre.component';
import { AdherantLayoutComponent } from './adherant-layout/adherant-layout.component';
import { MedecinListComponent } from './Views/medecin-list/medecin-list.component';
import { MedecinAddComponent } from './Views/medecin-add/medecin-add.component';
import { FormsModule } from '@angular/forms';
import { MedecinEditComponent } from './Views/medecin-edit/medecin-edit.component';

@NgModule({
  declarations: [ListeLivreComponent, AdherantLayoutComponent, MedecinListComponent, MedecinAddComponent, MedecinEditComponent],
  imports: [CommonModule, AdherentRoutingModule,FormsModule],
})
export class AdherentModule {}
