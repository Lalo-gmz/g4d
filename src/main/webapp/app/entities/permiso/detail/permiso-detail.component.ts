import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPermiso } from '../permiso.model';

@Component({
  selector: 'jhi-permiso-detail',
  templateUrl: './permiso-detail.component.html',
})
export class PermisoDetailComponent implements OnInit {
  permiso: IPermiso | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permiso }) => {
      this.permiso = permiso;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
