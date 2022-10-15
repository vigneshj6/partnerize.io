import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentEventComponent } from './list/payment-event.component';
import { PaymentEventDetailComponent } from './detail/payment-event-detail.component';
import { PaymentEventUpdateComponent } from './update/payment-event-update.component';
import { PaymentEventDeleteDialogComponent } from './delete/payment-event-delete-dialog.component';
import { PaymentEventRoutingModule } from './route/payment-event-routing.module';

@NgModule({
  imports: [SharedModule, PaymentEventRoutingModule],
  declarations: [PaymentEventComponent, PaymentEventDetailComponent, PaymentEventUpdateComponent, PaymentEventDeleteDialogComponent],
})
export class PaymentEventModule {}
