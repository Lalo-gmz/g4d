import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBitacora } from '../bitacora.model';

@Component({
  selector: 'jhi-bitacora-detail',
  templateUrl: './bitacora-detail.component.html',
})
export class BitacoraDetailComponent implements OnInit {
  bitacora: IBitacora | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bitacora }) => {
      this.bitacora = bitacora;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
