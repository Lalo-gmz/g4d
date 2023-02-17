import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AtributoFuncionalidadComponent } from './list/atributo-funcionalidad.component';
import { AtributoFuncionalidadDetailComponent } from './detail/atributo-funcionalidad-detail.component';
import { AtributoFuncionalidadUpdateComponent } from './update/atributo-funcionalidad-update.component';
import { AtributoFuncionalidadDeleteDialogComponent } from './delete/atributo-funcionalidad-delete-dialog.component';
import { AtributoFuncionalidadRoutingModule } from './route/atributo-funcionalidad-routing.module';

@NgModule({
  imports: [SharedModule, AtributoFuncionalidadRoutingModule],
  declarations: [
    AtributoFuncionalidadComponent,
    AtributoFuncionalidadDetailComponent,
    AtributoFuncionalidadUpdateComponent,
    AtributoFuncionalidadDeleteDialogComponent,
  ],
})
export class AtributoFuncionalidadModule {}
