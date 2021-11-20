import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITehditKategorisi } from '../tehdit-kategorisi.model';
import { TehditKategorisiService } from '../service/tehdit-kategorisi.service';

@Component({
  templateUrl: './tehdit-kategorisi-delete-dialog.component.html',
})
export class TehditKategorisiDeleteDialogComponent {
  tehditKategorisi?: ITehditKategorisi;

  constructor(protected tehditKategorisiService: TehditKategorisiService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tehditKategorisiService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
