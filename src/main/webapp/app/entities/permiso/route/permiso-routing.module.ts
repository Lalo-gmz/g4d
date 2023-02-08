import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PermisoComponent } from '../list/permiso.component';
import { PermisoDetailComponent } from '../detail/permiso-detail.component';
import { PermisoUpdateComponent } from '../update/permiso-update.component';
import { PermisoRoutingResolveService } from './permiso-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const permisoRoute: Routes = [
  {
    path: '',
    component: PermisoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PermisoDetailComponent,
    resolve: {
      permiso: PermisoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PermisoUpdateComponent,
    resolve: {
      permiso: PermisoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PermisoUpdateComponent,
    resolve: {
      permiso: PermisoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(permisoRoute)],
  exports: [RouterModule],
})
export class PermisoRoutingModule {}
