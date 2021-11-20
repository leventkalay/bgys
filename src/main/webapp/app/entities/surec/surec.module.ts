import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SurecComponent } from './list/surec.component';
import { SurecDetailComponent } from './detail/surec-detail.component';
import { SurecUpdateComponent } from './update/surec-update.component';
import { SurecDeleteDialogComponent } from './delete/surec-delete-dialog.component';
import { SurecRoutingModule } from './route/surec-routing.module';

@NgModule({
  imports: [SharedModule, SurecRoutingModule],
  declarations: [SurecComponent, SurecDetailComponent, SurecUpdateComponent, SurecDeleteDialogComponent],
  entryComponents: [SurecDeleteDialogComponent],
})
export class SurecModule {}
