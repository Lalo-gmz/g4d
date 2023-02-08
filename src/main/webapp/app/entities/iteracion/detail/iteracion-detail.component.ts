import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIteracion } from '../iteracion.model';

@Component({
  selector: 'jhi-iteracion-detail',
  templateUrl: './iteracion-detail.component.html',
})
export class IteracionDetailComponent implements OnInit {
  iteracion: IIteracion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ iteracion }) => {
      this.iteracion = iteracion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
