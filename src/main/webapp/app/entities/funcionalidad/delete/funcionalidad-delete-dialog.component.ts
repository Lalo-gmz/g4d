import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFuncionalidad } from '../funcionalidad.model';
import { FuncionalidadService } from '../service/funcionalidad.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './funcionalidad-delete-dialog.component.html',
})
export class FuncionalidadDeleteDialogComponent {
  funcionalidad?: IFuncionalidad;

  constructor(protected funcionalidadService: FuncionalidadService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.funcionalidadService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
