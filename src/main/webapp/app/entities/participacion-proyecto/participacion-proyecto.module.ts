import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ParticipacionProyectoComponent } from './list/participacion-proyecto.component';
import { ParticipacionProyectoDetailComponent } from './detail/participacion-proyecto-detail.component';
import { ParticipacionProyectoUpdateComponent } from './update/participacion-proyecto-update.component';
import { ParticipacionProyectoDeleteDialogComponent } from './delete/participacion-proyecto-delete-dialog.component';
import { ParticipacionProyectoRoutingModule } from './route/participacion-proyecto-routing.module';

@NgModule({
  imports: [SharedModule, ParticipacionProyectoRoutingModule],
  declarations: [
    ParticipacionProyectoComponent,
    ParticipacionProyectoDetailComponent,
    ParticipacionProyectoUpdateComponent,
    ParticipacionProyectoDeleteDialogComponent,
  ],
})
export class ParticipacionProyectoModule {}
