import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FuncionalidadComponent } from '../list/funcionalidad.component';
import { FuncionalidadDetailComponent } from '../detail/funcionalidad-detail.component';
import { FuncionalidadUpdateComponent } from '../update/funcionalidad-update.component';
import { FuncionalidadRoutingResolveService } from './funcionalidad-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const funcionalidadRoute: Routes = [
  {
    path: 'proyecto/:proyectoId/iteracion/:id',
    component: FuncionalidadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: ':id/view',
    component: FuncionalidadDetailComponent,
    resolve: {
      funcionalidad: FuncionalidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FuncionalidadUpdateComponent,
    resolve: {
      funcionalidad: FuncionalidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FuncionalidadUpdateComponent,
    resolve: {
      funcionalidad: FuncionalidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(funcionalidadRoute)],
  exports: [RouterModule],
})
export class FuncionalidadRoutingModule {}
