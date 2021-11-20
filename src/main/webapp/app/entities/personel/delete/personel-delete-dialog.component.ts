import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonel } from '../personel.model';
import { PersonelService } from '../service/personel.service';

@Component({
  templateUrl: './personel-delete-dialog.component.html',
})
export class PersonelDeleteDialogComponent {
  personel?: IPersonel;

  constructor(protected personelService: PersonelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
