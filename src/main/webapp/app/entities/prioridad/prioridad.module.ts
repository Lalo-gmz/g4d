import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrioridadComponent } from './list/prioridad.component';
import { PrioridadDetailComponent } from './detail/prioridad-detail.component';
import { PrioridadUpdateComponent } from './update/prioridad-update.component';
import { PrioridadDeleteDialogComponent } from './delete/prioridad-delete-dialog.component';
import { PrioridadRoutingModule } from './route/prioridad-routing.module';

@NgModule({
  imports: [SharedModule, PrioridadRoutingModule],
  declarations: [PrioridadComponent, PrioridadDetailComponent, PrioridadUpdateComponent, PrioridadDeleteDialogComponent],
})
export class PrioridadModule {}
