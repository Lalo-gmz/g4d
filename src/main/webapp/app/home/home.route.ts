import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProyectoListByUserComponent } from 'app/entities/proyecto/list-by-user/proyecto-list-by-user.component';

export const HOME_ROUTE: Route = {
  path: '',
  component: ProyectoListByUserComponent,
  data: {
    pageTitle: '¡Bienvenido, Java Hipster!',
  },
  canActivate: [UserRouteAccessService],
};
