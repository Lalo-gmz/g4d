import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBitacora } from '../bitacora.model';
import { BitacoraService } from '../service/bitacora.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './bitacora-delete-dialog.component.html',
})
export class BitacoraDeleteDialogComponent {
  bitacora?: IBitacora;

  constructor(protected bitacoraService: BitacoraService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bitacoraService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
