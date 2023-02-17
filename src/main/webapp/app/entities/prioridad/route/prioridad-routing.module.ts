import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrioridadComponent } from '../list/prioridad.component';
import { PrioridadDetailComponent } from '../detail/prioridad-detail.component';
import { PrioridadUpdateComponent } from '../update/prioridad-update.component';
import { PrioridadRoutingResolveService } from './prioridad-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const prioridadRoute: Routes = [
  {
    path: '',
    component: PrioridadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrioridadDetailComponent,
    resolve: {
      prioridad: PrioridadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrioridadUpdateComponent,
    resolve: {
      prioridad: PrioridadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrioridadUpdateComponent,
    resolve: {
      prioridad: PrioridadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prioridadRoute)],
  exports: [RouterModule],
})
export class PrioridadRoutingModule {}
