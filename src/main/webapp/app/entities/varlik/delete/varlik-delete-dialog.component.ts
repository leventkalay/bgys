import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVarlik } from '../varlik.model';
import { VarlikService } from '../service/varlik.service';

@Component({
  templateUrl: './varlik-delete-dialog.component.html',
})
export class VarlikDeleteDialogComponent {
  varlik?: IVarlik;

  constructor(protected varlikService: VarlikService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.varlikService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
