import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrioridad } from '../prioridad.model';

@Component({
  selector: 'jhi-prioridad-detail',
  templateUrl: './prioridad-detail.component.html',
})
export class PrioridadDetailComponent implements OnInit {
  prioridad: IPrioridad | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prioridad }) => {
      this.prioridad = prioridad;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
