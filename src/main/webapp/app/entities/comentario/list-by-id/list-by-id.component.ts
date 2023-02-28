import { IComentario, NewComentario } from './../comentario.model';
import { Component, OnInit } from '@angular/core';
import { ComentarioService } from '../service/comentario.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ComentarioFormService } from '../update/comentario-form.service';
import { AccountService } from 'app/core/auth/account.service';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';

@Component({
  selector: 'jhi-list-by-id',
  templateUrl: './list-by-id.component.html',
  styleUrls: ['./list-by-id.component.scss'],
})
export class ComentarioListByIdComponent implements OnInit {
  comentarios?: IComentario[];
  isSaving = false;
  comentario: IComentario | null = null;
  funcionalodadId: number | undefined;
  myForm: FormGroup;
  funcionalidad?: IFuncionalidad;

  constructor(
    protected funcionalidadService: FuncionalidadService,
    protected accountService: AccountService,
    protected comentarioFormService: ComentarioFormService,
    protected comentarioService: ComentarioService,
    private route: ActivatedRoute,
    protected formBuilder: FormBuilder
  ) {
    this.myForm = this.formBuilder.group({
      mensaje: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      // eslint-disable-next-line radix
      const id = parseInt(params['id']);
      this.funcionalodadId = id;
      if (id) {
        this.comentarioService
          .findByFuncId(id)
          .pipe()
          .subscribe({
            next: (res: any) => {
              this.onSuccess(res.body);
            },
            error: e => {
              this.onError(e);
            },
          });

        this.funcionalidadService.find(this.funcionalodadId).subscribe({
          next: (res: any) => {
            this.funcionalidad = res.body;
          },
        });
      }
    });
  }

  protected onSuccess(data: any): void {
    this.comentarios = data ?? [];
    // eslint-disable-next-line no-console
    console.log(this.comentarios);
  }

  protected onError(e: any): void {
    // eslint-disable-next-line no-console
    console.log(e);
  }

  save(): void {
    this.myForm.addControl('funcionalidad', this.formBuilder.control(this.funcionalidad));
    const msg: NewComentario = this.myForm.value;
    console.log(msg);
    let res = this.comentarioService.createByFuncidAndUserId(msg).subscribe({
      next: res => {
        if (res.body) this.comentarios?.push(res.body);
        this.myForm.reset();
      },
    });
  }
}
