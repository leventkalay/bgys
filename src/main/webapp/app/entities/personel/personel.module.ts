import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PersonelComponent } from './list/personel.component';
import { PersonelDetailComponent } from './detail/personel-detail.component';
import { PersonelUpdateComponent } from './update/personel-update.component';
import { PersonelDeleteDialogComponent } from './delete/personel-delete-dialog.component';
import { PersonelRoutingModule } from './route/personel-routing.module';

@NgModule({
  imports: [SharedModule, PersonelRoutingModule],
  declarations: [PersonelComponent, PersonelDetailComponent, PersonelUpdateComponent, PersonelDeleteDialogComponent],
  entryComponents: [PersonelDeleteDialogComponent],
})
export class PersonelModule {}
