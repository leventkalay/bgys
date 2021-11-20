import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VarlikKategorisiComponent } from '../list/varlik-kategorisi.component';
import { VarlikKategorisiDetailComponent } from '../detail/varlik-kategorisi-detail.component';
import { VarlikKategorisiUpdateComponent } from '../update/varlik-kategorisi-update.component';
import { VarlikKategorisiRoutingResolveService } from './varlik-kategorisi-routing-resolve.service';

const varlikKategorisiRoute: Routes = [
  {
    path: '',
    component: VarlikKategorisiComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VarlikKategorisiDetailComponent,
    resolve: {
      varlikKategorisi: VarlikKategorisiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VarlikKategorisiUpdateComponent,
    resolve: {
      varlikKategorisi: VarlikKategorisiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VarlikKategorisiUpdateComponent,
    resolve: {
      varlikKategorisi: VarlikKategorisiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(varlikKategorisiRoute)],
  exports: [RouterModule],
})
export class VarlikKategorisiRoutingModule {}
