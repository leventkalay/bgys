import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IsAkisiComponent } from './list/is-akisi.component';
import { IsAkisiDetailComponent } from './detail/is-akisi-detail.component';
import { IsAkisiUpdateComponent } from './update/is-akisi-update.component';
import { IsAkisiDeleteDialogComponent } from './delete/is-akisi-delete-dialog.component';
import { IsAkisiRoutingModule } from './route/is-akisi-routing.module';

@NgModule({
  imports: [SharedModule, IsAkisiRoutingModule],
  declarations: [IsAkisiComponent, IsAkisiDetailComponent, IsAkisiUpdateComponent, IsAkisiDeleteDialogComponent],
  entryComponents: [IsAkisiDeleteDialogComponent],
})
export class IsAkisiModule {}
