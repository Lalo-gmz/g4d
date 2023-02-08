import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AtributoComponent } from '../list/atributo.component';
import { AtributoDetailComponent } from '../detail/atributo-detail.component';
import { AtributoUpdateComponent } from '../update/atributo-update.component';
import { AtributoRoutingResolveService } from './atributo-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const atributoRoute: Routes = [
  {
    path: '',
    component: AtributoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AtributoDetailComponent,
    resolve: {
      atributo: AtributoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AtributoUpdateComponent,
    resolve: {
      atributo: AtributoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AtributoUpdateComponent,
    resolve: {
      atributo: AtributoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(atributoRoute)],
  exports: [RouterModule],
})
export class AtributoRoutingModule {}
