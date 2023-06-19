import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IScript } from '../script.model';

@Component({
  selector: 'jhi-script-detail',
  templateUrl: './script-detail.component.html',
})
export class ScriptDetailComponent implements OnInit {
  script: IScript | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ script }) => {
      this.script = script;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
