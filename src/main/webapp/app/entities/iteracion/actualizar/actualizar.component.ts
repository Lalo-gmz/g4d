import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IteracionService } from '../service/iteracion.service';

@Component({
  selector: 'jhi-actualizar',
  templateUrl: './actualizar.component.html',
})
export class ActualizarComponent implements OnInit {
  importForm = new FormGroup({
    file: new FormControl('', Validators.required),
  });

  registros?: number;

  proyectId?: number;
  excel?: any;

  enviado = false;

  faltaArchivo = false;
  enviadoCorrecto = false;

  constructor(protected activeRoute: ActivatedRoute, protected iteracionService: IteracionService) {}

  ngOnInit(): void {
    this.proyectId = this.activeRoute.snapshot.params['id'];
  }

  save(): void {
    if (this.importForm.valid && this.proyectId) {
      this.enviado = true;
      const formulario = new FormData();
      formulario.append('file', this.excel);

      this.iteracionService.import(formulario, this.proyectId).subscribe({
        next: (res: any) => {
          this.importForm.get('file')?.setValue('');
          this.enviadoCorrecto = true;
          this.faltaArchivo = false;
          this.registros = res.length;
        },
      });
    } else {
      this.faltaArchivo = true;
    }
  }

  onFileSelected(event: any): void {
    this.excel = <File>event.target.files[0];
  }

  previousState(): void {
    window.history.back();
  }
}
