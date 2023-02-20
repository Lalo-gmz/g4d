import { IComentario } from './../comentario.model';
import { Component, OnInit } from '@angular/core';
import { ComentarioService } from '../service/comentario.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-list-by-id',
  templateUrl: './list-by-id.component.html',
  styleUrls: ['./list-by-id.component.scss'],
})
export class ComentarioListByIdComponent implements OnInit {
  comentarios?: IComentario[];
  constructor(protected comentarioService: ComentarioService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      // eslint-disable-next-line radix
      const id = parseInt(params['id']);
      if (id) {
        this.comentarioService
          .findByFuncId(id)
          .pipe()
          .subscribe({
            next: (res: any) => {
              this.onSuccess(res.body);
            },
            error: () => {
              this.onError();
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

  protected onError(): void {
    // eslint-disable-next-line no-console
    console.log('Error');
  }
}
