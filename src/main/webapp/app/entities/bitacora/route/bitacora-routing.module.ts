import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BitacoraComponent } from '../list/bitacora.component';
import { BitacoraDetailComponent } from '../detail/bitacora-detail.component';
import { BitacoraUpdateComponent } from '../update/bitacora-update.component';
import { BitacoraRoutingResolveService } from './bitacora-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const bitacoraRoute: Routes = [
  {
    path: '',
    component: BitacoraComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BitacoraDetailComponent,
    resolve: {
      bitacora: BitacoraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BitacoraUpdateComponent,
    resolve: {
      bitacora: BitacoraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BitacoraUpdateComponent,
    resolve: {
      bitacora: BitacoraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bitacoraRoute)],
  exports: [RouterModule],
})
export class BitacoraRoutingModule {}
