import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IScript } from '../script.model';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ScriptService } from '../service/script.service';

@Component({
  templateUrl: './script-delete-dialog.component.html',
})
export class ScriptDeleteDialogComponent {
  script?: IScript;

  constructor(protected scriptService: ScriptService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.scriptService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
