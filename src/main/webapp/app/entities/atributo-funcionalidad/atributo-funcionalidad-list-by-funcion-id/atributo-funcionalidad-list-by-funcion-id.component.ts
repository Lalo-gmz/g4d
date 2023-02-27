import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IAtributoFuncionalidad } from '../atributo-funcionalidad.model';
import { AtributoFuncionalidadService } from '../service/atributo-funcionalidad.service';

@Component({
  selector: 'jhi-atributo-funcionalidad-list-by-funcion-id',
  templateUrl: './atributo-funcionalidad-list-by-funcion-id.component.html',
  styleUrls: ['./atributo-funcionalidad-list-by-funcion-id.component.scss'],
})
export class AtributoFuncionalidadListByFuncionIdComponent implements OnInit {
  @Input() destacados: boolean | false | undefined;

  atributosFuncionalidads?: IAtributoFuncionalidad[];

  constructor(protected atributoFuncionalidadService: AtributoFuncionalidadService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      // eslint-disable-next-line radix
      const id = parseInt(params['id']);
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
    console.log(this.atributosFuncionalidads);
  }
}
