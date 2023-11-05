import { Component, Input } from '@angular/core';

@Component({
  selector: 'jhi-func-by-proyecto',
  templateUrl: './func-by-proyecto.component.html',
})
export class FuncByProyectoComponent {
  @Input() proyectoId!: number;
}
