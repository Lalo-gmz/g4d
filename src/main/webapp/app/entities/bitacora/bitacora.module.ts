import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BitacoraComponent } from './list/bitacora.component';
import { BitacoraDetailComponent } from './detail/bitacora-detail.component';
import { BitacoraUpdateComponent } from './update/bitacora-update.component';
import { BitacoraDeleteDialogComponent } from './delete/bitacora-delete-dialog.component';
import { BitacoraRoutingModule } from './route/bitacora-routing.module';

@NgModule({
  imports: [SharedModule, BitacoraRoutingModule],
  declarations: [BitacoraComponent, BitacoraDetailComponent, BitacoraUpdateComponent, BitacoraDeleteDialogComponent],
})
export class BitacoraModule {}
