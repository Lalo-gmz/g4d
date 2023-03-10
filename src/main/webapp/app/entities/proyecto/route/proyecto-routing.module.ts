import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProyectoComponent } from '../list/proyecto.component';
import { ProyectoDetailComponent } from '../detail/proyecto-detail.component';
import { ProyectoUpdateComponent } from '../update/proyecto-update.component';
import { ProyectoRoutingResolveService } from './proyecto-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { ProyectoListByUserComponent } from '../list-by-user/proyecto-list-by-user.component';

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
    path: 'mis-proyectos',
    component: ProyectoListByUserComponent,
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
