import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EstatusFuncionalidadComponent } from './list/estatus-funcionalidad.component';
import { EstatusFuncionalidadDetailComponent } from './detail/estatus-funcionalidad-detail.component';
import { EstatusFuncionalidadUpdateComponent } from './update/estatus-funcionalidad-update.component';
import { EstatusFuncionalidadDeleteDialogComponent } from './delete/estatus-funcionalidad-delete-dialog.component';
import { EstatusFuncionalidadRoutingModule } from './route/estatus-funcionalidad-routing.module';

@NgModule({
  imports: [SharedModule, EstatusFuncionalidadRoutingModule],
  declarations: [
    EstatusFuncionalidadComponent,
    EstatusFuncionalidadDetailComponent,
    EstatusFuncionalidadUpdateComponent,
    EstatusFuncionalidadDeleteDialogComponent,
  ],
})
export class EstatusFuncionalidadModule {}
