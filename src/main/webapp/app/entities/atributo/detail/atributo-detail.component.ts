import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAtributo } from '../atributo.model';

@Component({
  selector: 'jhi-atributo-detail',
  templateUrl: './atributo-detail.component.html',
})
export class AtributoDetailComponent implements OnInit {
  atributo: IAtributo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ atributo }) => {
      this.atributo = atributo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
