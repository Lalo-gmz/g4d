import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProyecto } from '../proyecto.model';

@Component({
  selector: 'jhi-proyecto-detail',
  templateUrl: './proyecto-detail.component.html',
})
export class ProyectoDetailComponent implements OnInit {
  proyecto: IProyecto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proyecto }) => {
      this.proyecto = proyecto;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
