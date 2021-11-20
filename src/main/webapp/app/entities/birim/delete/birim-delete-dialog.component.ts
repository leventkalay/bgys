import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBirim } from '../birim.model';
import { BirimService } from '../service/birim.service';

@Component({
  templateUrl: './birim-delete-dialog.component.html',
})
export class BirimDeleteDialogComponent {
  birim?: IBirim;

  constructor(protected birimService: BirimService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.birimService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
