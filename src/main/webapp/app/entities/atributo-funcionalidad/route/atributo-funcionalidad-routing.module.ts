import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AtributoFuncionalidadComponent } from '../list/atributo-funcionalidad.component';
import { AtributoFuncionalidadDetailComponent } from '../detail/atributo-funcionalidad-detail.component';
import { AtributoFuncionalidadUpdateComponent } from '../update/atributo-funcionalidad-update.component';
import { AtributoFuncionalidadRoutingResolveService } from './atributo-funcionalidad-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const atributoFuncionalidadRoute: Routes = [
  {
    path: '',
    component: AtributoFuncionalidadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AtributoFuncionalidadDetailComponent,
    resolve: {
      atributoFuncionalidad: AtributoFuncionalidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AtributoFuncionalidadUpdateComponent,
    resolve: {
      atributoFuncionalidad: AtributoFuncionalidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AtributoFuncionalidadUpdateComponent,
    resolve: {
      atributoFuncionalidad: AtributoFuncionalidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(atributoFuncionalidadRoute)],
  exports: [RouterModule],
})
export class AtributoFuncionalidadRoutingModule {}
