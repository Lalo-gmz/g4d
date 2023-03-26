import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IteracionComponent } from '../list/iteracion.component';
import { IteracionDetailComponent } from '../detail/iteracion-detail.component';
import { IteracionUpdateComponent } from '../update/iteracion-update.component';
import { IteracionRoutingResolveService } from './iteracion-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { ProyectoRoutingResolveService } from 'app/entities/proyecto/route/proyecto-routing-resolve.service';
import { ImportarComponentComponent } from '../importar/importar-component.component';

const iteracionRoute: Routes = [
  {
    path: 'proyecto/:id',
    component: IteracionComponent,
    resolve: {
      iteracion: ProyectoRoutingResolveService,
    },
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IteracionDetailComponent,
    resolve: {
      iteracion: IteracionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IteracionUpdateComponent,
    resolve: {
      iteracion: IteracionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IteracionUpdateComponent,
    resolve: {
      iteracion: IteracionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'importar/proyecto/:id',
    component: ImportarComponentComponent,
    resolve: {
      iteracion: IteracionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(iteracionRoute)],
  exports: [RouterModule],
})
export class IteracionRoutingModule {}
