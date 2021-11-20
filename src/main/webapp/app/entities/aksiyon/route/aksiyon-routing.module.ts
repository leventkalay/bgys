import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AksiyonComponent } from '../list/aksiyon.component';
import { AksiyonDetailComponent } from '../detail/aksiyon-detail.component';
import { AksiyonUpdateComponent } from '../update/aksiyon-update.component';
import { AksiyonRoutingResolveService } from './aksiyon-routing-resolve.service';

const aksiyonRoute: Routes = [
  {
    path: '',
    component: AksiyonComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AksiyonDetailComponent,
    resolve: {
      aksiyon: AksiyonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AksiyonUpdateComponent,
    resolve: {
      aksiyon: AksiyonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AksiyonUpdateComponent,
    resolve: {
      aksiyon: AksiyonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aksiyonRoute)],
  exports: [RouterModule],
})
export class AksiyonRoutingModule {}
