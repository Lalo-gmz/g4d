import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EstatusFuncionalidadComponent } from '../list/estatus-funcionalidad.component';
import { EstatusFuncionalidadDetailComponent } from '../detail/estatus-funcionalidad-detail.component';
import { EstatusFuncionalidadUpdateComponent } from '../update/estatus-funcionalidad-update.component';
import { EstatusFuncionalidadRoutingResolveService } from './estatus-funcionalidad-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const estatusFuncionalidadRoute: Routes = [
  {
    path: '',
    component: EstatusFuncionalidadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EstatusFuncionalidadDetailComponent,
    resolve: {
      estatusFuncionalidad: EstatusFuncionalidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EstatusFuncionalidadUpdateComponent,
    resolve: {
      estatusFuncionalidad: EstatusFuncionalidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EstatusFuncionalidadUpdateComponent,
    resolve: {
      estatusFuncionalidad: EstatusFuncionalidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(estatusFuncionalidadRoute)],
  exports: [RouterModule],
})
export class EstatusFuncionalidadRoutingModule {}
