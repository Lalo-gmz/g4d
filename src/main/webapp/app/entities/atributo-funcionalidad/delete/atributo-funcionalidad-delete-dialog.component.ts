import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAtributoFuncionalidad } from '../atributo-funcionalidad.model';
import { AtributoFuncionalidadService } from '../service/atributo-funcionalidad.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './atributo-funcionalidad-delete-dialog.component.html',
})
export class AtributoFuncionalidadDeleteDialogComponent {
  atributoFuncionalidad?: IAtributoFuncionalidad;

  constructor(protected atributoFuncionalidadService: AtributoFuncionalidadService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.atributoFuncionalidadService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
