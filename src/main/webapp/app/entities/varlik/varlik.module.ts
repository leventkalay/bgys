import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VarlikComponent } from './list/varlik.component';
import { VarlikDetailComponent } from './detail/varlik-detail.component';
import { VarlikUpdateComponent } from './update/varlik-update.component';
import { VarlikDeleteDialogComponent } from './delete/varlik-delete-dialog.component';
import { VarlikRoutingModule } from './route/varlik-routing.module';
import { SelectButtonModule } from 'primeng/selectbutton';

@NgModule({
  imports: [SharedModule, VarlikRoutingModule, SelectButtonModule],
  declarations: [VarlikComponent, VarlikDetailComponent, VarlikUpdateComponent, VarlikDeleteDialogComponent],
  entryComponents: [VarlikDeleteDialogComponent],
})
export class VarlikModule {}
