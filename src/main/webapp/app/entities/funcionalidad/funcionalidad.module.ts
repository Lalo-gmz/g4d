import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FuncionalidadComponent } from './list/funcionalidad.component';
import { FuncionalidadDetailComponent } from './detail/funcionalidad-detail.component';
import { FuncionalidadUpdateComponent } from './update/funcionalidad-update.component';
import { FuncionalidadDeleteDialogComponent } from './delete/funcionalidad-delete-dialog.component';
import { FuncionalidadRoutingModule } from './route/funcionalidad-routing.module';
import { ComentarioListByIdComponent } from '../comentario/list-by-id/list-by-id.component';
import { AtributoFuncionalidadListByFuncionIdComponent } from '../atributo-funcionalidad/atributo-funcionalidad-list-by-funcion-id/atributo-funcionalidad-list-by-funcion-id.component';
import { BitacoraByFuncionalidadComponent } from '../bitacora/list-by-funcionalidad/bitacora.component';

@NgModule({
  imports: [SharedModule, FuncionalidadRoutingModule],
  declarations: [
    FuncionalidadComponent,
    FuncionalidadDetailComponent,
    FuncionalidadUpdateComponent,
    FuncionalidadDeleteDialogComponent,
    ComentarioListByIdComponent,
    AtributoFuncionalidadListByFuncionIdComponent,
    BitacoraByFuncionalidadComponent,
  ],
})
export class FuncionalidadModule {}
