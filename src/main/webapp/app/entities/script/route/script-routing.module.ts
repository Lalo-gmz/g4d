import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ScriptComponent } from '../list/script.component';
import { ScriptDetailComponent } from '../detail/script-detail.component';
import { ScriptUpdateComponent } from '../update/script-update.component';
import { ScriptRoutingResolveService } from './script-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const scriptRoute: Routes = [
  {
    path: 'proyecto/:id',
    component: ScriptComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ScriptDetailComponent,
    resolve: {
      script: ScriptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ScriptUpdateComponent,
    resolve: {
      script: ScriptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ScriptUpdateComponent,
    resolve: {
      script: ScriptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(scriptRoute)],
  exports: [RouterModule],
})
export class ScriptRoutingModule {}
