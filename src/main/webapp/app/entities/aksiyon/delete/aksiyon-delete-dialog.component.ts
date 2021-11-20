import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAksiyon } from '../aksiyon.model';
import { AksiyonService } from '../service/aksiyon.service';

@Component({
  templateUrl: './aksiyon-delete-dialog.component.html',
})
export class AksiyonDeleteDialogComponent {
  aksiyon?: IAksiyon;

  constructor(protected aksiyonService: AksiyonService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aksiyonService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
