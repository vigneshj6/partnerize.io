import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PartnerPaymentComponent } from '../list/partner-payment.component';
import { PartnerPaymentDetailComponent } from '../detail/partner-payment-detail.component';
import { PartnerPaymentUpdateComponent } from '../update/partner-payment-update.component';
import { PartnerPaymentRoutingResolveService } from './partner-payment-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const partnerPaymentRoute: Routes = [
  {
    path: '',
    component: PartnerPaymentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PartnerPaymentDetailComponent,
    resolve: {
      partnerPayment: PartnerPaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PartnerPaymentUpdateComponent,
    resolve: {
      partnerPayment: PartnerPaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PartnerPaymentUpdateComponent,
    resolve: {
      partnerPayment: PartnerPaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(partnerPaymentRoute)],
  exports: [RouterModule],
})
export class PartnerPaymentRoutingModule {}
