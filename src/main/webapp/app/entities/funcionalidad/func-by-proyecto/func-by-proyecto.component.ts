import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'jhi-func-by-proyecto',
  templateUrl: './func-by-proyecto.component.html',
  styleUrls: ['./func-by-proyecto.component.scss'],
})
export class FuncByProyectoComponent implements OnInit {
  @Input() proyectoId!: number;

  constructor() {}

  ngOnInit(): void {
    console.log(this.proyectoId);
  }
}
