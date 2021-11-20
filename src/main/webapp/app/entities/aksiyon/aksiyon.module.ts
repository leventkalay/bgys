import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AksiyonComponent } from './list/aksiyon.component';
import { AksiyonDetailComponent } from './detail/aksiyon-detail.component';
import { AksiyonUpdateComponent } from './update/aksiyon-update.component';
import { AksiyonDeleteDialogComponent } from './delete/aksiyon-delete-dialog.component';
import { AksiyonRoutingModule } from './route/aksiyon-routing.module';

@NgModule({
  imports: [SharedModule, AksiyonRoutingModule],
  declarations: [AksiyonComponent, AksiyonDetailComponent, AksiyonUpdateComponent, AksiyonDeleteDialogComponent],
  entryComponents: [AksiyonDeleteDialogComponent],
})
export class AksiyonModule {}
