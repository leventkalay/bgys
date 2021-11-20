import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SurecComponent } from '../list/surec.component';
import { SurecDetailComponent } from '../detail/surec-detail.component';
import { SurecUpdateComponent } from '../update/surec-update.component';
import { SurecRoutingResolveService } from './surec-routing-resolve.service';

const surecRoute: Routes = [
  {
    path: '',
    component: SurecComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SurecDetailComponent,
    resolve: {
      surec: SurecRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SurecUpdateComponent,
    resolve: {
      surec: SurecRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SurecUpdateComponent,
    resolve: {
      surec: SurecRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(surecRoute)],
  exports: [RouterModule],
})
export class SurecRoutingModule {}
