import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEstatusFuncionalidad } from '../estatus-funcionalidad.model';
import { EstatusFuncionalidadService } from '../service/estatus-funcionalidad.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './estatus-funcionalidad-delete-dialog.component.html',
})
export class EstatusFuncionalidadDeleteDialogComponent {
  estatusFuncionalidad?: IEstatusFuncionalidad;

  constructor(protected estatusFuncionalidadService: EstatusFuncionalidadService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.estatusFuncionalidadService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
