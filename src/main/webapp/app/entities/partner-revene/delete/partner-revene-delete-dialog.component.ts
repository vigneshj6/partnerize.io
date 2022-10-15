import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPartnerRevene } from '../partner-revene.model';
import { PartnerReveneService } from '../service/partner-revene.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './partner-revene-delete-dialog.component.html',
})
export class PartnerReveneDeleteDialogComponent {
  partnerRevene?: IPartnerRevene;

  constructor(protected partnerReveneService: PartnerReveneService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.partnerReveneService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
