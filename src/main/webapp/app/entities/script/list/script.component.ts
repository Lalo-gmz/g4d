import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IScript } from '../script.model';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, ScriptService } from '../service/script.service';
import { ScriptDeleteDialogComponent } from '../delete/script-delete-dialog.component';
import { SortService } from 'app/shared/sort/sort.service';
import { ICaptura } from '../captura.model';
import * as XLSX from 'xlsx';
import { AlertService } from 'app/core/util/alert.service';
//import { FileSaverService } from 'ngx-filesaver';

@Component({
  selector: 'jhi-script',
  templateUrl: './script.component.html',
})
export class ScriptComponent implements OnInit {
  scripts?: IScript[];
  isLoading = false;
  proyectoId?: number;
  capturas?: ICaptura[];

  predicate = 'id';
  ascending = true;

  constructor(
    protected scriptService: ScriptService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal,
    protected alertService: AlertService //private filerSaver: FileSaverService
  ) {
    this.proyectoId = this.activatedRoute.snapshot.params['id'];
  }

  trackId = (_index: number, item: IScript): number => this.scriptService.getScriptIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  ejecutar(script: string, nombrefuncion: string): void {
    this.scriptService.findByProyect(this.proyectoId!).subscribe({
      next: res => {
        if (res.body) {
          console.log(script);
          this.capturas = res.body;
          // console.log(this.capturas);
          const nuevafuncion = new Function('capturas', script);

          //console.log(nuevafuncion)
          const resultado = nuevafuncion(this.capturas);
          console.log(resultado);

          //continuación

          const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8';
          const EXCEL_EXTENSION = '.xlsx';
          //custome code
          const worksheet = XLSX.utils.json_to_sheet(nuevafuncion(this.capturas));
          const workbook = {
            Sheets: {
              Script: worksheet,
            },
            SheetNames: ['Script'],
          };

          const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
          const blobData = new Blob([excelBuffer], { type: EXCEL_TYPE });
          //this.filerSaver.save(blobData, "demoFile")

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

  /*

  obtenerFechas(capturas: ICaptura[]): any{
    const res = [];
    capturas.forEach(element => {
          res.push(element.fecha)
    });
    return res;
  }
*/
  delete(script: IScript): void {
    const modalRef = this.modalService.open(ScriptDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.script = script;
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
    this.scripts = this.refineData(dataFromBody);
  }

  protected refineData(data: IScript[]): IScript[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IScript[] | null): IScript[] {
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject = {
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.scriptService.findAllByProyectoId(queryObject, this.proyectoId).pipe(tap(() => (this.isLoading = false)));
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

  copiarTextoAlPortapapeles(texto: string) {
    navigator.clipboard
      .writeText(texto)
      .then(() => {
        console.log('Texto copiado al portapapeles: ' + texto);
        this.alertService.addAlert({
          type: 'success',
          message: 'Script Copiado',
          timeout: 5000,
        });
      })
      .catch(function (error) {
        console.error('Error al copiar el texto al portapapeles: ', error);
      });
  }
}
