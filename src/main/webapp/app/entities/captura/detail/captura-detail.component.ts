import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICaptura } from '../captura.model';

@Component({
  selector: 'jhi-captura-detail',
  templateUrl: './captura-detail.component.html',
})
export class CapturaDetailComponent implements OnInit {
  captura: ICaptura | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ captura }) => {
      this.captura = captura;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
