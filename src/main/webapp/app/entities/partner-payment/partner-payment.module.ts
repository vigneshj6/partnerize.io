import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PartnerPaymentComponent } from './list/partner-payment.component';
import { PartnerPaymentDetailComponent } from './detail/partner-payment-detail.component';
import { PartnerPaymentUpdateComponent } from './update/partner-payment-update.component';
import { PartnerPaymentDeleteDialogComponent } from './delete/partner-payment-delete-dialog.component';
import { PartnerPaymentRoutingModule } from './route/partner-payment-routing.module';

@NgModule({
  imports: [SharedModule, PartnerPaymentRoutingModule],
  declarations: [
    PartnerPaymentComponent,
    PartnerPaymentDetailComponent,
    PartnerPaymentUpdateComponent,
    PartnerPaymentDeleteDialogComponent,
  ],
})
export class PartnerPaymentModule {}
