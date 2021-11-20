import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIsAkisi } from '../is-akisi.model';
import { IsAkisiService } from '../service/is-akisi.service';

@Component({
  templateUrl: './is-akisi-delete-dialog.component.html',
})
export class IsAkisiDeleteDialogComponent {
  isAkisi?: IIsAkisi;

  constructor(protected isAkisiService: IsAkisiService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.isAkisiService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
