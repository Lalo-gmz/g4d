import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFuncionalidad } from '../funcionalidad.model';

@Component({
  selector: 'jhi-funcionalidad-detail',
  templateUrl: './funcionalidad-detail.component.html',
})
export class FuncionalidadDetailComponent implements OnInit {
  funcionalidad: IFuncionalidad | null = null;
  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ funcionalidad }) => {
      this.funcionalidad = funcionalidad;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
