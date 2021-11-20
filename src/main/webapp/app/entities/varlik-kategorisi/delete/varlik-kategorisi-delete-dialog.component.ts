import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVarlikKategorisi } from '../varlik-kategorisi.model';
import { VarlikKategorisiService } from '../service/varlik-kategorisi.service';

@Component({
  templateUrl: './varlik-kategorisi-delete-dialog.component.html',
})
export class VarlikKategorisiDeleteDialogComponent {
  varlikKategorisi?: IVarlikKategorisi;

  constructor(protected varlikKategorisiService: VarlikKategorisiService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.varlikKategorisiService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
