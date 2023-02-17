import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrioridad } from '../prioridad.model';
import { PrioridadService } from '../service/prioridad.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './prioridad-delete-dialog.component.html',
})
export class PrioridadDeleteDialogComponent {
  prioridad?: IPrioridad;

  constructor(protected prioridadService: PrioridadService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prioridadService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
