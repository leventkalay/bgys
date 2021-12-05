import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RiskComponent } from './list/risk.component';
import { RiskDetailComponent } from './detail/risk-detail.component';
import { RiskUpdateComponent } from './update/risk-update.component';
import { RiskDeleteDialogComponent } from './delete/risk-delete-dialog.component';
import { RiskRoutingModule } from './route/risk-routing.module';
import { SelectButtonModule } from 'primeng/selectbutton';

@NgModule({
  imports: [SharedModule, RiskRoutingModule, SelectButtonModule],
  declarations: [RiskComponent, RiskDetailComponent, RiskUpdateComponent, RiskDeleteDialogComponent],
  entryComponents: [RiskDeleteDialogComponent],
})
export class RiskModule {}
