import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ParticipacionProyectoComponent } from '../list/participacion-proyecto.component';
import { ParticipacionProyectoDetailComponent } from '../detail/participacion-proyecto-detail.component';
import { ParticipacionProyectoUpdateComponent } from '../update/participacion-proyecto-update.component';
import { ParticipacionProyectoRoutingResolveService } from './participacion-proyecto-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const participacionProyectoRoute: Routes = [
  {
    path: '',
    component: ParticipacionProyectoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParticipacionProyectoDetailComponent,
    resolve: {
      participacionProyecto: ParticipacionProyectoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParticipacionProyectoUpdateComponent,
    resolve: {
      participacionProyecto: ParticipacionProyectoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParticipacionProyectoUpdateComponent,
    resolve: {
      participacionProyecto: ParticipacionProyectoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(participacionProyectoRoute)],
  exports: [RouterModule],
})
export class ParticipacionProyectoRoutingModule {}
