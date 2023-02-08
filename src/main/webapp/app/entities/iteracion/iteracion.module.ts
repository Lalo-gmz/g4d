import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IteracionComponent } from './list/iteracion.component';
import { IteracionDetailComponent } from './detail/iteracion-detail.component';
import { IteracionUpdateComponent } from './update/iteracion-update.component';
import { IteracionDeleteDialogComponent } from './delete/iteracion-delete-dialog.component';
import { IteracionRoutingModule } from './route/iteracion-routing.module';

@NgModule({
  imports: [SharedModule, IteracionRoutingModule],
  declarations: [IteracionComponent, IteracionDetailComponent, IteracionUpdateComponent, IteracionDeleteDialogComponent],
})
export class IteracionModule {}
