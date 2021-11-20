import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TehditComponent } from '../list/tehdit.component';
import { TehditDetailComponent } from '../detail/tehdit-detail.component';
import { TehditUpdateComponent } from '../update/tehdit-update.component';
import { TehditRoutingResolveService } from './tehdit-routing-resolve.service';

const tehditRoute: Routes = [
  {
    path: '',
    component: TehditComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TehditDetailComponent,
    resolve: {
      tehdit: TehditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TehditUpdateComponent,
    resolve: {
      tehdit: TehditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TehditUpdateComponent,
    resolve: {
      tehdit: TehditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tehditRoute)],
  exports: [RouterModule],
})
export class TehditRoutingModule {}
