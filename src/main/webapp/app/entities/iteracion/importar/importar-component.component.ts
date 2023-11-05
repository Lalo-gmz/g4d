import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IteracionService } from '../service/iteracion.service';

@Component({
  selector: 'jhi-importar-component',
  templateUrl: './importar-component.component.html',
})
export class ImportarComponentComponent {
  importForm = new FormGroup({
    file: new FormControl('', Validators.required),
  });

  registros?: number;

  proyectId: number;
  excel?: any;

  enviado = false;

  faltaArchivo = false;
  enviadoCorrecto = false;

  constructor(protected activeRoute: ActivatedRoute, protected iteracionService: IteracionService) {
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

  getPlantillaExcel(proyectoId: number): void {
    this.iteracionService.getPlantilla(proyectoId).subscribe({
      next: (res: any) => {
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        document.body.appendChild(a);
        a.style.display = 'none';
        a.href = url;
        a.download = `Pantilla_carga_inicial_G4D.xlsx`; // cambia el nombre del archivo según lo que desees
        a.click();
        window.URL.revokeObjectURL(url);
      },
    });
  }

  onFileSelected(event: any): void {
    this.excel = <File>event.target.files[0];
  }

  previousState(): void {
    window.history.back();
  }
}
