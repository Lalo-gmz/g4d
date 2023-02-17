import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'bitacora',
        data: { pageTitle: 'Bitacoras' },
        loadChildren: () => import('./bitacora/bitacora.module').then(m => m.BitacoraModule),
      },
      {
        path: 'iteracion',
        data: { pageTitle: 'Iteracions' },
        loadChildren: () => import('./iteracion/iteracion.module').then(m => m.IteracionModule),
      },
      {
        path: 'proyecto',
        data: { pageTitle: 'Proyectos' },
        loadChildren: () => import('./proyecto/proyecto.module').then(m => m.ProyectoModule),
      },
      {
        path: 'estatus-funcionalidad',
        data: { pageTitle: 'EstatusFuncionalidads' },
        loadChildren: () => import('./estatus-funcionalidad/estatus-funcionalidad.module').then(m => m.EstatusFuncionalidadModule),
      },
      {
        path: 'etiqueta',
        data: { pageTitle: 'Etiquetas' },
        loadChildren: () => import('./etiqueta/etiqueta.module').then(m => m.EtiquetaModule),
      },
      {
        path: 'funcionalidad',
        data: { pageTitle: 'Funcionalidads' },
        loadChildren: () => import('./funcionalidad/funcionalidad.module').then(m => m.FuncionalidadModule),
      },
      {
        path: 'prioridad',
        data: { pageTitle: 'Prioridads' },
        loadChildren: () => import('./prioridad/prioridad.module').then(m => m.PrioridadModule),
      },
      {
        path: 'comentario',
        data: { pageTitle: 'Comentarios' },
        loadChildren: () => import('./comentario/comentario.module').then(m => m.ComentarioModule),
      },
      {
        path: 'atributo',
        data: { pageTitle: 'Atributos' },
        loadChildren: () => import('./atributo/atributo.module').then(m => m.AtributoModule),
      },
      {
        path: 'atributo-funcionalidad',
        data: { pageTitle: 'AtributoFuncionalidads' },
        loadChildren: () => import('./atributo-funcionalidad/atributo-funcionalidad.module').then(m => m.AtributoFuncionalidadModule),
      },
      {
        path: 'configuracion',
        data: { pageTitle: 'Configuracions' },
        loadChildren: () => import('./configuracion/configuracion.module').then(m => m.ConfiguracionModule),
      },
      {
        path: 'rol',
        data: { pageTitle: 'Rols' },
        loadChildren: () => import('./rol/rol.module').then(m => m.RolModule),
      },
      {
        path: 'participacion-proyecto',
        data: { pageTitle: 'ParticipacionProyectos' },
        loadChildren: () => import('./participacion-proyecto/participacion-proyecto.module').then(m => m.ParticipacionProyectoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
