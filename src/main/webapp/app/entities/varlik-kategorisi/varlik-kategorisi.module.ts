import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VarlikKategorisiComponent } from './list/varlik-kategorisi.component';
import { VarlikKategorisiDetailComponent } from './detail/varlik-kategorisi-detail.component';
import { VarlikKategorisiUpdateComponent } from './update/varlik-kategorisi-update.component';
import { VarlikKategorisiDeleteDialogComponent } from './delete/varlik-kategorisi-delete-dialog.component';
import { VarlikKategorisiRoutingModule } from './route/varlik-kategorisi-routing.module';

@NgModule({
  imports: [SharedModule, VarlikKategorisiRoutingModule],
  declarations: [
    VarlikKategorisiComponent,
    VarlikKategorisiDetailComponent,
    VarlikKategorisiUpdateComponent,
    VarlikKategorisiDeleteDialogComponent,
  ],
  entryComponents: [VarlikKategorisiDeleteDialogComponent],
})
export class VarlikKategorisiModule {}
