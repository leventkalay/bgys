import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRisk } from '../risk.model';
import { RiskService } from '../service/risk.service';

@Component({
  templateUrl: './risk-delete-dialog.component.html',
})
export class RiskDeleteDialogComponent {
  risk?: IRisk;

  constructor(protected riskService: RiskService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.riskService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
