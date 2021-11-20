import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IsAkisiComponent } from '../list/is-akisi.component';
import { IsAkisiDetailComponent } from '../detail/is-akisi-detail.component';
import { IsAkisiUpdateComponent } from '../update/is-akisi-update.component';
import { IsAkisiRoutingResolveService } from './is-akisi-routing-resolve.service';

const isAkisiRoute: Routes = [
  {
    path: '',
    component: IsAkisiComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IsAkisiDetailComponent,
    resolve: {
      isAkisi: IsAkisiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IsAkisiUpdateComponent,
    resolve: {
      isAkisi: IsAkisiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IsAkisiUpdateComponent,
    resolve: {
      isAkisi: IsAkisiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(isAkisiRoute)],
  exports: [RouterModule],
})
export class IsAkisiRoutingModule {}
