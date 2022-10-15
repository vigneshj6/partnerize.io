import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPartnerPayment } from '../partner-payment.model';
import { PartnerPaymentService } from '../service/partner-payment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './partner-payment-delete-dialog.component.html',
})
export class PartnerPaymentDeleteDialogComponent {
  partnerPayment?: IPartnerPayment;

  constructor(protected partnerPaymentService: PartnerPaymentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.partnerPaymentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
