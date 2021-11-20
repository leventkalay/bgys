import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TehditKategorisiComponent } from './list/tehdit-kategorisi.component';
import { TehditKategorisiDetailComponent } from './detail/tehdit-kategorisi-detail.component';
import { TehditKategorisiUpdateComponent } from './update/tehdit-kategorisi-update.component';
import { TehditKategorisiDeleteDialogComponent } from './delete/tehdit-kategorisi-delete-dialog.component';
import { TehditKategorisiRoutingModule } from './route/tehdit-kategorisi-routing.module';

@NgModule({
  imports: [SharedModule, TehditKategorisiRoutingModule],
  declarations: [
    TehditKategorisiComponent,
    TehditKategorisiDetailComponent,
    TehditKategorisiUpdateComponent,
    TehditKategorisiDeleteDialogComponent,
  ],
  entryComponents: [TehditKategorisiDeleteDialogComponent],
})
export class TehditKategorisiModule {}
