import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IteracionService } from '../service/iteracion.service';

@Component({
  selector: 'jhi-importar-component',
  templateUrl: './importar-component.component.html',
  styleUrls: ['./importar-component.component.scss'],
})
export class ImportarComponentComponent implements OnInit {
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

  save() {
    if (this.importForm.valid && this.proyectId) {
      this.enviado = true;
      let formulario = new FormData();
      formulario.append('file', this.excel);

      this.iteracionService.import(formulario, this.proyectId).subscribe({
        next: (res: any) => {
          this.importForm.get('file')?.setValue('');
          this.enviadoCorrecto = true;
          this.faltaArchivo = false;
          this.registros = res.length;
          console.table(res);
          console.log({ res });
        },
      });
    } else {
      this.faltaArchivo = true;
    }
  }

  onFileSelected(event: any) {
    this.excel = <File>event.target.files[0];
  }

  previousState(): void {
    window.history.back();
  }
}
