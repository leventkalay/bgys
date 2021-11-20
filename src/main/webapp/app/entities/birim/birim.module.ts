import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BirimComponent } from './list/birim.component';
import { BirimDetailComponent } from './detail/birim-detail.component';
import { BirimUpdateComponent } from './update/birim-update.component';
import { BirimDeleteDialogComponent } from './delete/birim-delete-dialog.component';
import { BirimRoutingModule } from './route/birim-routing.module';

@NgModule({
  imports: [SharedModule, BirimRoutingModule],
  declarations: [BirimComponent, BirimDetailComponent, BirimUpdateComponent, BirimDeleteDialogComponent],
  entryComponents: [BirimDeleteDialogComponent],
})
export class BirimModule {}
