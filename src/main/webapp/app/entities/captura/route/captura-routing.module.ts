import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CapturaComponent } from '../list/captura.component';
import { CapturaDetailComponent } from '../detail/captura-detail.component';
import { CapturaUpdateComponent } from '../update/captura-update.component';
import { CapturaRoutingResolveService } from './captura-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const capturaRoute: Routes = [
  {
    path: '',
    component: CapturaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CapturaDetailComponent,
    resolve: {
      captura: CapturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CapturaUpdateComponent,
    resolve: {
      captura: CapturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CapturaUpdateComponent,
    resolve: {
      captura: CapturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(capturaRoute)],
  exports: [RouterModule],
})
export class CapturaRoutingModule {}
