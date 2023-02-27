import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CapturaComponent } from './list/captura.component';
import { CapturaDetailComponent } from './detail/captura-detail.component';
import { CapturaUpdateComponent } from './update/captura-update.component';
import { CapturaDeleteDialogComponent } from './delete/captura-delete-dialog.component';
import { CapturaRoutingModule } from './route/captura-routing.module';

@NgModule({
  imports: [SharedModule, CapturaRoutingModule],
  declarations: [CapturaComponent, CapturaDetailComponent, CapturaUpdateComponent, CapturaDeleteDialogComponent],
})
export class CapturaModule {}
