import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IteracionComponent } from '../list/iteracion.component';
import { IteracionDetailComponent } from '../detail/iteracion-detail.component';
import { IteracionUpdateComponent } from '../update/iteracion-update.component';
import { IteracionRoutingResolveService } from './iteracion-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const iteracionRoute: Routes = [
  {
    path: '',
    component: IteracionComponent,
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
];

@NgModule({
  imports: [RouterModule.forChild(iteracionRoute)],
  exports: [RouterModule],
})
export class IteracionRoutingModule {}
