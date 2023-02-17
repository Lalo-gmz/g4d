import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAtributoFuncionalidad } from '../atributo-funcionalidad.model';

@Component({
  selector: 'jhi-atributo-funcionalidad-detail',
  templateUrl: './atributo-funcionalidad-detail.component.html',
})
export class AtributoFuncionalidadDetailComponent implements OnInit {
  atributoFuncionalidad: IAtributoFuncionalidad | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ atributoFuncionalidad }) => {
      this.atributoFuncionalidad = atributoFuncionalidad;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
