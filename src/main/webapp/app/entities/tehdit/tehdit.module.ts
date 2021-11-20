import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TehditComponent } from './list/tehdit.component';
import { TehditDetailComponent } from './detail/tehdit-detail.component';
import { TehditUpdateComponent } from './update/tehdit-update.component';
import { TehditDeleteDialogComponent } from './delete/tehdit-delete-dialog.component';
import { TehditRoutingModule } from './route/tehdit-routing.module';

@NgModule({
  imports: [SharedModule, TehditRoutingModule],
  declarations: [TehditComponent, TehditDetailComponent, TehditUpdateComponent, TehditDeleteDialogComponent],
  entryComponents: [TehditDeleteDialogComponent],
})
export class TehditModule {}
