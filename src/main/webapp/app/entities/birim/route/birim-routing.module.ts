import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BirimComponent } from '../list/birim.component';
import { BirimDetailComponent } from '../detail/birim-detail.component';
import { BirimUpdateComponent } from '../update/birim-update.component';
import { BirimRoutingResolveService } from './birim-routing-resolve.service';

const birimRoute: Routes = [
  {
    path: '',
    component: BirimComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BirimDetailComponent,
    resolve: {
      birim: BirimRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BirimUpdateComponent,
    resolve: {
      birim: BirimRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BirimUpdateComponent,
    resolve: {
      birim: BirimRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(birimRoute)],
  exports: [RouterModule],
})
export class BirimRoutingModule {}
