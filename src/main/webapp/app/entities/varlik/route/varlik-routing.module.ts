import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VarlikComponent } from '../list/varlik.component';
import { VarlikDetailComponent } from '../detail/varlik-detail.component';
import { VarlikUpdateComponent } from '../update/varlik-update.component';
import { VarlikRoutingResolveService } from './varlik-routing-resolve.service';

const varlikRoute: Routes = [
  {
    path: '',
    component: VarlikComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VarlikDetailComponent,
    resolve: {
      varlik: VarlikRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VarlikUpdateComponent,
    resolve: {
      varlik: VarlikRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VarlikUpdateComponent,
    resolve: {
      varlik: VarlikRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(varlikRoute)],
  exports: [RouterModule],
})
export class VarlikRoutingModule {}
