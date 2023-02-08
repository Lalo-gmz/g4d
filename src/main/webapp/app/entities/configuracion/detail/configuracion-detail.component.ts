import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfiguracion } from '../configuracion.model';

@Component({
  selector: 'jhi-configuracion-detail',
  templateUrl: './configuracion-detail.component.html',
})
export class ConfiguracionDetailComponent implements OnInit {
  configuracion: IConfiguracion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configuracion }) => {
      this.configuracion = configuracion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
