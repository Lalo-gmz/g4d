import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AtributoComponent } from './list/atributo.component';
import { AtributoDetailComponent } from './detail/atributo-detail.component';
import { AtributoUpdateComponent } from './update/atributo-update.component';
import { AtributoDeleteDialogComponent } from './delete/atributo-delete-dialog.component';
import { AtributoRoutingModule } from './route/atributo-routing.module';

@NgModule({
  imports: [SharedModule, AtributoRoutingModule],
  declarations: [AtributoComponent, AtributoDetailComponent, AtributoUpdateComponent, AtributoDeleteDialogComponent],
})
export class AtributoModule {}
