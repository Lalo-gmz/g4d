import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParticipacionProyecto } from '../participacion-proyecto.model';

@Component({
  selector: 'jhi-participacion-proyecto-detail',
  templateUrl: './participacion-proyecto-detail.component.html',
})
export class ParticipacionProyectoDetailComponent implements OnInit {
  participacionProyecto: IParticipacionProyecto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ participacionProyecto }) => {
      this.participacionProyecto = participacionProyecto;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
