import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITehdit } from '../tehdit.model';
import { TehditService } from '../service/tehdit.service';

@Component({
  templateUrl: './tehdit-delete-dialog.component.html',
})
export class TehditDeleteDialogComponent {
  tehdit?: ITehdit;

  constructor(protected tehditService: TehditService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tehditService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
