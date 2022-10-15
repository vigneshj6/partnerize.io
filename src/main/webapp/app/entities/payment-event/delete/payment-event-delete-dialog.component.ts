import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentEvent } from '../payment-event.model';
import { PaymentEventService } from '../service/payment-event.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './payment-event-delete-dialog.component.html',
})
export class PaymentEventDeleteDialogComponent {
  paymentEvent?: IPaymentEvent;

  constructor(protected paymentEventService: PaymentEventService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentEventService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
