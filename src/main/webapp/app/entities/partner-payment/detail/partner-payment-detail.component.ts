import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPartnerPayment } from '../partner-payment.model';

@Component({
  selector: 'jhi-partner-payment-detail',
  templateUrl: './partner-payment-detail.component.html',
})
export class PartnerPaymentDetailComponent implements OnInit {
  partnerPayment: IPartnerPayment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partnerPayment }) => {
      this.partnerPayment = partnerPayment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
