import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBitacora } from '../bitacora.model';
import { SortService } from 'app/shared/sort/sort.service';
import { BitacoraService } from '../service/bitacora.service';

@Component({
  selector: 'jhi-bitacora-by-funcionalidad',
  templateUrl: './bitacora.component.html',
})
export class BitacoraByFuncionalidadComponent implements OnInit {
  bitacoras?: IBitacora[];

  @Input() funcionalidadId: number = 0;

  constructor(
    protected bitacoraService: BitacoraService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.bitacoraService.findAllByFuncionalidad(this.funcionalidadId).subscribe({
      next: res => {
        this.bitacoras = res.body ?? [];
      },
    });
  }
}
