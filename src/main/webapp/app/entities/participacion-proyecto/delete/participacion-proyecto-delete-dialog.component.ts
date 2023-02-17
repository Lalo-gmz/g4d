import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IParticipacionProyecto } from '../participacion-proyecto.model';
import { ParticipacionProyectoService } from '../service/participacion-proyecto.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './participacion-proyecto-delete-dialog.component.html',
})
export class ParticipacionProyectoDeleteDialogComponent {
  participacionProyecto?: IParticipacionProyecto;

  constructor(protected participacionProyectoService: ParticipacionProyectoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.participacionProyectoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
