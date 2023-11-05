import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIteracion } from '../iteracion.model';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, IteracionService } from '../service/iteracion.service';
import { IteracionDeleteDialogComponent } from '../delete/iteracion-delete-dialog.component';
import { SortService } from 'app/shared/sort/sort.service';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';
import { AlertService } from 'app/core/util/alert.service';
import { ICaptura } from 'app/entities/script/captura.model';
import * as XLSX from 'xlsx';
import { ScriptService } from 'app/entities/script/service/script.service';
import { IScript } from 'app/entities/script/script.model';

@Component({
  selector: 'jhi-iteracion',
  templateUrl: './iteracion.component.html',
})
export class IteracionComponent implements OnInit {
  iteracions?: IIteracion[];
  isLoading = false;
  isSync = false;
  proyectoId?: number;
  predicate = 'id';
  ascending = true;
  capturas?: ICaptura[];
  scripts?: IScript[];

  nombreProyecto?: any;

  constructor(
    protected iteracionService: IteracionService,
    protected activatedRoute: ActivatedRoute,
    protected proyectoService: ProyectoService,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal,
    protected alertService: AlertService,
    protected scriptService: ScriptService
  ) {
    this.proyectoId = this.activatedRoute.snapshot.params['id'];
  }

  trackId = (_index: number, item: IIteracion): number => this.iteracionService.getIteracionIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  delete(iteracion: IIteracion): void {
    const modalRef = this.modalService.open(IteracionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.iteracion = iteracion;
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

  sincronizar(): void {
    this.isSync = true;
    if (this.proyectoId) {
      this.iteracionService.updateFuncionalidadesByIssuesAtGitLab(this.proyectoId).subscribe({
        next: (res: EntityArrayResponseType) => {
          this.alertService.addAlert({
            type: 'success',
            message: `Se actualizaron ${res.body?.length ?? 0}  Funcionalidades desde GitLab`,
            timeout: 5000,
          });
          this.isSync = false;
        },
      });
    }
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
    if (this.proyectoId) {
      this.proyectoService.find(this.proyectoId).subscribe({
        next: res => {
          if (res.body) {
            this.nombreProyecto = res.body.nombre;
          }
        },
      });

      this.scriptService.findAllByProyectoId(null, this.proyectoId).subscribe({
        next: res => {
          if (res.body) {
            this.scripts = res.body;
          }
        },
      });
    }
  }

  descargarExcel(proyectoId: number): void {
    this.iteracionService.exportarExcel(proyectoId).subscribe({
      next: (res: any) => {
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        document.body.appendChild(a);
        a.style.display = 'none';
        a.href = url;
        a.download = `Proyecto_${proyectoId}.xlsx`;
        a.click();
        window.URL.revokeObjectURL(url);
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
    this.iteracions = this.refineData(dataFromBody);
  }

  protected refineData(data: IIteracion[]): IIteracion[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IIteracion[] | null): IIteracion[] {
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject = {
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.iteracionService.query(this.proyectoId, queryObject).pipe(tap(() => (this.isLoading = false)));
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

  previousState(): void {
    window.history.back();
  }

  ejecutar(script: string, nombrefuncion: string): void {
    this.scriptService.findByProyect(this.proyectoId!).subscribe({
      next: res => {
        if (res.body) {
          this.capturas = res.body;
          const nuevafuncion = new Function('capturas', script);

          nuevafuncion(this.capturas);

          //continuación

          const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8';
          const worksheet = XLSX.utils.json_to_sheet(nuevafuncion(this.capturas));
          const workbook = {
            Sheets: {
              Script: worksheet,
            },
            SheetNames: ['Script'],
          };

          const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
          const blobData = new Blob([excelBuffer], { type: EXCEL_TYPE });

          const blobUrl = URL.createObjectURL(blobData);

          // Crear un enlace <a> para descargar el archivo
          const link = document.createElement('a');
          link.href = blobUrl;
          link.download = nombrefuncion + '_g4d.xlsx'; // Establece el nombre de archivo que deseas para la descarga

          // Simular un clic en el enlace para iniciar la descarga
          link.click();

          // Liberar la URL temporal
          URL.revokeObjectURL(blobUrl);
        }
      },
    });
  }
}
