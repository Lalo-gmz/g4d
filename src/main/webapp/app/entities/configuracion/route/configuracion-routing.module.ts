import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConfiguracionComponent } from '../list/configuracion.component';
import { ConfiguracionDetailComponent } from '../detail/configuracion-detail.component';
import { ConfiguracionUpdateComponent } from '../update/configuracion-update.component';
import { ConfiguracionRoutingResolveService } from './configuracion-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const configuracionRoute: Routes = [
  {
    path: '',
    component: ConfiguracionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConfiguracionDetailComponent,
    resolve: {
      configuracion: ConfiguracionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConfiguracionUpdateComponent,
    resolve: {
      configuracion: ConfiguracionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConfiguracionUpdateComponent,
    resolve: {
      configuracion: ConfiguracionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(configuracionRoute)],
  exports: [RouterModule],
})
export class ConfiguracionRoutingModule {}
