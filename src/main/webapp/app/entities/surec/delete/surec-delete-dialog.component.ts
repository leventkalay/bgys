import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISurec } from '../surec.model';
import { SurecService } from '../service/surec.service';

@Component({
  templateUrl: './surec-delete-dialog.component.html',
})
export class SurecDeleteDialogComponent {
  surec?: ISurec;

  constructor(protected surecService: SurecService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.surecService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
