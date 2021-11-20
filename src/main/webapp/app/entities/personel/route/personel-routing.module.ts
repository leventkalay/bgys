import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonelComponent } from '../list/personel.component';
import { PersonelDetailComponent } from '../detail/personel-detail.component';
import { PersonelUpdateComponent } from '../update/personel-update.component';
import { PersonelRoutingResolveService } from './personel-routing-resolve.service';

const personelRoute: Routes = [
  {
    path: '',
    component: PersonelComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonelDetailComponent,
    resolve: {
      personel: PersonelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonelUpdateComponent,
    resolve: {
      personel: PersonelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonelUpdateComponent,
    resolve: {
      personel: PersonelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personelRoute)],
  exports: [RouterModule],
})
export class PersonelRoutingModule {}
