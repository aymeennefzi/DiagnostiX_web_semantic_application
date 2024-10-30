import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdherentRoutingModule } from './adherent-routing.module';
import { ListeLivreComponent } from './Views/liste-livre/liste-livre.component';
import { AdherantLayoutComponent } from './adherant-layout/adherant-layout.component';

@NgModule({
  declarations: [ListeLivreComponent, AdherantLayoutComponent],
  imports: [CommonModule, AdherentRoutingModule],
})
export class AdherentModule {}
