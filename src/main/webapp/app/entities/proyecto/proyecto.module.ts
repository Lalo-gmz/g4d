import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProyectoComponent } from './list/proyecto.component';
import { ProyectoDetailComponent } from './detail/proyecto-detail.component';
import { ProyectoUpdateComponent } from './update/proyecto-update.component';
import { ProyectoDeleteDialogComponent } from './delete/proyecto-delete-dialog.component';
import { ProyectoRoutingModule } from './route/proyecto-routing.module';

@NgModule({
  imports: [SharedModule, ProyectoRoutingModule],
  declarations: [ProyectoComponent, ProyectoDetailComponent, ProyectoUpdateComponent, ProyectoDeleteDialogComponent],
})
export class ProyectoModule {}
