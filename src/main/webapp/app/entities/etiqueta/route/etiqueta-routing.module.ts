import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EtiquetaComponent } from '../list/etiqueta.component';
import { EtiquetaDetailComponent } from '../detail/etiqueta-detail.component';
import { EtiquetaUpdateComponent } from '../update/etiqueta-update.component';
import { EtiquetaRoutingResolveService } from './etiqueta-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const etiquetaRoute: Routes = [
  {
    path: '',
    component: EtiquetaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EtiquetaDetailComponent,
    resolve: {
      etiqueta: EtiquetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new/funcionalidad/:idFuncionalidad',
    component: EtiquetaUpdateComponent,
    resolve: {
      etiqueta: EtiquetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EtiquetaUpdateComponent,
    resolve: {
      etiqueta: EtiquetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(etiquetaRoute)],
  exports: [RouterModule],
})
export class EtiquetaRoutingModule {}
