import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProyecto } from '../proyecto.model';
import { ProyectoService } from '../service/proyecto.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './proyecto-delete-dialog.component.html',
})
export class ProyectoDeleteDialogComponent {
  proyecto?: IProyecto;

  constructor(protected proyectoService: ProyectoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.proyectoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
