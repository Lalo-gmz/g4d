import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEstatusFuncionalidad } from '../estatus-funcionalidad.model';

@Component({
  selector: 'jhi-estatus-funcionalidad-detail',
  templateUrl: './estatus-funcionalidad-detail.component.html',
})
export class EstatusFuncionalidadDetailComponent implements OnInit {
  estatusFuncionalidad: IEstatusFuncionalidad | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ estatusFuncionalidad }) => {
      this.estatusFuncionalidad = estatusFuncionalidad;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
