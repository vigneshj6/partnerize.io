import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentEventComponent } from '../list/payment-event.component';
import { PaymentEventDetailComponent } from '../detail/payment-event-detail.component';
import { PaymentEventUpdateComponent } from '../update/payment-event-update.component';
import { PaymentEventRoutingResolveService } from './payment-event-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const paymentEventRoute: Routes = [
  {
    path: '',
    component: PaymentEventComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentEventDetailComponent,
    resolve: {
      paymentEvent: PaymentEventRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentEventUpdateComponent,
    resolve: {
      paymentEvent: PaymentEventRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentEventUpdateComponent,
    resolve: {
      paymentEvent: PaymentEventRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentEventRoute)],
  exports: [RouterModule],
})
export class PaymentEventRoutingModule {}
