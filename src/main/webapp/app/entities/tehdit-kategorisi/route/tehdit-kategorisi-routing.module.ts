import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TehditKategorisiComponent } from '../list/tehdit-kategorisi.component';
import { TehditKategorisiDetailComponent } from '../detail/tehdit-kategorisi-detail.component';
import { TehditKategorisiUpdateComponent } from '../update/tehdit-kategorisi-update.component';
import { TehditKategorisiRoutingResolveService } from './tehdit-kategorisi-routing-resolve.service';

const tehditKategorisiRoute: Routes = [
  {
    path: '',
    component: TehditKategorisiComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TehditKategorisiDetailComponent,
    resolve: {
      tehditKategorisi: TehditKategorisiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TehditKategorisiUpdateComponent,
    resolve: {
      tehditKategorisi: TehditKategorisiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TehditKategorisiUpdateComponent,
    resolve: {
      tehditKategorisi: TehditKategorisiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tehditKategorisiRoute)],
  exports: [RouterModule],
})
export class TehditKategorisiRoutingModule {}
