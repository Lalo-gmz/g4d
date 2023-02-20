import { Component, OnInit } from '@angular/core';
import { IEtiqueta } from '../etiqueta.model';
import { EtiquetaService } from '../service/etiqueta.service';

import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-etiqueta-list-by-funcional-id',
  templateUrl: './etiqueta-list-by-funcional-id.component.html',
  styleUrls: ['./etiqueta-list-by-funcional-id.component.scss'],
})
export class EtiquetaListByFuncionalIdComponent implements OnInit {
  etiquetas?: IEtiqueta[];
  constructor(protected etiquetaService: EtiquetaService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      // eslint-disable-next-line radix
      const id = parseInt(params['id']);
      if (id) {
        this.etiquetaService
          .findByFuncionalidadId(id)
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
    this.etiquetas = data ?? [];
    this.agreagarPropContraste();
    // eslint-disable-next-line no-console
    console.log(this.etiquetas);
  }

  protected agreagarPropContraste(): void {
    if (this.etiquetas !== undefined) {
      this.etiquetas.forEach(e => {
        e.contrasteColor = this.getContrastColor(e.color);
      });
    }
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

  protected onError(): void {
    // eslint-disable-next-line no-console
    console.log('Error');
  }
}
