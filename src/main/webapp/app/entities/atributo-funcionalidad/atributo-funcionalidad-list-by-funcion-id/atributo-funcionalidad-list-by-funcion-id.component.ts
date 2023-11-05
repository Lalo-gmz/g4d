import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IAtributoFuncionalidad } from '../atributo-funcionalidad.model';
import { AtributoFuncionalidadService } from '../service/atributo-funcionalidad.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AtributoFuncionalidadDeleteDialogComponent } from '../delete/atributo-funcionalidad-delete-dialog.component';

@Component({
  selector: 'jhi-atributo-funcionalidad-list-by-funcion-id',
  templateUrl: './atributo-funcionalidad-list-by-funcion-id.component.html',
  styleUrls: ['./atributo-funcionalidad-list-by-funcion-id.component.scss'],
})
export class AtributoFuncionalidadListByFuncionIdComponent implements OnInit {
  @Input() destacados: boolean | false | undefined;

  atributosFuncionalidads?: IAtributoFuncionalidad[];

  funcionId: number = 0;
  constructor(
    protected atributoFuncionalidadService: AtributoFuncionalidadService,
    private route: ActivatedRoute,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      // eslint-disable-next-line radix
      const id = parseInt(params['id']);
      this.funcionId = id;
      if (id) {
        this.atributoFuncionalidadService
          .findByFuncionalidadId(id)
          .pipe()
          .subscribe({
            next: (res: any) => {
              this.onSuccess(res.body);
            },
          });
      }
    });
  }

  protected onSuccess(data: any): void {
    this.atributosFuncionalidads = data?.filter((e: { marcado: boolean }) => e.marcado === this.destacados) ?? [];
    // this.atributosFuncionalidads = data ?? [];
    // eslint-disable-next-line no-console
    // console.log(this.atributosFuncionalidads);
  }

  delete(atributoFuncionalidad: IAtributoFuncionalidad): void {
    const modalRef = this.modalService.open(AtributoFuncionalidadDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.atributoFuncionalidad = atributoFuncionalidad;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe({
      next: () => {
        this.atributosFuncionalidads = this.atributosFuncionalidads?.filter(atributo => atributo !== atributoFuncionalidad);
      },
    });
  }
}
