import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FuncionalidadComponent } from './list/funcionalidad.component';
import { FuncionalidadDetailComponent } from './detail/funcionalidad-detail.component';
import { FuncionalidadUpdateComponent } from './update/funcionalidad-update.component';
import { FuncionalidadDeleteDialogComponent } from './delete/funcionalidad-delete-dialog.component';
import { FuncionalidadRoutingModule } from './route/funcionalidad-routing.module';

@NgModule({
  imports: [SharedModule, FuncionalidadRoutingModule],
  declarations: [FuncionalidadComponent, FuncionalidadDetailComponent, FuncionalidadUpdateComponent, FuncionalidadDeleteDialogComponent],
})
export class FuncionalidadModule {}
