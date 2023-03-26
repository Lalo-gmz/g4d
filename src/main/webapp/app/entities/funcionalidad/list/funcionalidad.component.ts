import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFuncionalidad } from '../funcionalidad.model';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, FuncionalidadService } from '../service/funcionalidad.service';
import { FuncionalidadDeleteDialogComponent } from '../delete/funcionalidad-delete-dialog.component';
import { SortService } from 'app/shared/sort/sort.service';

@Component({
  selector: 'jhi-funcionalidad',
  templateUrl: './funcionalidad.component.html',
  styleUrls: ['./funcionalidad.component.scss'],
})
export class FuncionalidadComponent implements OnInit {
  funcionalidads?: IFuncionalidad[];
  funcPorEstatus?: Record<string, IFuncionalidad[]>;
  keysEstatus?: string[];

  proyectoId?: number;

  tab: number = 3;
  isLoading = false;
  iteracionId?: number;
  predicate = 'id';
  ascending = true;

  constructor(
    protected funcionalidadService: FuncionalidadService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal
  ) {
    this.iteracionId = this.activatedRoute.snapshot.params['id'];
    this.proyectoId = this.activatedRoute.snapshot.params['proyectoId'];
  }

  trackId = (_index: number, item: IFuncionalidad): number => this.funcionalidadService.getFuncionalidadIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  delete(funcionalidad: IFuncionalidad): void {
    const modalRef = this.modalService.open(FuncionalidadDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.funcionalidad = funcionalidad;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.predicate, this.ascending))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.funcionalidads = this.refineData(dataFromBody);
    this.ordenarPorEstatusOrden();
  }

  protected refineData(data: IFuncionalidad[]): IFuncionalidad[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IFuncionalidad[] | null): IFuncionalidad[] {
    console.log(data);
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject = {
      eagerload: true,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.funcionalidadService.query(this.iteracionId, queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

  protected ordenarPorEstatusOrden(): void {
    if (this.funcionalidads) {
      this.funcionalidads = this.funcionalidads.sort(this.compararFuncionalidad);
    }
  }

  compararFuncionalidad(a: any, b: any): number {
    if (a.estatusFuncionalidad?.orden < b.estatusFuncionalidad?.orden) {
      return -1;
    }
    if (a.estatusFuncionalidad?.orden > b.estatusFuncionalidad?.orden) {
      return 1;
    }
    return 0;
  }

  protected getContrastColor(hexColor: any): string {
    // Convertir el color exadecimal a un número entero
    const color = parseInt(hexColor.replace('#', ''), 16);

    // Calcular el brillo del color (usando la fórmula YIQ)
    // eslint-disable-next-line no-bitwise
    const brightness = ((color >> 16) & 0xff) * 0.299 + ((color >> 8) & 0xff) * 0.587 + (color & 0xff) * 0.114;

    // Devolver el color blanco o negro, dependiendo del brillo
    return brightness > 128 ? '#000000' : '#FFFFFF';
  }

  previousState(): void {
    window.history.back();
  }

  //  kanban
  /*
  loadKanban(): void {
    if (this.funcionalidads) {
      this.funcPorEstatus = this.funcionalidads.reduce((acumulador: any, funcionalidad) => {
        const estatusFuncionalidad = funcionalidad.estatusFuncionalidad?.nombre ?? 'none';
        acumulador[estatusFuncionalidad] = acumulador[estatusFuncionalidad] || [];
        acumulador[estatusFuncionalidad].push(funcionalidad);
        return acumulador;
      }, {});

      this.keysEstatus = this.funcPorEstatus ? Object.keys(this.funcPorEstatus) : [];

      console.log(this.funcPorEstatus);
    }
  }
  */
}
