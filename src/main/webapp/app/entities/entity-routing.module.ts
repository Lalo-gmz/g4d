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
        path: 'usuario',
        data: { pageTitle: 'Usuarios' },
        loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule),
      },
      {
        path: 'iteracion',
        data: { pageTitle: 'Iteracions' },
        loadChildren: () => import('./iteracion/iteracion.module').then(m => m.IteracionModule),
      },
      {
        path: 'permiso',
        data: { pageTitle: 'Permisos' },
        loadChildren: () => import('./permiso/permiso.module').then(m => m.PermisoModule),
      },
      {
        path: 'rol',
        data: { pageTitle: 'Rols' },
        loadChildren: () => import('./rol/rol.module').then(m => m.RolModule),
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
        path: 'configuracion',
        data: { pageTitle: 'Configuracions' },
        loadChildren: () => import('./configuracion/configuracion.module').then(m => m.ConfiguracionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
