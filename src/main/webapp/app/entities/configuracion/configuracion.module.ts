import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConfiguracionComponent } from './list/configuracion.component';
import { ConfiguracionDetailComponent } from './detail/configuracion-detail.component';
import { ConfiguracionUpdateComponent } from './update/configuracion-update.component';
import { ConfiguracionDeleteDialogComponent } from './delete/configuracion-delete-dialog.component';
import { ConfiguracionRoutingModule } from './route/configuracion-routing.module';

@NgModule({
  imports: [SharedModule, ConfiguracionRoutingModule],
  declarations: [ConfiguracionComponent, ConfiguracionDetailComponent, ConfiguracionUpdateComponent, ConfiguracionDeleteDialogComponent],
})
export class ConfiguracionModule {}
