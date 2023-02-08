import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIteracion } from '../iteracion.model';
import { IteracionService } from '../service/iteracion.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './iteracion-delete-dialog.component.html',
})
export class IteracionDeleteDialogComponent {
  iteracion?: IIteracion;

  constructor(protected iteracionService: IteracionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.iteracionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
