import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFuncionalidad } from '../funcionalidad.model';

@Component({
  selector: 'jhi-funcionalidad-detail',
  templateUrl: './funcionalidad-detail.component.html',
})
export class FuncionalidadDetailComponent implements OnInit {
  funcionalidad: IFuncionalidad | null = null;
  GITURL?: string = 'https://gitlab.com/g4dadmin/';
  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ funcionalidad }) => {
      this.funcionalidad = funcionalidad;
    });
  }

  convertToSlugURL(text: string) {
    return this.GITURL + text.toLowerCase().replace(/\s+/g, '-');
  }

  // asignar la función a una propiedad del componente
  slugify = this.convertToSlugURL.bind(this);

  previousState(): void {
    window.history.back();
  }
}
