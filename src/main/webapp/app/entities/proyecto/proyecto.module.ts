import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProyectoComponent } from './list/proyecto.component';
import { ProyectoDetailComponent } from './detail/proyecto-detail.component';
import { ProyectoUpdateComponent } from './update/proyecto-update.component';
import { ProyectoDeleteDialogComponent } from './delete/proyecto-delete-dialog.component';
import { ProyectoRoutingModule } from './route/proyecto-routing.module';
import { ProyectoListByUserComponent } from './list-by-user/proyecto-list-by-user.component';
import { ParticipacionComponent } from './participacion/participacion.component';

@NgModule({
  imports: [SharedModule, ProyectoRoutingModule],
  declarations: [
    ProyectoComponent,
    ProyectoDetailComponent,
    ProyectoUpdateComponent,
    ProyectoDeleteDialogComponent,
    ProyectoListByUserComponent,
    ParticipacionComponent,
  ],
})
export class ProyectoModule {}
