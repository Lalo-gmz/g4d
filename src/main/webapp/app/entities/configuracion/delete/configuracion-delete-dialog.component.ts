import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConfiguracion } from '../configuracion.model';
import { ConfiguracionService } from '../service/configuracion.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './configuracion-delete-dialog.component.html',
})
export class ConfiguracionDeleteDialogComponent {
  configuracion?: IConfiguracion;

  constructor(protected configuracionService: ConfiguracionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.configuracionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
