import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PermisoComponent } from './list/permiso.component';
import { PermisoDetailComponent } from './detail/permiso-detail.component';
import { PermisoUpdateComponent } from './update/permiso-update.component';
import { PermisoDeleteDialogComponent } from './delete/permiso-delete-dialog.component';
import { PermisoRoutingModule } from './route/permiso-routing.module';

@NgModule({
  imports: [SharedModule, PermisoRoutingModule],
  declarations: [PermisoComponent, PermisoDetailComponent, PermisoUpdateComponent, PermisoDeleteDialogComponent],
})
export class PermisoModule {}
