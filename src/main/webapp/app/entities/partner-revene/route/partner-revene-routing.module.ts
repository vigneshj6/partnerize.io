import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PartnerReveneComponent } from '../list/partner-revene.component';
import { PartnerReveneDetailComponent } from '../detail/partner-revene-detail.component';
import { PartnerReveneUpdateComponent } from '../update/partner-revene-update.component';
import { PartnerReveneRoutingResolveService } from './partner-revene-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const partnerReveneRoute: Routes = [
  {
    path: '',
    component: PartnerReveneComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PartnerReveneDetailComponent,
    resolve: {
      partnerRevene: PartnerReveneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PartnerReveneUpdateComponent,
    resolve: {
      partnerRevene: PartnerReveneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PartnerReveneUpdateComponent,
    resolve: {
      partnerRevene: PartnerReveneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(partnerReveneRoute)],
  exports: [RouterModule],
})
export class PartnerReveneRoutingModule {}
