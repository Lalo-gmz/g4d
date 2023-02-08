import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProyectoComponent } from '../list/proyecto.component';
import { ProyectoDetailComponent } from '../detail/proyecto-detail.component';
import { ProyectoUpdateComponent } from '../update/proyecto-update.component';
import { ProyectoRoutingResolveService } from './proyecto-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const proyectoRoute: Routes = [
  {
    path: '',
    component: ProyectoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProyectoDetailComponent,
    resolve: {
      proyecto: ProyectoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProyectoUpdateComponent,
    resolve: {
      proyecto: ProyectoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProyectoUpdateComponent,
    resolve: {
      proyecto: ProyectoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(proyectoRoute)],
  exports: [RouterModule],
})
export class ProyectoRoutingModule {}
